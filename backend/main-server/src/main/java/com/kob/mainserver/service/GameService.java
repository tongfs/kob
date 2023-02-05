package com.kob.mainserver.service;

import com.kob.common.model.dto.GameSituation;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.thread.Game;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
public interface GameService {

    /**
     * 开始匹配
     */
    void startMatching(User user, long botId);

    /**
     * 取消匹配
     */
    void stopMatching(User user);

    /**
     * 保存游戏结果
     */
    void saveResult(Game game);

    /**
     * 蛇移动
     */
    void setNextStep(long userId, int direction, boolean isPlayer);

    /**
     * 开始游戏
     */
    void startGame(long playerId1, long botId1, long playerId2, long botId2);

    /**
     * bot控制蛇移动
     */
    void setNextStepByBot(long userId, int direction);

    /**
     * 发送请求获取bot下一步操作
     */
    void requestForNextStep(GameSituation gameSituation);
}
