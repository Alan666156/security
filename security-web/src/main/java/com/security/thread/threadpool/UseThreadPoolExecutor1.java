package com.security.thread.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池
 *
 * @author fuhongxing
 */
public class UseThreadPoolExecutor1 {

    public static void main(String[] args) {
        /**
         * 在使用有界队列时，若有新的任务需要执行，如果线程池实际线程数小于corePoolSize，则优先创建线程，
         * 若大于corePoolSize，则会将任务加入队列，
         * 若队列已满，则在总线程数不大于maximumPoolSize的前提下，创建新的线程，
         * 若线程数大于maximumPoolSize，则执行拒绝策略。或其他自定义方式。
         *
         */
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                //coreSize：指定线程池线程的数量（线程池中常驻核心线程数），类似银行网点
                1,
                //MaxSize：指定线程池中线程的最大数量（线程池能够容纳同时执行的最大线程数，此值必须大于1），类似银行窗口
                2,
                //keepAliveTime：空闲线程存活的时间，如果超过了corePoolSize，在keepAliveTime的时间之后，销毁线程
                60,
                TimeUnit.SECONDS,
                //指定一种队列 （有界队列）
                new ArrayBlockingQueue<Runnable>(3)
                //new LinkedBlockingQueue<Runnable>()
                , new MyRejected()
                //, new DiscardOldestPolicy()
        );

        MyTask mt1 = new MyTask(1, "任务1");
        MyTask mt2 = new MyTask(2, "任务2");
        MyTask mt3 = new MyTask(3, "任务3");
        MyTask mt4 = new MyTask(4, "任务4");
        MyTask mt5 = new MyTask(5, "任务5");
        MyTask mt6 = new MyTask(6, "任务6");

        pool.execute(mt1);
        pool.execute(mt2);
        pool.execute(mt3);
        pool.execute(mt4);
        pool.execute(mt5);
        pool.execute(mt6);

        pool.shutdown();

    }
}
