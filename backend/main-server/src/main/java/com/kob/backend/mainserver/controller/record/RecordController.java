package com.kob.backend.mainserver.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mainserver.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-31
 * @description
 */
@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/list")
    JSONObject getList(@RequestParam Map<String, String> map) {
        Integer page = new Integer(map.get("page"));
        return recordService.getRecords(page);
    }
}
