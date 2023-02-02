package com.kob.mainserver.model.po;

import java.util.Date;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
@Data
public class Bot {

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String content;

    private Integer rating;

    private Date createTime;

    private Date updateTime;
}
