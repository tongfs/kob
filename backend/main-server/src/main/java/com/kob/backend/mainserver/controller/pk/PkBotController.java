package com.kob.backend.mainserver.controller.pk;

import com.kob.backend.mainserver.service.pk.PkBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-30
 * @description
 */
@RestController
@RequestMapping("pk/bot")
public class PkBotController {

    @Autowired
    private PkBotService pkBotService;

    @PostMapping("/next")
    public String getNextStep(@RequestParam Map<String, String> map) {
        int userId = new Integer(map.get("userId"));
        int direction = new Integer(map.get("direction"));
        System.out.println(map);
        return pkBotService.setNextStep(userId, direction);
    }
}
