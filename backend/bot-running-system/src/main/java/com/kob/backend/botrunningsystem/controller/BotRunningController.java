package com.kob.backend.botrunningsystem.controller;

import com.kob.backend.botrunningsystem.service.BotRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-29
 * @description
 */
@RestController
@RequestMapping("/pk/bot")
public class BotRunningController {

    @Autowired
    private BotRunningService botRunningService;

    @PostMapping("/add")
    public String addBot(@RequestParam Map<String, String> map) {
        int userId = new Integer(map.get("userId"));
        String botCode = map.get("botCode");
        String input = map.get("input");
        return botRunningService.addBot(userId, botCode, input);
    }
}
