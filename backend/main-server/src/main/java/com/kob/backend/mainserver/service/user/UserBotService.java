package com.kob.backend.mainserver.service.user;

import com.kob.backend.mainserver.pojo.Bot;

import java.util.List;
import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-21
 * @description
 */
public interface UserBotService {
    Map<String, String> add(Map<String, String> map);

    Map<String, String> remove(int botId);

    Map<String, String> update(Map<String, String> map);

    List<Bot> all();
}
