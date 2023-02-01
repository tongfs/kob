package com.kob.mainserver.exception;

import com.kob.common.enums.ErrorCode;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
public class UserLoginException extends RuntimeException {
    private final Integer code;

    public UserLoginException(ErrorCode e) {
        super(e.getMsg());
        code = e.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
