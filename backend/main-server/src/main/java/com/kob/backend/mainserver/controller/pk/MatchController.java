package com.kob.backend.mainserver.controller.pk;

import com.kob.backend.mainserver.service.pk.MatchService;
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
@RequestMapping("/pk/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/start")
    public void startGame(@RequestParam Map<String, String> map) {
        Integer id1 = new Integer(map.get("id1"));
        Integer botId1 = new Integer(map.get("botId1"));
        Integer id2 = new Integer(map.get("id2"));
        Integer botId2 = new Integer(map.get("botId2"));
        matchService.startGame(id1, botId1, id2, botId2);
    }
}
