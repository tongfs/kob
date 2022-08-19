package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-19
 * @description
 */
@RestController
@RequestMapping("/pk")
public class IndexController {
    @RequestMapping("/getInfo")
    public Map<String, String> getBotInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "rabbit");
        map.put("rating", "1000");
        return map;
    }
}
