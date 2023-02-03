package com.kob.mainserver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.mainserver.model.po.Record;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
