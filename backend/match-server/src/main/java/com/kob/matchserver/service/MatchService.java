package com.kob.matchserver.service;

import com.kob.common.model.dto.PlayerDTO;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
public interface MatchService {
    void add(PlayerDTO playerDTO);

    void remove(long userId);

}
