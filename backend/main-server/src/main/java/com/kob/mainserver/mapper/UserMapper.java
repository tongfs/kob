package com.kob.mainserver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.mainserver.model.po.User;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
