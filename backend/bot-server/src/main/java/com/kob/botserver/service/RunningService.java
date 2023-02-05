package com.kob.botserver.service;

import com.kob.common.model.dto.GameSituation;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public interface RunningService {

    /**
     * 通过比赛的局势给出下一步行动
     */
    int getNextStep(GameSituation gameSituation);
}
