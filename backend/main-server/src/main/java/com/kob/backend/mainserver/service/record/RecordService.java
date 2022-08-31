package com.kob.backend.mainserver.service.record;

import com.alibaba.fastjson.JSONObject;

/**
 * @author tfs
 * @date 2022-08-31
 * @description
 */
public interface RecordService {
    JSONObject getRecords(int page);
}
