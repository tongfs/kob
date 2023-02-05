package com.kob.matchserver.service;

import com.kob.common.model.dto.PlayerDTO;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
public interface MatchService {

    /**
     * 将玩家加入匹配池
     */
    void add(PlayerDTO playerDTO);

    /**
     * 将玩家移出匹配池
     */
    void remove(long userId);

    /**
     * 返回匹配结果
     */
    void sendMatchResult(PlayerDTO player1, PlayerDTO player2);
}
