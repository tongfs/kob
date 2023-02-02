package com.kob.common.enums;

import com.kob.common.constant.Constants;

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
    PASSWORD_INCONSISTENCY(207, "两次输入的密码不一致"),

    // 300: Bot相关
    BOT_TITLE_BLANK(301, "Bot名称为空"),
    BOT_TITLE_TOO_LONG(302, "Bot名称过长"),
    BOT_DESC_TO_LONG(303, "描述不超过" + Constants.BOT_DESC_MAX_LENGTH + "个字符"),
    BOT_CONTENT_BLANK(304, "Bot代码为空"),
    BOT_CONTENT_TO_LONG(305, "Bot代码过长"),
    BOT_NOT_EXIST(306, "Bot不存在或已被删除"),
    NO_BOT_PERMISSION(307,"没有操作该Bot的权限"),

    ;


    private final int code;
    private final String msg;
}
