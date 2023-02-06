package com.kob.mainserver.service.impl;

import static com.kob.common.constant.Constants.BLOCK_COUNT;
import static com.kob.common.constant.Constants.BOT_ADD_URL;
import static com.kob.common.constant.Constants.COLS;
import static com.kob.common.constant.Constants.MATCH_ADD_URL;
import static com.kob.common.constant.Constants.MATCH_REMOVE_URL;
import static com.kob.common.constant.Constants.ROWS;
import static com.kob.common.constant.Constants.dx;
import static com.kob.common.constant.Constants.dy;
import static com.kob.common.enums.GameResult.LOSER_1;
import static com.kob.common.enums.GameResult.LOSER_2;
import static com.kob.common.enums.SocketResultType.MATCHING_SUCCESS;
import static com.kob.mainserver.constant.Constants.BENBEN_ID;
import static com.kob.mainserver.constant.Constants.DRAW_SCORE;
import static com.kob.mainserver.constant.Constants.LOSER_SCORE;
import static com.kob.mainserver.constant.Constants.WINNER_SCORE;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kob.common.model.SocketResp;
import com.kob.common.model.dto.GameSituation;
import com.kob.common.model.dto.MatchRemoveDTO;
import com.kob.common.model.dto.PlayerDTO;
import com.kob.common.util.GsonUtils;
import com.kob.mainserver.model.bean.Player;
import com.kob.mainserver.model.bean.UserConnection;
import com.kob.mainserver.model.po.Bot;
import com.kob.mainserver.model.po.Record;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.GameMatchResultVO;
import com.kob.mainserver.service.BotService;
import com.kob.mainserver.service.GameService;
import com.kob.mainserver.service.RecordService;
import com.kob.mainserver.service.UserService;
import com.kob.mainserver.thread.Game;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RecordService recordService;

    @Autowired
    private BotService botService;

    @Autowired
    private UserService userService;

    @Autowired
    private Map<Long, UserConnection> users;

    @Override
    public void startMatching(User user, long botId) {
        PlayerDTO playerDTO = new PlayerDTO(user.getId(), botId, user.getScore(), 0);
        restTemplate.postForObject(MATCH_ADD_URL, playerDTO, Object.class);
    }

    @Override
    public void stopMatching(User user) {
        MatchRemoveDTO matchRemoveDTO = new MatchRemoveDTO(user.getId());
        restTemplate.postForObject(MATCH_REMOVE_URL, matchRemoveDTO, Object.class);
    }

    @Override
    public void saveResult(Game game) {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        int loser = game.getLoser();
        Record record = Record.builder()
                .userId1(player1.getId())
                .userId2(player2.getId() < 0 ? BENBEN_ID : player2.getId())
                .x1(player1.getSx())
                .y1(player1.getSy())
                .x2(player2.getSx())
                .y2(player2.getSy())
                .steps1(GsonUtils.toJson(player1.getSteps()))
                .steps2(GsonUtils.toJson(player2.getSteps()))
                .map(GsonUtils.toJson(game.getGameMap()))
                .loserIdentity(game.getLoser())
                .originalScore1(users.get(player1.getId()).getUser().getScore())
                .originalScore2(users.get(player2.getId()).getUser().getScore())
                .getScore1(loser == LOSER_1.getResultCode() ? LOSER_SCORE
                        : loser == LOSER_2.getResultCode() ? WINNER_SCORE
                        : DRAW_SCORE)
                .getScore2(loser == LOSER_1.getResultCode() ? WINNER_SCORE
                        : loser == LOSER_2.getResultCode() ? LOSER_SCORE
                        : DRAW_SCORE)
                .createTime(new Date())
                .build();
        recordService.insert(record);
    }

    @Override
    public void setNextStep(long userId, int direction, boolean isPlayer) {
        Game game = users.get(userId).getGame();

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        if (player1.getId() == userId) {
            setNextStep(player1, direction, isPlayer);
        } else if (player2.getId() == userId) {
            setNextStep(player2, direction, isPlayer);
        }
    }

    @Override
    public void startGame(long playerId1, long botId1, long playerId2, long botId2) {
        UserConnection conn1 = users.get(playerId1);
        UserConnection conn2 = users.get(playerId2);

        User user1 = conn1.getUser();
        User user2;
        if (conn2 != null) {
            user2 = conn2.getUser();
        } else {
            user2 = userService.selectById(BENBEN_ID);
            user2.setId(playerId2);
            conn2 = new UserConnection(null, user2, null);
            users.put(playerId2, conn2);
        }

        Bot bot1 = botService.selectUserBotById(botId1, user1.getId());
        Bot bot2;
        if (playerId2 < 0) {
            botId2 = playerId2 = BENBEN_ID;
        }
        bot2 = botService.selectUserBotById(botId2, playerId2);

        // 创建
        int[][] gameMap = getGameMap();
        Player player1 = new Player(
                user1.getId(), ROWS - 2, 1, bot1 == null ? null : bot1.getCode(), conn1.getWebSocket());
        Player player2 = new Player(
                user2.getId(), 1, COLS - 2, bot2 == null ? null : bot2.getCode(), conn2.getWebSocket());
        Game game = new Game(gameMap, player1, player2, this, userService);
        conn1.setGame(game);
        conn2.setGame(game);

        // 发送结果
        GameMatchResultVO result1 = new GameMatchResultVO(
                new GameMatchResultVO.Opponent(user2.getUsername(), user2.getAvatar()), 1, gameMap);
        conn1.getWebSocket().sendMessage(SocketResp.ok(MATCHING_SUCCESS, result1));
        if (conn2.getWebSocket() != null) {
            GameMatchResultVO result2 = new GameMatchResultVO(
                    new GameMatchResultVO.Opponent(user1.getUsername(), user1.getAvatar()), 2, gameMap);
            conn2.getWebSocket().sendMessage(SocketResp.ok(MATCHING_SUCCESS, result2));
        }

        // 启动游戏
        new Thread(game).start();
    }

    @Override
    public void setNextStepByBot(long userId, int direction) {
        if (users.get(userId) != null) {
            // 因为可能这个时候玩家离开游戏页面，可以直接判负
            setNextStep(userId, direction, false);
        }
    }

    @Override
    public void requestForNextStep(GameSituation gameSituation) {
        restTemplate.postForObject(BOT_ADD_URL, gameSituation, Object.class);
    }

    @Override
    public void removeBenben(long id) {
        users.remove(id);
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

    /**
     * 检查操作合法性并控制蛇移动
     */
    private void setNextStep(Player player, int direction, boolean isPlayer) {
        if (player.getBotCode() != null || isPlayer) {
            player.setNextStep(direction);
        }
    }
}
