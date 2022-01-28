package com.security.thread.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;


/**
 * wait和notify必须在同步代码块或同一方法中 必须先等待后唤醒，线程才能够被唤醒
 */
public class LockSupportDemo {

    private static Object object = new Object();
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
//        synchronizedWaitNotify();
//        lockWaitNotify();
        lockSupportNotify();
    }

    /**
     * 线程阻塞工具类，增强版wait和notify，不区分操作顺序
     */
    private static void lockSupportNotify() {

        Thread t1 = new Thread(() -> {
            try {
                //休眠3秒
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " ==>come in" + System.currentTimeMillis());
            //等同于wait()方法,阻塞等待通知放行需要许可证；如果先unpark，这里不会生效，相当于提前获取到了许可证
            //线程阻塞需要消耗凭证(permit)，这个凭证最多只有1个，调用park方法如果有凭证则直接消耗这个凭证并退出；如果无凭证，则阻塞等待凭证可用
            LockSupport.park();
//            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " ==>被唤醒" + System.currentTimeMillis());

        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                //发放许可证，接触线程阻塞;连续多次调用unpark和调用一次unpark的效果一样，因为凭证的上限是1，但是调用多次park就会出现凭证不够，阻塞等待
                LockSupport.unpark(t1);
//                LockSupport.unpark(t1);
                System.out.println(Thread.currentThread().getName() + " ==>notify通知");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }, "t2");
        t2.start();
    }

    /**
     * 如果不加锁会报错：java.lang.IllegalMonitorStateException
     */
    private static void lockWaitNotify() {

        new Thread(() -> {
            try {
                //休眠3秒
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " ==>come in");
                condition.await();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + " ==>被唤醒");

        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + " ==>notify通知");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * synchronized代码块去掉会出现异常：java.lang.IllegalMonitorStateException
     */
    private static void synchronizedWaitNotify() {
        new Thread(() -> {
            try {
                //休眠3秒,如果t2先notify通知，t1就会导致一直等待
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (object){
                try {
                    System.out.println(Thread.currentThread().getName() + " ==>come in");
                    object.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " ==>被唤醒");
            }

        }, "t1").start();

        new Thread(() -> {
            synchronized (object){
                try {
                    object.notify();
                    System.out.println(Thread.currentThread().getName() + " ==>notify通知");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();
    }

}
