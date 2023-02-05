package com.kob.mainserver.service;

import com.kob.mainserver.model.po.Record;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public interface RecordService {

    /**
     * 插入一条记录
     */
    void insert(Record record);
}
