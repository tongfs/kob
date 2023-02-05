package com.kob.mainserver.service.impl;

import static com.kob.common.constant.Constants.PAGE_SIZE;
import static com.kob.common.enums.ErrorCode.PASSWORD_BLANK;
import static com.kob.common.enums.ErrorCode.PASSWORD_INCONSISTENCY;
import static com.kob.common.enums.ErrorCode.PASSWORD_TOO_LONG;
import static com.kob.common.enums.ErrorCode.USERNAME_BLANK;
import static com.kob.common.enums.ErrorCode.USERNAME_TOO_LONG;
import static com.kob.common.enums.ErrorCode.USER_ALREADY_EXISTS;
import static com.kob.common.enums.GameResult.DRAW;
import static com.kob.common.enums.GameResult.LOSER_1;
import static com.kob.common.enums.GameResult.LOSER_2;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.common.exception.UserException;
import com.kob.mainserver.mapper.UserMapper;
import com.kob.mainserver.model.UserDetailsImpl;
import com.kob.mainserver.model.bean.UserConnection;
import com.kob.mainserver.model.bo.UserLoginBO;
import com.kob.mainserver.model.bo.UserRegisterBO;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.PageResultVO;
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

    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int PASSWORD_MAX_LENGTH = 20;
    private static final String[] DEFAULT_AVATARS = {
            "https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/FtMW3g7cYqUk_U537fKmqweXpoAf.jpg",
            "https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/c4c810b78db64a10b820985e85579e0d.jpeg",
            "https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/d28e4ef0792f23362e5ca00e5681182d.jpg",
            "https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/d50337e5759653aa27268847b1ca16e0.jpg",
            "https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/v2-c1b0fcddf7841b576549da07c41cbe8a_r.jpg",
            "https://youpeng-yp.oss-cn-chengdu.aliyuncs.com/avatar/2023-01-01/v2-f262a5a14d98ec31b59d1dc6893308e3_1440w.jpg",
    };
    private static final int DEFAULT_SCORE = 5000;
    private static final int WINNER_SCORE = 5;
    private static final int LOSER_SCORE = -3;
    private static final int DRAW_SCORE = -2;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Map<Long, UserConnection> users;

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
        user.setAvatar(randomAvatar());
        user.setScore(DEFAULT_SCORE);
        userMapper.insert(user);
    }

    /**
     * 获得一个随机头像
     */
    private String randomAvatar() {
        Random random = new Random();
        return DEFAULT_AVATARS[random.nextInt(DEFAULT_AVATARS.length)];
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
    public User selectById(long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public PageResultVO<UserInfoVO> getRankList(long page) {
        Page<User> userPage = new Page<>(page, PAGE_SIZE);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(User::getScore);
        Page<User> queryResult = userMapper.selectPage(userPage, queryWrapper);

        List<UserInfoVO> userInfoList = queryResult.getRecords().stream()
                .map(user -> {
                    UserInfoVO userInfoVO = new UserInfoVO();
                    BeanUtils.copyProperties(user, userInfoVO);
                    return userInfoVO;
                })
                .collect(Collectors.toList());

        PageResultVO<UserInfoVO> result = new PageResultVO<>();
        result.setCurrent(page);
        result.setTotal(queryResult.getTotal());
        result.setData(userInfoList);
        return result;
    }

    @Override
    public void updateScore(int loser, long id1, long id2) {
        User user1 = users.get(id1).getUser();
        User user2 = users.get(id2).getUser();

        if (loser == LOSER_1.getResultCode()) {
            user1.setScore(user1.getScore() + LOSER_SCORE);
            user2.setScore(user2.getScore() + WINNER_SCORE);
        } else if (loser == LOSER_2.getResultCode()) {
            user1.setScore(user1.getScore() + WINNER_SCORE);
            user2.setScore(user2.getScore() + LOSER_SCORE);
        } else if (loser == DRAW.getResultCode()) {
            user1.setScore(user1.getScore() + DRAW_SCORE);
            user2.setScore(user2.getScore() + DRAW_SCORE);
        }

        userMapper.updateById(user1);
        userMapper.updateById(user2);
    }
}
