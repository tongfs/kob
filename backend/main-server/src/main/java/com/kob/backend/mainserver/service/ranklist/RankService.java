package com.kob.backend.mainserver.service.ranklist;

import com.alibaba.fastjson.JSONObject;

/**
 * @author tfs
 * @date 2022-08-31
 * @description
 */
public interface RankService {
    JSONObject getRankList(int page);
}
