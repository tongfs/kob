package com.kob.backend.matchsystem.service;

import com.kob.backend.matchsystem.service.util.MatchPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tfs
 * @date 2022-08-25
 * @description
 */
@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchPool matchPool;

    @Override
    public String match(int userId, int rating, int botId) {
        System.out.println("add player: " + userId + " " + rating + " " + botId);
        matchPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String remove(int userId) {
        System.out.println("remove player: " + userId );
        matchPool.removePlayer(userId);
        return "remove player success";
    }
}
