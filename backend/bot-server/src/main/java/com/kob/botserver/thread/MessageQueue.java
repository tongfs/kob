package com.kob.botserver.thread;

import java.util.LinkedList;
import java.util.Queue;

import com.kob.botserver.service.BotService;
import com.kob.common.model.dto.GameSituation;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
public class MessageQueue implements Runnable {

    public MessageQueue(BotService botService) {
        this.botService = botService;
        new Thread(this).start();
    }

    private static final long TASK_TIME_LIMIT = 2000;

    private final Queue<GameSituation> queue = new LinkedList<>();
    private final BotService botService;

    @Override
    public void run() {
        while (true) {
            GameSituation gameSituation = null;
            synchronized (this) {
                if (queue.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    gameSituation = queue.remove();
                }
            }
            if (gameSituation != null) {
                consume(gameSituation);
            }
        }
    }

    /**
     * 将新增的Bot加入队列
     */
    public void add(GameSituation gameSituation) {
        synchronized (this) {
            queue.offer(gameSituation);
            notify();
        }
    }

    /**
     * 创建线程执行Bot代码
     */
    private void consume(GameSituation gameSituation) {
        Consumer consumer = new Consumer(gameSituation, botService);
        Thread thread = new Thread(consumer);
        thread.start();
        try {
            thread.join(TASK_TIME_LIMIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            thread.interrupt();
        }
    }
}
