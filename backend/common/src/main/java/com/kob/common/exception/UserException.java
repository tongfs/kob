package com.kob.common.exception;

import com.kob.common.enums.ErrorCode;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
public class UserException extends BaseCustomException {
    public UserException(ErrorCode e) {
        super(e);
    }
}
