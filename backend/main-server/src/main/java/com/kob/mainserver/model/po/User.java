package com.kob.mainserver.model.po;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private String avatar;

    private Integer score;
}
