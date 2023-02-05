package com.kob.mainserver.service;

import com.kob.mainserver.model.po.Record;
import com.kob.mainserver.model.vo.PageResultVO;
import com.kob.mainserver.model.vo.RecordVO;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public interface RecordService {

    /**
     * 插入一条记录
     */
    void insert(Record record);

    /**
     * 分页查询对战记录
     */
    PageResultVO<RecordVO> list(int page);
}
