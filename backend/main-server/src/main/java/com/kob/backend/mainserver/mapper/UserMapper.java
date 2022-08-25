package com.kob.backend.mainserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.mainserver.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tfs
 * @date 2022-08-20
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
