package com.kob.botserver.service.impl;

import static com.kob.common.constant.Constants.GAME_NEXT_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kob.botserver.service.BotService;
import com.kob.botserver.thread.ThreadPoolExecutor;
import com.kob.botserver.thread.ThreadTask;
import com.kob.common.model.dto.GameSituation;
import com.kob.common.model.dto.NextStepDTO;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@Service
public class BotServiceImpl implements BotService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void add(GameSituation gameSituation) {
        threadPoolExecutor.execute(new ThreadTask(gameSituation, this));
    }

    @Override
    public void sendNextStep(Long userId, int nextStep) {
        NextStepDTO nextStepDTO = new NextStepDTO(userId, nextStep);
        restTemplate.postForObject(GAME_NEXT_URL, nextStepDTO, Object.class);
    }
}
