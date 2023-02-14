package com.kob.botserver.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

/**
 * 自己实现的线程池
 *
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/8
 */
public class ThreadPoolExecutor implements Executor {

    private final int corePoolSize;
    private final int maximumPoolSize;
    private final BlockingQueue<Runnable> workQueue;

    private int threadCount = 0;

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
    }

    @Override
    public synchronized void execute(@Nonnull Runnable command) {
        if (threadCount < corePoolSize) {
            addWorker(command);
            return;
        }
        if (!workQueue.offer(command)) {
            if (!addWorker(command)) {
                reject();
            }
        }
    }

    /**
     * 拒绝策略
     */
    private void reject() {
        throw new RuntimeException("Error! ctl.count: " + threadCount + " workQueue.size: " + workQueue.size());
    }

    /**
     * 尝试新增线程去处理任务
     */
    private boolean addWorker(Runnable firstTask) {
        if (threadCount >= maximumPoolSize) return false;

        Worker worker = new Worker(firstTask);
        worker.thread.start();
        threadCount++;
        return true;
    }

    /**
     * 真正执行任务的线程
     */
    private final class Worker implements Runnable {

        final Thread thread;
        Runnable firstTask;

        public Worker(Runnable firstTask) {
            this.thread = new Thread(this);
            this.firstTask = firstTask;
        }

        @Override
        public void run() {
            Runnable task = firstTask;

            while (true) {
                task.run();
                synchronized (ThreadPoolExecutor.this) {
                    if (threadCount > corePoolSize) {
                        threadCount--;
                        return;
                    }
                }
                task = getTask();
            }
        }

        private Runnable getTask() {
            try {
                return workQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
