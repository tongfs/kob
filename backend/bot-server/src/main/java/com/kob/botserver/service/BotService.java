package com.kob.botserver.service;

import com.kob.common.model.dto.GameSituation;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public interface BotService {

    /**
     * 将bot代码加入消息队列
     */
    void add(GameSituation gameSituation);

    /**
     * 发送bot代码执行结果
     */
    void sendNextStep(Long userId, int nextStep);
}
