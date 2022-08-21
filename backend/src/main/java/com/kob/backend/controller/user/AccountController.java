package com.kob.backend.controller.user;

import com.kob.backend.service.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-20
 * @description
 */
@RestController
@RequestMapping("/user/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return accountService.getToken(username, password);
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return accountService.getInfo();
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        return accountService.register(username, password, confirmedPassword);
    }
}
