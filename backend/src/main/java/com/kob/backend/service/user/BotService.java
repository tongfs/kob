package com.kob.backend.service.user;

import com.kob.backend.pojo.Bot;

import java.util.List;
import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-21
 * @description
 */
public interface BotService {
    Map<String, String> add(Map<String, String> map);

    Map<String, String> remove(int botId);

    Map<String, String> update(Map<String, String> map);

    List<Bot> all();
}
