package com.kob.botserver.thread;

import java.util.UUID;

import org.joor.Reflect;

import com.kob.botserver.service.BotService;
import com.kob.botserver.service.RunningService;
import com.kob.common.model.dto.GameSituation;

import lombok.AllArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@AllArgsConstructor
public class Consumer implements Runnable {

    private final GameSituation gameSituation;
    private final BotService botService;

    @Override
    public void run() {
        try {
            // TODO 希望写成LeetCode的形式
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String code = gameSituation.getBotCode();
            int i = code.indexOf(" implements com.kob.botserver.service.RunningService");
            code = code.substring(0, i) + uuid + code.substring(i);

            RunningService runningService = Reflect
                    .compile("com.kob.botserver.service.impl.RunningServiceImpl" + uuid, code)
                    .create()
                    .get();

            int nextStep = runningService.getNextStep(gameSituation);
            botService.sendNextStep(gameSituation.getUserId(), nextStep);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
