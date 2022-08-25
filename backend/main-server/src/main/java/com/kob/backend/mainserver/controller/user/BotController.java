package com.kob.backend.mainserver.controller.user;

import com.kob.backend.mainserver.pojo.Bot;
import com.kob.backend.mainserver.service.user.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-21
 * @description
 */

@RestController
@RequestMapping("/user/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping("/add")
    public Map<String, String> add(@RequestParam Map<String, String> map) {
        System.out.println(map);
        return botService.add(map);
    }

    @PostMapping("/remove")
    public Map<String, String> remove(@RequestParam Integer botId) {
        return botService.remove(botId);
    }

    @PostMapping("/update")
    public Map<String, String> update(@RequestParam Map<String, String> map) {
        return botService.update(map);
    }

    @GetMapping("/all")
    public List<Bot> all() {
        return botService.all();
    }
}
