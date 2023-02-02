package com.kob.common.model;

import java.util.LinkedHashMap;

import com.kob.common.enums.ErrorCode;
import com.kob.common.exception.BaseCustomException;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/1
 */
public class R extends LinkedHashMap<String, Object> {

    public R() {
        put("code", ErrorCode.SUCCESS.getCode());
        put("message", ErrorCode.SUCCESS.getMsg());
    }

    /**
     * 用于无data的情况
     */
    public static R ok() {
        return new R();
    }

    /**
     * 用于data为一整个对象
     */
    public static R ok(Object data) {
        R r = new R();
        r.put("data", data);
        return r;
    }

    public static R error() {
        return error(ErrorCode.DEFAULT_EXCEPTION);
    }

    public static R error(String message) {
        return error(ErrorCode.DEFAULT_EXCEPTION.getCode(), message);
    }

    public static R error(int code, String message) {
        R r = new R();
        r.put("code", code);
        r.put("message", message);
        return r;
    }

    public static R error(ErrorCode e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("message", e.getMsg());
        return r;
    }

    public static R error(BaseCustomException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("message", e.getMessage());
        return r;
    }
}

