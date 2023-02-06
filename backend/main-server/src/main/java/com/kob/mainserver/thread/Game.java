package com.kob.mainserver.thread;

import static com.kob.common.constant.Constants.DIRECTION_UNDEFINED;
import static com.kob.common.enums.GameResult.LOSER_1;
import static com.kob.common.enums.GameResult.LOSER_2;
import static com.kob.common.enums.SocketResultType.GAME_RESULT;
import static com.kob.common.enums.SocketResultType.NEXT_STEP;

import com.kob.common.model.SocketResp;
import com.kob.common.model.dto.GameSituation;
import com.kob.mainserver.model.bean.Player;
import com.kob.mainserver.model.vo.GameNextStepVO;
import com.kob.mainserver.model.vo.GameResultVO;
import com.kob.mainserver.service.GameService;
import com.kob.mainserver.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Data
@AllArgsConstructor
public class Game implements Runnable {

    public Game(int[][] gameMap, Player player1, Player player2, GameService gameService, UserService userService) {
        this.gameMap = gameMap;
        this.player1 = player1;
        this.player2 = player2;
        this.gameService = gameService;
        this.userService = userService;
    }

    private static final long STEP_INTERVAL = 300;
    private static final long CHECK_MAX_TIME = 5000;
    private static final long CHECK_INTERVAL = 100;
    private static final long TIME_BEFORE_RUNNING = 2100;

    private final int[][] gameMap;
    private final Player player1;
    private final Player player2;

    private int round = 0;
    private int loser = 0;
    private GameService gameService;
    private UserService userService;

    @Override
    public void run() {
        // 发送匹配结果给前端后，前端2秒后才显示地图，后端这里等待前端一段时间
        try {
            Thread.sleep(TIME_BEFORE_RUNNING);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 进行游戏
        while (true) {
            // 前端蛇每走一格需要200ms，后端每次提供nextStep，需要大于200ms
            try {
                Thread.sleep(STEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (readyForNextStep()) {
                player1.getSteps().add(player1.getNextStep());
                player2.getSteps().add(player2.getNextStep());
                if (judge()) {
                    sendNextStep();
                } else {
                    sendGameResult();
                    break;
                }
            } else {
                if (player1.getNextStep() == DIRECTION_UNDEFINED) loser |= LOSER_1.getResultCode();
                if (player2.getNextStep() == DIRECTION_UNDEFINED) loser |= LOSER_2.getResultCode();
                player1.getSteps().add(player1.getNextStep());
                player2.getSteps().add(player2.getNextStep());
                sendGameResult();
                break;
            }
        }
    }


    /**
     * 当前回合蛇的身体是否要增长
     */
    public boolean isIncreasing() {
        if (round <= 5) return true;
        if (round <= 11) return (round & 1) == 1;
        return round % 3 == 2;
    }

    /**
     * 等待两名玩家的下一步操作
     */
    private boolean readyForNextStep() {

        // 检查是否是bot，是bot则发请求获取下一步操作
        if (player1.getBotCode() != null) {
            requestForNextStep(player1);
        }
        if (player2.getBotCode() != null) {
            requestForNextStep(player2);
        }

        // 间隔一段时间判断玩家是否都准备好了
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < CHECK_MAX_TIME) {
            if (player1.getNextStep() != DIRECTION_UNDEFINED && player2.getNextStep() != DIRECTION_UNDEFINED) {
                return true;
            }
            // 每100ms再检查一次
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 发送请求获取bot代码执行结果
     */
    private void requestForNextStep(Player player) {
        GameSituation gameSituation = new GameSituation(
                player.getId(), player.getBody().getLast(), player.getBotCode(), gameMap,
                round + 1, player1.getBody(), player2.getBody());
        gameService.requestForNextStep(gameSituation);
    }

    /**
     * 根据玩家每一步的合法性，判断本轮结果
     */
    private boolean judge() {
        round++;
        if (!isIncreasing()) {
            player1.getBody().poll();
            player2.getBody().poll();
        }

        // 判断下一步是否合法
        boolean isValid1 = player1.judge(player2.getBody(), this);
        boolean isValid2 = player2.judge(player1.getBody(), this);

        if (!isValid1) loser |= LOSER_1.getResultCode();
        if (!isValid2) loser |= LOSER_2.getResultCode();

        return isValid1 && isValid2;
    }

    /**
     * 向玩家发送下一步要往哪儿走
     */
    private void sendNextStep() {
        GameNextStepVO nextStepVO = new GameNextStepVO(player1.getNextStep(), player2.getNextStep());

        player1.setNextStep(DIRECTION_UNDEFINED);
        player2.setNextStep(DIRECTION_UNDEFINED);

        String resp = SocketResp.ok(NEXT_STEP, nextStepVO);
        batchSendMessage(resp);
    }

    /**
     * 向玩家广播消息
     */
    private void batchSendMessage(String message) {
        player1.getSocket().sendMessage(message);
        if (player2.getId() > 0) {
            player2.getSocket().sendMessage(message);
        }
    }

    /**
     * 向玩家发送比赛结果
     */
    private void sendGameResult() {
        // 先持久化
        saveGameResult();
        updateUserScore();

        GameResultVO gameResultVO = new GameResultVO(loser, player1.getNextStep(), player2.getNextStep());
        String resp = SocketResp.ok(GAME_RESULT, gameResultVO);
        batchSendMessage(resp);

        if (player2.getId() < 0) {
            gameService.removeBenben(player2.getId());
        }
    }

    /**
     * 持久化比赛结果
     */
    private void saveGameResult() {
        gameService.saveResult(this);
    }

    /**
     * 更新天梯分
     */
    private void updateUserScore() {
        userService.updateScore(loser, player1.getId(), player2.getId());
    }

}
