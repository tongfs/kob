package com.kob.backend.mainserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.mainserver.pojo.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tfs
 * @date 2022-08-24
 * @description
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
