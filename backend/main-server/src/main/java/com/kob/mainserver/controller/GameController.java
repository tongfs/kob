package com.kob.mainserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.common.model.R;
import com.kob.common.model.dto.MatchResultDTO;
import com.kob.common.model.dto.NextStepDTO;
import com.kob.mainserver.service.GameService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public R start(@RequestBody MatchResultDTO matchResultDTO) {
        gameService.startGame(
                matchResultDTO.getPlayerId1(), matchResultDTO.getBotId1(),
                matchResultDTO.getPlayerId2(), matchResultDTO.getBotId2());
        return R.ok();
    }

    @PostMapping("/next")
    public R next(@RequestBody NextStepDTO nextStepDTO) {
        gameService.setNextStepByBot(nextStepDTO.getUserId(), nextStepDTO.getDirection());
        return R.ok();
    }
}
