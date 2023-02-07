package com.kob.mainserver.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.common.constant.Constants;
import com.kob.common.enums.GameResult;
import com.kob.common.util.GsonUtils;
import com.kob.mainserver.mapper.RecordMapper;
import com.kob.mainserver.model.po.Record;
import com.kob.mainserver.model.po.User;
import com.kob.mainserver.model.vo.PageResultVO;
import com.kob.mainserver.model.vo.RecordVO;
import com.kob.mainserver.service.RecordService;
import com.kob.mainserver.service.UserService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserService userService;

    @Override
    public void insert(Record record) {
        recordMapper.insert(record);
    }

    @Override
    public PageResultVO<RecordVO> list(int page) {
        Page<Record> recordPage = new Page<>(page, Constants.PAGE_SIZE);
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Record::getId);
        Page<Record> queryResult = recordMapper.selectPage(recordPage, queryWrapper);

        List<RecordVO> recordList = queryResult.getRecords().stream()
                .map(record -> {
                    User user1 = userService.selectById(record.getUserId1());
                    User user2 = userService.selectById(record.getUserId2());
                    return RecordVO.builder()
                            .id(record.getId())
                            .userId1(user1.getId())
                            .username1(decorateUsername(record.getLoserIdentity(), user1.getUsername(), 1))
                            .avatar1(user1.getAvatar())
                            .userId2(user2.getId())
                            .username2(decorateUsername(record.getLoserIdentity(), user2.getUsername(), 2))
                            .avatar2(user2.getAvatar())
                            .steps1(GsonUtils.fromJson(record.getSteps1(), int[].class))
                            .steps2(GsonUtils.fromJson(record.getSteps2(), int[].class))
                            .loserIdentity(record.getLoserIdentity())
                            .gameMap(GsonUtils.fromJson(record.getMap(), int[][].class))
                            .originalScore1(record.getOriginalScore1())
                            .getScore1(record.getGetScore1())
                            .originalScore2(record.getOriginalScore2())
                            .getScore2(record.getGetScore2())
                            .createTime(record.getCreateTime())
                            .build();
                })
                .collect(Collectors.toList());

        PageResultVO<RecordVO> result = new PageResultVO<>();
        result.setCurrent(page);
        result.setTotal(queryResult.getTotal());
        result.setData(recordList);

        return result;
    }

    private String decorateUsername(int loser, String username, int identity) {
        if (loser == GameResult.DRAW.getResultCode()) {
            return username + "\uD83E\uDD1D";
        }
        if (loser == identity) {
            return username + "\uD83D\uDC94";
        }
        return username + "\uD83C\uDFC6";
    }
}
