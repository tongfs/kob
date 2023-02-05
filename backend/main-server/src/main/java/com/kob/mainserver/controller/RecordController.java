package com.kob.mainserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.common.model.R;
import com.kob.mainserver.model.vo.PageResultVO;
import com.kob.mainserver.model.vo.RecordVO;
import com.kob.mainserver.service.RecordService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/5
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/list/{page}")
    public R list(@PathVariable int page) {
        PageResultVO<RecordVO> recordList = recordService.list(page);
        return R.ok(recordList);
    }
}
