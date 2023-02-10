package com.security.thread.concurrent.lock;

import java.util.concurrent.TimeUnit;

/**
 * 线上死锁问题排查: 死锁是指两个或两个以上的进程在执行过程中，因争夺资源而造成一种的相互等待的现象，若无外接干预都将无法推进下去
 *
 * Windows：
 * 1、使用java自带的 jps -l查看进程
 * 2、jstack 2338443  > show.txt
 * linux：
 * 1、top或ps -ef命令查看进程
 * 2、jstack 2338443  > show.txt
 * 3、top -H -p查看某个进程内部线程占用情况分析: 显示每个进程的栈跟踪 top -p 2338443 -H
 *
 * @author fuhongxing
 * @date 2021/3/14 22:16
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldLockThread(lockA, lockB), "Thread A").start();
        new Thread(new HoldLockThread(lockB, lockA), "Thread B").start();
    }
}

/**
 * 模拟死锁线层
 */
class HoldLockThread implements Runnable {
    private String lockA;
    private String lockB;

    public HoldLockThread() {
    }

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println("当前" + Thread.currentThread().getName() + "持有" + lockA + "尝试获得" + lockB);
            try {
                //第一个线程进来后，另一个线程执行了先的代码
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "持有" + lockB + "尝试获得" + lockA);

            }
        }
    }
}