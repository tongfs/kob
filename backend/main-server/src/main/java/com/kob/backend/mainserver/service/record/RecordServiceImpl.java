package com.kob.backend.mainserver.service.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mainserver.mapper.RecordMapper;
import com.kob.backend.mainserver.mapper.UserMapper;
import com.kob.backend.mainserver.pojo.Record;
import com.kob.backend.mainserver.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tfs
 * @date 2022-08-31
 * @description
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getRecords(int page) {
        IPage<Record> iPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(iPage, queryWrapper).getRecords();

        JSONObject resp = new JSONObject();
        List<JSONObject> items = new ArrayList<>();

        for (Record record : records) {
            User user1 = userMapper.selectById(record.getAId());
            User user2 = userMapper.selectById(record.getBId());

            JSONObject item = new JSONObject();
            item.put("username1", user1.getUsername());
            item.put("profile1", user1.getProfile());
            item.put("username2", user2.getUsername());
            item.put("profile2", user2.getProfile());

            int identity = record.getLoserIdentity();
            String winner = "平局";
            if (identity == 1) {
                winner = "Player2: " + user2.getUsername();
            } else if (identity == 2) {
                winner = "Player1: " + user1.getUsername();
            }
            item.put("winner", winner);

            item.put("record", record);

            items.add(item);
        }

        resp.put("records", items);
        resp.put("total", recordMapper.selectCount(null));
        return resp;
    }
}
