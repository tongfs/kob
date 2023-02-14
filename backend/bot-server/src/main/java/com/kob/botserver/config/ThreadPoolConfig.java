package com.kob.botserver.config;

import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kob.botserver.thread.ThreadPoolExecutor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/8
 */
@Configuration
public class ThreadPoolConfig {

    private static final int CORE_THREAD_SIZE = 10;
    private static final int MAX_THREAD_SIZE = 30;
    private static final int BLOCKING_QUEUE_SIZE = 20;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE);
        return new ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, workQueue);
    }
}
