package com.kob.mainserver.service.impl;

import static com.kob.common.enums.ErrorCode.PASSWORD_BLANK;
import static com.kob.common.enums.ErrorCode.PASSWORD_INCONSISTENCY;
import static com.kob.common.enums.ErrorCode.PASSWORD_TOO_LONG;
import static com.kob.common.enums.ErrorCode.USERNAME_BLANK;
import static com.kob.common.enums.ErrorCode.USERNAME_TOO_LONG;
import static com.kob.common.enums.ErrorCode.USER_ALREADY_EXISTS;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kob.common.exception.UserException;
import com.kob.mainserver.mapper.UserMapper;
import com.kob.mainserver.model.UserDetailsImpl;
import com.kob.mainserver.model.bo.UserLoginBO;
import com.kob.mainserver.model.bo.UserRegisterBO;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.UserInfoVO;
import com.kob.mainserver.model.vo.UserLoginVO;
import com.kob.mainserver.service.UserService;
import com.kob.mainserver.util.AuthenticationUtils;
import com.kob.mainserver.util.JwtUtils;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Integer USERNAME_MAX_LENGTH = 20;
    private static final Integer PASSWORD_MAX_LENGTH = 20;
    private static final String DEFAULT_AVATAR =
            "https://pic4.zhimg.com/80/v2-f262a5a14d98ec31b59d1dc6893308e3_1440w.webp";
    private static final Integer DEFAULT_SCORE = 3000;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(UserRegisterBO userRegisterBO) {
        String username = userRegisterBO.getUsername();
        String password = userRegisterBO.getPassword();
        String confirmedPassword = userRegisterBO.getConfirmedPassword();

        if (StringUtils.isBlank(username)) {
            throw new UserException(USERNAME_BLANK);
        }
        if (StringUtils.isBlank(password) || StringUtils.isBlank(confirmedPassword)) {
            throw new UserException(PASSWORD_BLANK);
        }
        if (StringUtils.length(username) > USERNAME_MAX_LENGTH) {
            throw new UserException(USERNAME_TOO_LONG);
        }
        if (StringUtils.length(password) > PASSWORD_MAX_LENGTH) {
            throw new UserException(PASSWORD_TOO_LONG);
        }
        if (!StringUtils.equals(password, confirmedPassword)) {
            throw new UserException(PASSWORD_INCONSISTENCY);
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        boolean exists = userMapper.exists(queryWrapper);
        if (exists) {
            throw new UserException(USER_ALREADY_EXISTS);
        }

        String encodePassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setAvatar(DEFAULT_AVATAR);
        user.setScore(DEFAULT_SCORE);
        userMapper.insert(user);
    }

    @Override
    public UserLoginVO login(UserLoginBO userLoginBO) {
        String username = userLoginBO.getUsername();
        String password = userLoginBO.getPassword();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authentication);

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt = JwtUtils.createJWT((user.getId().toString()));
        return new UserLoginVO(jwt);
    }

    @Override
    public UserInfoVO getInfo() {
        User user = AuthenticationUtils.getUser();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    public User getUserById(long userId) {
        return userMapper.selectById(userId);
    }
}
