package com.kob.backend.matchsystem.controller;

import com.kob.backend.matchsystem.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-25
 * @description
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/add")
    public String match(@RequestParam Map<String, String> map) {
        int userId = new Integer(map.get("userId"));
        int rating = new Integer(map.get("rating"));
        return matchService.match(userId, rating);
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Map<String, String> map) {
        int userId = new Integer(map.get("userId"));
        return matchService.remove(userId);
    }
}
