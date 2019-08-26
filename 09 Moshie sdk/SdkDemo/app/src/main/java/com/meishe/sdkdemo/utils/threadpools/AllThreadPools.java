package com.meishe.sdkdemo.utils.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by CaoZhiChao on 2018/12/18 13:53
 */
public class AllThreadPools {
    /**
     * 有序的
     *
     * @param runnable
     */
    public static void singleThread(Runnable runnable) {
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), getThreadFactory("singleThread"));
        singleThreadPool.execute(runnable);
    }

    /**
     * 无序的
     *
     * @param runnable
     */
    public static void fixedThread(Runnable runnable) {
        ExecutorService fixedThreadPool = new ThreadPoolExecutor(getCPUNum() + 1, getCPUNum() + 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), getThreadFactory("fixedThread"));
        fixedThreadPool.execute(runnable);
    }

    public static int getCPUNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 自适应线程池
     *
     * @param runnable
     */
    public static void cacheThread(Runnable runnable) {
        ExecutorService cacheThread = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), getThreadFactory("cacheThread"));
        cacheThread.execute(runnable);
    }

    public static void scheduleThread(Runnable runnable, long delay, TimeUnit unit) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2, getThreadFactory("scheduleThread"));
        executorService.schedule(runnable, delay, unit);
    }

    private static ThreadFactory getThreadFactory(String name) {
        return new CustomThreadFactoryBuilder()
                .setNamePrefix(name).setDaemon(false)
                .setPriority(Thread.NORM_PRIORITY).build();
    }

    private static ThreadFactory getThreadFactory(String name, boolean daemon, int priority) {
        return new CustomThreadFactoryBuilder()
                .setNamePrefix(name).setDaemon(daemon)
                .setPriority(priority).build();
    }

}
