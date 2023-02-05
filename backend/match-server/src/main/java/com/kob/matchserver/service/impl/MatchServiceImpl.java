package com.kob.matchserver.service.impl;

import static com.kob.common.constant.Constants.GAME_START_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kob.common.model.dto.MatchResultDTO;
import com.kob.common.model.dto.PlayerDTO;
import com.kob.matchserver.service.MatchService;
import com.kob.matchserver.thread.MatchPool;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/3
 */
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchPool matchPool = new MatchPool(this);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void add(PlayerDTO player) {
        matchPool.add(player);
    }

    @Override
    public void remove(long userId) {
        matchPool.remove(userId);
    }

    @Override
    public void sendMatchResult(PlayerDTO player1, PlayerDTO player2) {
        MatchResultDTO matchResultDTO = new MatchResultDTO(
                player1.getUserId(), player1.getBotId(),
                player2.getUserId(), player2.getBotId()
        );
        restTemplate.postForObject(GAME_START_URL, matchResultDTO, Object.class);
    }
}
