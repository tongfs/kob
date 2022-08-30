package com.kob.backend.botrunningsystem.service.util;

import com.kob.backend.botrunningsystem.util.RunningInterface;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author tfs
 * @date 2022-08-30
 * @description
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Consumer implements Runnable {
    private static final long WAITING_TIME = 2000;
    private static RestTemplate restTemplate;
    private static final String SET_NEXT_URL = "http://127.0.0.1:3000/pk/bot/next";

    private Bot bot;

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startConsume() {
        Thread consumerThread = new Thread(this);
        consumerThread.start();
        try {
            consumerThread.join(WAITING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumerThread.interrupt();
        }
    }

    @Override
    public void run() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String botCode = bot.getBotCode();
        int i = botCode.indexOf(" implements com.kob.backend.botrunningsystem.util.RunningInterface");
        botCode = botCode.substring(0, i) + uuid + botCode.substring(i);

        RunningInterface runningInterface = Reflect
                .compile("com.kob.backend.botrunningsystem.util.RunningInterfaceImpl" + uuid, botCode)
                .create()
                .get();

        int nextStep = runningInterface.getNextStep(bot.getInput());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", bot.getUserId().toString());
        map.add("direction", Integer.toString(nextStep));
        restTemplate.postForObject(SET_NEXT_URL, map, String.class);
    }
}
