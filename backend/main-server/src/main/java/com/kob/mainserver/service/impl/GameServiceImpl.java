package com.kob.mainserver.service.impl;

import static com.kob.common.constant.Constants.MATCH_SERVER;
import static com.kob.common.enums.SocketResultType.MATCHING_SUCCESS;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kob.common.model.SocketResp;
import com.kob.common.model.dto.PlayerDTO;
import com.kob.common.model.dto.MatchRemoveDTO;
import com.kob.mainserver.mapper.RecordMapper;
import com.kob.mainserver.model.bean.Game;
import com.kob.mainserver.model.bean.Player;
import com.kob.mainserver.model.bean.UserConnection;
import com.kob.mainserver.model.po.Record;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.GameMatchResultVO;
import com.kob.mainserver.service.GameService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Service
public class GameServiceImpl implements GameService {

    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};
    private static final int ROWS = 13;
    private static final int COLS = 14;
    private static final int BLOCK_COUNT = 20;

    private static final String MATCH_ADD_URI = "/match/add";
    private static final String MATCH_REMOVE_URI = "/match/remove";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private Map<Long, UserConnection> users;

    @Override
    public void startMatching(User user) {
        PlayerDTO playerDTO = new PlayerDTO(user.getId(), user.getScore(), 0);
        restTemplate.postForObject(MATCH_SERVER + MATCH_ADD_URI, playerDTO, Object.class);
    }

    @Override
    public void stopMatching(User user) {
        MatchRemoveDTO matchRemoveDTO = new MatchRemoveDTO(user.getId());
        restTemplate.postForObject(MATCH_SERVER + MATCH_REMOVE_URI, matchRemoveDTO, Object.class);
    }

    @Override
    public void saveResult(Game game) {
        Record record = new Record(game);
        recordMapper.insert(record);
    }

    @Override
    public void playerMove(UserConnection userConnection, int direction) {
        User user = userConnection.getUser();
        Game game = userConnection.getGame();
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        if (player1.getId().equals(user.getId())) {
            player1.setNextStep(direction);
        } else if (player2.getId().equals(user.getId())) {
            player2.setNextStep(direction);
        }
    }

    @Override
    public void startGame(Long playerId1, Long playerId2) {
        System.out.println("match success, player" + playerId1 + " vs player" +playerId2);
        createNewGame(users.get(playerId1), users.get(playerId2));
    }

    /**
     * 创建一局新游戏
     */
    private void createNewGame(UserConnection connA, UserConnection connB) {
        User userA = connA.getUser();
        User userB = connB.getUser();

        // 创建
        int[][] gameMap = getGameMap();
        Player playerA = new Player(userA.getId(), ROWS - 2, 1, connA.getWebSocket());
        Player playerB = new Player(userB.getId(), 1, COLS - 2, connB.getWebSocket());
        Game game = new Game(getGameMap(), playerA, playerB, this);
        connA.setGame(game);
        connB.setGame(game);

        // 发送结果
        GameMatchResultVO result1 = new GameMatchResultVO(
                new GameMatchResultVO.Opponent(userB.getUsername(), userB.getAvatar()), 1, gameMap);
        GameMatchResultVO result2 = new GameMatchResultVO(
                new GameMatchResultVO.Opponent(userA.getUsername(), userA.getAvatar()), 2, gameMap);
        connA.getWebSocket().sendMessage(SocketResp.ok(MATCHING_SUCCESS, result1));
        connB.getWebSocket().sendMessage(SocketResp.ok(MATCHING_SUCCESS, result2));

        // 启动游戏
        new Thread(game).start();
    }

    /**
     * 获取地图
     */
    private int[][] getGameMap() {
        int[][] g = new int[ROWS][COLS];
        while (!createWalls(g)) ;
        return g;
    }

    /**
     * 创建障碍物
     */
    private boolean createWalls(int[][] g) {
        // 初始化地图
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                g[i][j] = 0;
            }
        }

        // 给四周加上障碍物
        for (int r = 0; r < ROWS; r++) {
            g[r][0] = g[r][COLS - 1] = 1;
        }
        for (int c = 0; c < COLS; c++) {
            g[0][c] = g[ROWS - 1][c] = 1;
        }

        // 创建随机障碍物
        Random random = new Random(System.currentTimeMillis());
        for (int cnt = 0; cnt < BLOCK_COUNT; ) {
            int r = random.nextInt(ROWS), c = random.nextInt(COLS);
            if (g[r][c] == 1) {
                continue;
            }
            if (r == ROWS - 2 && c == 1 || r == 1 && c == COLS - 2) {
                continue;
            }
            g[r][c] = g[ROWS - r - 1][COLS - c - 1] = 1;

            if (r == ROWS - r - 1 && c == COLS - c - 1) cnt += 1;
            else cnt += 2;
        }

        return checkConnectivity(ROWS - 2, 1, 1, COLS - 2, g);
    }

    /**
     * 判断地图连通性
     */
    private boolean checkConnectivity(int x1, int y1, int x2, int y2, int[][] g) {
        if (x1 == x2 && y1 == y2) return true;
        g[x1][y1] = 1;

        for (int i = 0; i < 4; i++) {
            int x = x1 + dx[i], y = y1 + dy[i];
            if (x >= 1 && x < ROWS - 1 && y >= 1 && y < COLS - 1) {
                if (g[x][y] == 0 && checkConnectivity(x, y, x2, y2, g)) {
                    g[x1][y1] = 0;
                    return true;
                }
            }
        }

        g[x1][y1] = 0;
        return false;
    }
}
