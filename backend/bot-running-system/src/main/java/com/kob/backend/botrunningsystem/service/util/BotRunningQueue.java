package com.kob.backend.botrunningsystem.service.util;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author tfs
 * @date 2022-08-30
 * @description 消息队列，动态编译运行bot的代码
 */
@Component
public class BotRunningQueue implements Runnable {

    private final Queue<Bot> queue = new LinkedList<>();

    public BotRunningQueue() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            Bot bot = null;
            synchronized (this) {
                if (queue.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    bot = queue.remove();
                }
            }
            if (bot != null) {
                consume(bot);
            }
        }
    }

    /**
     * 处理取出的队头元素
     */
    private void consume(Bot bot) {
        Consumer consumer = new Consumer(bot);
        consumer.startConsume();
    }

    /**
     * 将新增的Bot加入队列
     */
    public void add(int userId, String botCode, String input) {
        Bot bot = new Bot(userId, botCode, input);
        synchronized (this) {
            queue.offer(bot);
            notify();
        }
    }
}
