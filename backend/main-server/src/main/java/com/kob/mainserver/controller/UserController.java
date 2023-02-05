package com.kob.mainserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kob.common.exception.UserException;
import com.kob.common.model.R;
import com.kob.mainserver.model.bo.UserLoginBO;
import com.kob.mainserver.model.bo.UserRegisterBO;
import com.kob.mainserver.model.vo.PageResultVO;
import com.kob.mainserver.model.vo.UserInfoVO;
import com.kob.mainserver.model.vo.UserLoginVO;
import com.kob.mainserver.service.UserService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody UserLoginBO userLoginBO) {
        UserLoginVO userLoginVO = userService.login(userLoginBO);
        return R.ok(userLoginVO);
    }

    @PostMapping("/register")
    public R register(@RequestBody UserRegisterBO userRegisterBO) {
        try {
            userService.register(userRegisterBO);
            return R.ok();
        } catch (UserException e) {
            return R.error(e);
        }
    }

    @GetMapping("/info")
    public R getInfo() {
        UserInfoVO userInfoVO = userService.getInfo();
        return R.ok(userInfoVO);
    }

    @GetMapping("/rank/{page}")
    public R getRankList(@PathVariable long page) {
        PageResultVO<UserInfoVO> rankList = userService.getRankList(page);
        return R.ok(rankList);
    }
}
