package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tfs
 * @date 2022-08-20
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String profile;
}
