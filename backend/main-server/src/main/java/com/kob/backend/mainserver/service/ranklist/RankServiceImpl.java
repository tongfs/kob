package com.kob.backend.mainserver.service.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mainserver.mapper.UserMapper;
import com.kob.backend.mainserver.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tfs
 * @date 2022-08-31
 * @description
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getRankList(int page) {
        Page<User> userPage = new Page<>(page, 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");

        List<User> users = userMapper.selectPage(userPage, queryWrapper).getRecords();
        for (User user : users) {
            user.setPassword(null);
        }

        JSONObject resp = new JSONObject();
        resp.put("total", userMapper.selectCount(null));
        resp.put("users", users);

        return resp;
    }
}
