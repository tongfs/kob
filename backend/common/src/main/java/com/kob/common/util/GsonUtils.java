package com.kob.common.util;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
public class GsonUtils {
    private static final Gson gson;

    static {
        gson = new Gson();
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String s, Class<T> cls) {
        return gson.fromJson(s, cls);
    }

    public static <T> List<T> fromJsonToList(String s, Class<T> cls) {
        return gson.fromJson(s, new TypeToken<List<T>>() {
        }.getType());
    }
}
