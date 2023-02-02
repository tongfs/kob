package com.kob.mainserver.model.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Data
public class Bot {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String content;

    private Integer rating;

    private Date createTime;

    private Date updateTime;
}
