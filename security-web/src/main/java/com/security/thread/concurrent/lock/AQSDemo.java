package com.security.thread.concurrent.lock;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AQS问题
 * AQS使用一个volatile的int类型的成员变量来表示同步状态，通过内置的 FIFO队列来完成资源获取的排队工作将每条要去抢占资源的线程封装成 一个Node节点来实现锁的分配，通过CAS完成对State值的修改。
 * AbstractQueuedSynchronizer又称为队列同步器(后面简称AQS)，它是用来构建锁或其他同步组件的基础框架
 * @author fuhx
 */
public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        //带入一个银行办理业务的案例来模拟我们的AQS如何进行线程的管理和通知唤醒机制

        //3个线程模拟3个来银行网点，受理窗口办理业务的顾客
        //A顾客就是第一个顾客，此时受理窗口没有任何人，A可以直接去办理
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("-----A thread come in");

                try { TimeUnit.SECONDS.sleep(5); }catch (Exception e) {e.printStackTrace();}
            }finally {
                lock.unlock();
            }
        },"A").start();

        //第二个顾客，第二个线程---》由于受理业务的窗口只有一个(只能一个线程持有锁)，此时B只能等待，
        //进入候客区
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("-----B thread come in");
            }finally {
                lock.unlock();
            }
        },"B").start();

        //第三个顾客，第三个线程---》由于受理业务的窗口只有一个(只能一个线程持有锁)，此时C只能等待，
        //进入候客区
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("-----C thread come in");
            }finally {
                lock.unlock();
            }
        },"C").start();

    }
}
