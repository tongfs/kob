package com.kob.matchserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.common.model.R;
import com.kob.common.model.dto.PlayerDTO;
import com.kob.common.model.dto.MatchRemoveDTO;
import com.kob.matchserver.service.MatchService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/add")
    public R match(@RequestBody PlayerDTO playerDTO) {
        matchService.add(playerDTO);
        return R.ok();
    }

    @PostMapping("/remove")
    public R remove(@RequestBody MatchRemoveDTO matchRemoveDTO) {
        matchService.remove(matchRemoveDTO.getUserId());
        return R.ok();
    }
}
