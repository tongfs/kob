package com.kob.backend.matchsystem.service;

/**
 * @author tfs
 * @date 2022-08-25
 * @description
 */
public interface MatchService {
    public String match(int userId, int rating, int botId);

    public String remove(int userId);
}
