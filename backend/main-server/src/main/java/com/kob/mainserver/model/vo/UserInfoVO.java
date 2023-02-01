package com.kob.mainserver.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Data
public class UserInfoVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String avatar;

    private Integer score;
}
