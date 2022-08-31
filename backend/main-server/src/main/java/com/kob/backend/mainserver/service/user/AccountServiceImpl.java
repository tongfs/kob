package com.kob.backend.mainserver.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mainserver.mapper.UserMapper;
import com.kob.backend.mainserver.pojo.User;
import com.kob.backend.mainserver.util.AuthenticationUtil;
import com.kob.backend.mainserver.util.JwtUtil;
import com.kob.backend.mainserver.service.util.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-20
 * @description
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> getInfo() {
        User user = AuthenticationUtil.getLoginUser();

        Map<String, String> map = new HashMap<>();
        map.put("msg", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("profile", user.getProfile());

        return map;
    }

    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authentication);

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt = JwtUtil.createJWT((user.getId().toString()));

        Map<String, String> map = new HashMap<>();
        map.put("msg", "success");
        map.put("token", jwt);

        return map;
    }

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();

        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(username.trim())) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (ObjectUtils.isEmpty(password) || ObjectUtils.isEmpty(confirmedPassword)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if (username.trim().length() > 100) {
            map.put("msg", "用户名长度不能超过100");
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("msg", "密码长度不能超过100");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("msg", "两次输入的密码不一致");
            return map;
        }

        username = username.trim();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        boolean isExisted = userMapper.exists(queryWrapper);
        if (isExisted) {
            map.put("msg", "用户名已存在");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String profile = "https://pic3.zhimg.com/v2-c1b0fcddf7841b576549da07c41cbe8a_r.jpg";

        User user = new User(null, username, encodedPassword, profile, null);
        userMapper.insert(user);

        map.put("msg", "success");
        return map;
    }
}
