package com.kob.mainserver.service;

import java.util.List;

import com.kob.mainserver.model.bo.BotAddBO;
import com.kob.mainserver.model.bo.BotUpdateBO;
import com.kob.mainserver.model.po.Bot;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
public interface BotService {
    /**
     * 新增一个bot
     */
    void add(BotAddBO botAddBO);

    void delete(Long botId);

    void update(BotUpdateBO botUpdateBO);

    List<Bot> getAllBot();

    Bot selectUserBotById(long botId, long userId);
}
