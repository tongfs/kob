package com.kob.mainserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kob.mainserver.mapper.RecordMapper;
import com.kob.mainserver.model.po.Record;
import com.kob.mainserver.service.RecordService;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public void insert(Record record) {
        recordMapper.insert(record);
    }
}
