package com.kob.backend.mainserver.controller.user;

import com.kob.backend.mainserver.pojo.Bot;
import com.kob.backend.mainserver.service.user.UserBotService;
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
@RequestMapping("/api/user/bot")
public class UserBotController {

    @Autowired
    private UserBotService userBotService;

    @PostMapping("/add")
    public Map<String, String> add(@RequestParam Map<String, String> map) {
        System.out.println(map);
        return userBotService.add(map);
    }

    @PostMapping("/remove")
    public Map<String, String> remove(@RequestParam Integer botId) {
        return userBotService.remove(botId);
    }

    @PostMapping("/update")
    public Map<String, String> update(@RequestParam Map<String, String> map) {
        return userBotService.update(map);
    }

    @GetMapping("/all")
    public List<Bot> all() {
        return userBotService.all();
    }
}
