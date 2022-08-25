package com.kob.backend.matchsystem.service;

/**
 * @author tfs
 * @date 2022-08-25
 * @description
 */
public interface MatchService {
    public String match(Integer userId, Integer rating);

    public String remove(Integer userId);
}
