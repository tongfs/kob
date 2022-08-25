package com.kob.backend.mainserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tfs
 * @date 2022-08-21
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String content;

    private Date createTime;

    private Date updateTime;
}
