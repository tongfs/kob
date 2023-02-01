package com.kob.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "success"),
    DEFAULT_EXCEPTION(100, "未知异常"),

    // 200: 用户相关
    AUTHENTICATION_EXCEPTION(201, "用户名或密码错误"),
    USERNAME_BLANK(202, "用户名为空"),
    PASSWORD_BLANK(203, "密码为空"),
    USERNAME_TOO_LONG(204, "用户名过长"),
    PASSWORD_TOO_LONG(205, "密码过长"),
    USER_ALREADY_EXISTS(206, "用户已存在"),
    PASSWORD_INCONSISTENCY(207, "PASSWORD_INCONSISTENCY_EXCEPTION"),
    ;

    private final int code;
    private final String msg;
    }
