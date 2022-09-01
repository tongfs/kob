package com.kob.backend.botrunningsystem.service.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

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
        int i = botCode.indexOf(" implements java.util.function.Supplier<Integer>");
        botCode = botCode.substring(0, i) + uuid + botCode.substring(i);

        Supplier<Integer> runningInterface = Reflect
                .compile("com.kob.backend.botrunningsystem.util.RunningInterfaceImpl" + uuid, botCode)
                .create()
                .get();

        File file = new File("input.txt");
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(bot.getInput());
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int nextStep = runningInterface.get();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", bot.getUserId().toString());
        map.add("direction", Integer.toString(nextStep));
        restTemplate.postForObject(SET_NEXT_URL, map, String.class);
    }
}
