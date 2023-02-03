package com.kob.mainserver.model.bean;

import static com.kob.common.enums.GameResult.LOSER_1;
import static com.kob.common.enums.GameResult.LOSER_2;
import static com.kob.common.enums.SocketResultType.GAME_RESULT;
import static com.kob.common.enums.SocketResultType.NEXT_STEP;

import java.util.List;

import com.kob.common.model.SocketResp;
import com.kob.mainserver.model.vo.GameNextStepVO;
import com.kob.mainserver.model.vo.GameResultVO;
import com.kob.mainserver.service.GameService;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
@AllArgsConstructor
public class Game implements Runnable {

    public Game(int[][] gameMap, Player player1, Player player2, GameService gameService) {
        this.gameMap = gameMap;
        this.player1 = player1;
        this.player2 = player2;
        this.gameService = gameService;
    }

    private static final int STEP_INTERVAL = 300;
    private static final int CHECK_MAX_TIME = 5000;
    private static final int CHECK_INTERVAL = 100;

    private final int[][] gameMap;
    private final Player player1;
    private final Player player2;

    private Integer loser = 0;
    private GameService gameService;

    @Override
    public void run() {
        // 进行游戏
        while (true) {
            if (nextStep()) {
                if (judge()) {
                    sendNextStep();
                } else {
                    sendGameResult();
                    break;
                }
            } else {
                if (player1.getNextStep() == null) loser |= LOSER_1.getResultCode();
                if (player2.getNextStep() == null) loser |= LOSER_2.getResultCode();
                sendGameResult();
                break;
            }
        }
    }

    /**
     * 等待两名玩家的下一步操作
     */
    private boolean nextStep() {
        // 前端蛇每走一格需要200ms，后端每次提供nextStep，需要大于200ms
        try {
            Thread.sleep(STEP_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 间隔一段时间判断玩家是否都准备好了
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < CHECK_MAX_TIME) {
            try {
                // 每100ms检查一次是否准备好
                Thread.sleep(CHECK_INTERVAL);
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
     * 根据玩家每一步的合法性，判断本轮结果
     */
    private boolean judge() {
        List<Player.Cell> bodyA = player1.getBody();
        List<Player.Cell> bodyB = player2.getBody();

        boolean validA = checkStepValid(bodyA, bodyB);
        boolean validB = checkStepValid(bodyB, bodyA);

        if (!validA) loser |= LOSER_1.getResultCode();
        if (!validB) loser |= LOSER_2.getResultCode();

        return validA && validB;
    }

    /**
     * 判断玩家下一步操作是否合法
     */
    private boolean checkStepValid(List<Player.Cell> bodyA, List<Player.Cell> bodyB) {
        int n = bodyA.size();
        Player.Cell head = bodyA.get(n - 1);

        // 判断是否撞到障碍物
        if (gameMap[head.x][head.y] == 1) {
            return false;
        }

        // 判断是否碰到蛇身
        for (int i = 0; i < n - 1; i++) {
            if (isOverlapped(head, bodyA.get(i)) || isOverlapped(head, bodyB.get(i))) {
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
     * 向玩家发送下一步要往哪儿走
     */
    private void sendNextStep() {
        GameNextStepVO nextStepVO = new GameNextStepVO(player1.getNextStep(), player2.getNextStep());

        player1.setNextStep(null);
        player2.setNextStep(null);

        String resp = SocketResp.ok(NEXT_STEP, nextStepVO);
        batchSendMessage(resp);
    }

    /**
     * 向玩家广播消息
     */
    private void batchSendMessage(String message) {
        player1.getSocket().sendMessage(message);
        player2.getSocket().sendMessage(message);
    }

    /**
     * 向玩家发送比赛结果
     */
    private void sendGameResult() {
        // 在这之前先保存游戏结果
        saveResult();

        GameResultVO gameResultVO = new GameResultVO(loser);
        String resp = SocketResp.ok(GAME_RESULT, gameResultVO);
        batchSendMessage(resp);
    }

    private void saveResult() {
        gameService.saveResult(this);
    }
}
