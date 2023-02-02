package com.kob.mainserver.exception;

import com.kob.common.enums.ErrorCode;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
public class BotException extends BaseCustomException{
    public BotException(ErrorCode e) {
        super(e);
    }
}
