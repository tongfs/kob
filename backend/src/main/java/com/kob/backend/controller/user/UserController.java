package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tfs
 * @date 2022-08-20
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("/{id}")
    public List<User> getUser(@PathVariable int id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("id", 2)
                .le("id", 3);
        return userMapper.selectList(queryWrapper);
    }

    @GetMapping("/{username}/{password}")
    public String registry(
            @PathVariable String username,
            @PathVariable String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword);
        int cnt = userMapper.insert(user);
        if (cnt != 0) {
            return "Success";
        } else {
            return "Failed";
        }
    }
}
