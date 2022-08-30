package com.kob.backend.botrunningsystem.service;

import com.kob.backend.botrunningsystem.service.util.BotRunningQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tfs
 * @date 2022-08-29
 * @description
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {

    @Autowired
    private BotRunningQueue botRunningQueue;

    @Override
    public String addBot(int userId, String botCode, String input) {
        System.out.println("add bot: " + userId + " " + input);
        botRunningQueue.add(userId, botCode, input);
        return "add bot success";
    }
}
