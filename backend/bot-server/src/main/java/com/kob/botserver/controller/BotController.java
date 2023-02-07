package com.kob.botserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.botserver.service.BotService;
import com.kob.common.model.R;
import com.kob.common.model.dto.GameSituation;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping("/add")
    public R add(@RequestBody GameSituation gameSituation) {
        botService.add(gameSituation);
        return R.ok();
    }
}
