package com.kob.mainserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.common.exception.BotException;
import com.kob.common.model.R;
import com.kob.mainserver.model.bo.BotAddBO;
import com.kob.mainserver.model.bo.BotRemoveBO;
import com.kob.mainserver.model.bo.BotUpdateBO;
import com.kob.mainserver.model.po.Bot;
import com.kob.mainserver.service.BotService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    private BotService botService;

    /**
     * 在个人空间中添加bot
     */
    @PostMapping("/add")
    public R addBot(@RequestBody BotAddBO botAddBO) {
        try {
            botService.add(botAddBO);
            return R.ok();
        } catch (BotException e) {
            return R.error(e);
        }
    }

    /**
     * 在个人空间中删除bot
     */
    @PostMapping("/remove")
    public R deleteBot(@RequestBody BotRemoveBO botRemoveBO) {
        try {
            botService.delete(botRemoveBO.getBotId());
            return R.ok();
        } catch (BotException e) {
            return R.error(e);
        } catch (Exception e) {
            return R.error();
        }
    }

    /**
     * 更新bot信息
     */
    @PostMapping("/update")
    public R updateBot(@RequestBody BotUpdateBO botUpdateBO) {
        try {
            botService.update(botUpdateBO);
            return R.ok();
        } catch (BotException e) {
            return R.error(e);
        }
    }

    /**
     * 查找用户所有bot
     */
    @GetMapping("/list")
    public R getAllBot() {
        List<Bot> bots = botService.getAllBot();
        return R.ok(bots);
    }
}
