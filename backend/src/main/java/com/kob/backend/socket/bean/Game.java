package com.kob.backend.socket.bean;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.socket.WebSocketServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Random;

/**
 * @author tfs
 * @date 2022-08-23
 * @description 一局游戏里应该存的信息：
 * 1. 游戏地图
 * 2. 两名玩家
 */
@Getter
public class Game implements Runnable {
    private static final int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private static final int ROWS = 13;
    private static final int COLS = 14;
    private static final int BLOCK_COUNT = 20;

    private static final int LOSER_1 = 1, LOSER_2 = 2, DRAW = 3;

    private final int[][] gameMap;
    private final Player player1, player2;
    private Integer loser = 0;

    public Game(WebSocketServer socket1, WebSocketServer socket2) {
        // 构造地图
        gameMap = new int[ROWS][COLS];
        while (!createWalls(gameMap)) ;

        // 引入玩家
        player1 = new Player(socket1, ROWS - 2, 1);
        player2 = new Player(socket2,1, COLS - 2);

        // 发送匹配结果
        sendMatchResult(socket1, socket2.getUser());
        sendMatchResult(socket2, socket1.getUser());

        // 开启线程
        new Thread(this).start();
    }

    @Override
    public void run() {
        // 进行游戏
        while (true) {
            if (nextStep()) {
                if (judge()) {
                    sendNextStep();
                } else {
                    sendResult();
                    break;
                }
            } else {
                if (player1.getNextStep() == null) loser |= LOSER_1;
                if (player2.getNextStep() == null) loser |= LOSER_2;
                sendResult();
                break;
            }
        }
    }

    /**
     * 生成地图
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
        for (int i = 0; i < BLOCK_COUNT / 2; i++) {
            while (true) {
                int r = random.nextInt(ROWS), c = random.nextInt(COLS);
                if (g[r][c] == 1) {
                    continue;
                }
                if (r == ROWS - 2 && c == 1 || r == 1 && c == COLS - 2) {
                    continue;
                }
                g[r][c] = g[ROWS - r - 1][COLS - c - 1] = 1;
                break;
            }
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
     * 等待两名玩家的下一步操作
     */
    private boolean nextStep() {
        // 前端蛇每走一格需要200ms，后端每次提供nextStep，需要大于200ms
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 间隔一段时间判断玩家是否都准备好了
//        for (int i = 0; i < 50; i++) {
        for (int i = 0; i < 50000; i++) {
            try {
                Thread.sleep(100);
                if (player1.getNextStep() != null && player2.getNextStep() != null) {
                    player1.getSteps().add(player1.getNextStep());
                    player2.getSteps().add(player2.getNextStep());
                    return true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 向玩家发送比赛结果
     */
    private void sendResult() {
        // 在这之前先保存游戏结果
        saveResult();

        JSONObject resp = new JSONObject();
        setEvent(resp, "result");
        resp.put("loser", loser);
        batchSendMessage(resp.toJSONString());
    }

    /**
     * 根据玩家每一步的合法性，判断本轮结果
     */
    private boolean judge() {
        List<Player.Cell> body1 = player1.getBody();
        List<Player.Cell> body2 = player2.getBody();

        boolean valid1 = isValid(body1, body2);
        boolean valid2 = isValid(body2, body1);

        if (!valid1) loser |= LOSER_1;
        if (!valid2) loser |= LOSER_2;

        return valid1 && valid2;
    }

    /**
     * 向玩家发送下一步要往哪儿走
     */
    private void sendNextStep() {
        JSONObject resp = new JSONObject();
        setEvent(resp, "move");

        resp.put("direction", new int[]{player1.getNextStep(), player2.getNextStep()});
        player1.setNextStep(null);
        player2.setNextStep(null);

        batchSendMessage(resp.toJSONString());
    }

    /**
     * 向两名玩家发送消息
     */
    private void batchSendMessage(String message) {
        player1.getSocket().sendMessage(message);
        player2.getSocket().sendMessage(message);
    }

    /**
     * 判断玩家下一步操作是否合法
     */
    private boolean isValid(List<Player.Cell> body1, List<Player.Cell> body2) {
        int n = body1.size();
        Player.Cell head = body1.get(n - 1);

        // 判断是否撞到障碍物
        if (gameMap[head.x][head.y] == 1) {
            return false;
        }

        // 判断是否碰到蛇身
        for (int i = 0; i < n - 1; i++) {
            if (isOverlapped(head, body1.get(i)) || isOverlapped(head, body2.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断单元格是否重复
     */
    private boolean isOverlapped(Player.Cell a, Player.Cell b) {
        return a.x == b.x && a.y == b.y;
    }

    /**
     * 返回匹配结果
     */
    private void sendMatchResult(WebSocketServer socket, User opponent) {
        JSONObject resp = new JSONObject();
        setEvent(resp, "match_result");

        // 要返回的结果有 1. 对手的昵称和头像；2，玩家在地图中的身份；3.地图
        resp.put("opponent", new Opponent(opponent.getUsername(), opponent.getProfile()));
        resp.put("identity", socket == player1.getSocket() ? 1 : 2);
        resp.put("gameMap", gameMap);

        socket.sendMessage(resp.toJSONString());
    }

    private void setEvent(JSONObject resp, String event) {
        resp.put("event", event);
    }

    /**
     * 将结果保存到数据库中
     */
    private void saveResult() {
        Record record = new Record(this);
        WebSocketServer.getRecordMapper().insert(record);
    }

    public String map2String() {
        StringBuilder sb = new StringBuilder();
        for (int[] rows :gameMap) {
            for (int cell : rows) {
                sb.append(cell);
            }
        }
        return sb.toString();
    }

    @Data
    @AllArgsConstructor
    private static class Opponent {
        String username;
        String profile;
    }
}
