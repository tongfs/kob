package com.kob.common.model;

import java.util.HashMap;
import java.util.Map;

import com.kob.common.enums.SocketResult;
import com.kob.common.util.GsonUtils;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/2
 */
public class SocketResp {
    public static String ok(SocketResult result, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", result.getCode());
        map.put("desc", result.getDesc());
        map.put("data", data);
        return GsonUtils.toJson(map);
    }
}
