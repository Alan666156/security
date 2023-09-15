package com.security.thread.threadpool;

import java.util.concurrent.*;


/**
 * 线程池(实际工作中都是自己手动创建ThreadPoolExecutor，不使用Executors创建)
 * newFixedThreadPool：返回固定长度的线程池，线程池中的线程数量是固定的。
 * newCacheThreadPool：该方法返回一个根据实际情况来进行调整线程数量的线程池，空余线程存活时间是60s
 * newSingleThreadExecutor：该方法返回一个只有一个线程的线程池。
 * newSingleThreadScheduledExecutor：该方法返回一个SchemeExecutorService对象，线程池大小为1，SchemeExecutorService接口在ThreadPoolExecutor类和 ExecutorService接口之上的扩展，在给定时间执行某任务。
 * newSchemeThreadPool：该方法返回一个SchemeExecutorService对象，可指定线程池线程数量。
 *
 * @author fuhongxing
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        //fixedThreadPool();
        myThreadPool();
        /**
         * 在使用有界队列时，若有新的任务需要执行，如果线程池实际线程数小于corePoolSize，则优先创建线程；若大于corePoolSize，则会将任务加入队列；
         * 若队列已满，则在总线程数不大于maximumPoolSize的前提下，创建新的线程，若线程数大于maximumPoolSize，则执行拒绝策略。或其他自定义方式。
         *
         * corePoolSize 指定线程池线程的数量（线程池中常驻核心线程数），类似银行网点
         * maximumPoolSize 指定线程池中线程的最大数量（线程池能够容纳同时执行的最大线程数，此值必须大于1），类似银行窗口
         * keepAliveTime 当线程池线程的数量超过corePoolSize的时候，多余的空闲线程存活的时间，如果超过了corePoolSize，在keepAliveTime的时间之后，销毁线程
         * unit keepAliveTime的单位
         * workQueue 工作队列，将被提交但尚未执行的任务缓存起来，类似银行的候客区
         * threadFactory 线程工厂，用于创建线程，不指定为默认线程工厂DefaultThreadFactory
         * handler 拒绝策略
         */
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS,
                //指定一种队列 （有界队列）
                new ArrayBlockingQueue<Runnable>(3)
                //new LinkedBlockingQueue<Runnable>()
                , new MyRejected()
                //, new DiscardOldestPolicy()
        );

//		MyTask mt1 = new MyTask(1, "任务1");
//		MyTask mt2 = new MyTask(2, "任务2");
//		MyTask mt3 = new MyTask(3, "任务3");
//		MyTask mt4 = new MyTask(4, "任务4");
//		MyTask mt5 = new MyTask(5, "任务5");
//		MyTask mt6 = new MyTask(6, "任务6");
//
//		pool.execute(mt1);
//		pool.execute(mt2);
//		pool.execute(mt3);
//		pool.execute(mt4);
//		pool.execute(mt5);
//		pool.execute(mt6);
//
//		pool.shutdown();

    }


    /**
     * newFixedThreadPool返回固定长度的线程池，线程池中的线程数量是固定的。
     */
    public static void fixedThreadPool() {
        /**
         * 创建缓冲池,初始化5个线程
         */
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        //模拟10个用户办理业务，每个用户就是一个来自外部的请求线程
        for (int i = 1; i <= 10; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 办理业务");
            });
        }
    }

    /**
     * 手动创建线程池
     * 拒绝策略：
     * ThreadPoolExecutor.AbortPolicy：线程池的默认拒绝策略为AbortPolicy，即丢弃任务并抛出RejectedExecutionException异常
     * ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃。
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务。
     * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务，如果任务被拒绝了，则由调用线程（提交任务的线程）直接执行此任务
     * 当流量比较大的时候，由于任务没有设置超时时间，处理线程会一直hang住，导致线程池被打满的时候触发饱和策略CallerRunsPolicy，由主线程去处理任务，而任务又是阻塞的，主线程hang主，从而导致主流程超时，发生故障。所以在使用饱和策略的时候需要考虑一下场景，看看是否合适，以免踩坑。
     */
    public static void myThreadPool() {
        //maximumPoolSize=5, ArrayBlockingQueue=3，这里最多能容纳8个，执行默认的拒绝策略超过就会抛出异常
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2,
                5,
                1,
                TimeUnit.SECONDS,
                //指定一种队列 （有界队列）
                new ArrayBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        //模拟10个用户办理业务，每个用户就是一个来自外部的请求线程
        for (int i = 1; i <= 9; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 办理业务");
            });
        }

    }
}
