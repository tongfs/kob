package com.kob.mainserver.model.bo;

import lombok.Data;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Data
public class UserRegisterBO {

    private String username;

    private String password;

    private String confirmedPassword;
}
