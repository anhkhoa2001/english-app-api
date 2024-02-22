package org.base.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public abstract class ThreadManager<T> {
    public final int BATCH_SIZE = 10;
    public final long WAIT_TIME_OUT = 1000; // 1 seconds
    public final long TIME_OUT = 2 * 1000; // 2 seconds
    private final BlockingQueue<T> sourceQueue = new LinkedBlockingQueue<>();

    protected ArrayList<T> items = new ArrayList<>(BATCH_SIZE);
    protected AtomicBoolean shouldWork = new AtomicBoolean(true);
    protected AtomicBoolean isRunning = new AtomicBoolean(true);
    private boolean listening = false;
    protected ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    private Thread mainThread;

    public ThreadManager() {
        taskExecutor.setCorePoolSize(5);//Số thread cho phép chạy cùng lúc
        taskExecutor.setQueueCapacity(200);//Số lệnh có thể chờ trong hàng đợi. Nếu =0 thì tất cả sẽ chạy cùng lúc không phụ thuộc CorePoolSize
        taskExecutor.setThreadNamePrefix(getName());
        taskExecutor.afterPropertiesSet();
        taskExecutor.setTaskDecorator(new MDCLogTaskDecorator());

        log.debug("Start task manager named: " + getName());
        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("Queued job manager " + getName() + " is running and watching for queue... ");
                isRunning.set(true);
                int recNum = 0;
                long lgnStart = System.currentTimeMillis();
                while (shouldWork.get()) {
                    try {
                        T item = sourceQueue.poll(WAIT_TIME_OUT, TimeUnit.MILLISECONDS);
                        if (item != null) {
                            items.add(item);
                            recNum++;
                        }

                        if (recNum >= BATCH_SIZE || timedOut(lgnStart)) {
                            if (items.size() > 0) {
                                log.info(String.format("Thread %s: %s submits %d item(s)",
                                        Thread.currentThread().getName(), getName(), items.size()));
                                doProcess(items);
                                items = new ArrayList<>(BATCH_SIZE);
                                lgnStart = System.currentTimeMillis();
                                recNum = 0;
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    isRunning.set(false);
                }
                log.info("Taskmanager " + getName() + " is stopped!!");
            }

            private boolean timedOut(Long startTime) {
                return System.currentTimeMillis() - startTime > TIME_OUT;
            }
        });

    }

    public abstract void doProcess(ArrayList<T> items);

    public abstract String getName();

    public synchronized void listen() {
        if (!listening) {
            mainThread.start();
            listening = true;
        }
    }

    public BlockingQueue<T> getSourceQueue() {
        return sourceQueue;
    }

    public void stop() {
        log.info(String.format("%s received a termination signal, stopping ... ", getName()));
        this.shouldWork.set(false);
        int tryTime = 0;
        while (isRunning.get() && tryTime < 50) {
            try {
                Thread.currentThread().sleep(50L);
            } catch (Exception ex) {

            }
            tryTime++;
        }
    }

    public void submit(T item) {
        sourceQueue.offer(item);
    }
}

