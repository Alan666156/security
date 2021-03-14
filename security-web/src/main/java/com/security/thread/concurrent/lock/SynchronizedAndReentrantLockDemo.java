package com.security.thread.concurrent.lock;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程之间按顺序调用，实现A-B>->C三个线程启动，要求如下；
 * A打印5次，B打印10次，C打印15次
 * 连续 A打印5次，B打印10次，C打印15次...
 * 执行10次
 * @author fuhongxing
 */
public class SynchronizedAndReentrantLockDemo {


    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print5();
            }
        },"A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print10();
            }
        },"B").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print15();
            }
        },"C").start();
    }

    /**
     * 多线程资源类
     */
    static class ShareResource {

        private int num = 1;
        private Lock lock = new ReentrantLock();
        /**
         * Synchronized和ReentrantLock的区别：
         *
         * Synchronized是jvm底层，ReentrantLock是juc包下的
         *
         * Synchronized不需要手动释放锁，当Synchronized代码执行完系统会自动让线程释放对锁的占用
         * ReentrantLock需要手动释放锁，如果没有主动释放锁可能会出现死锁
         *
         * Synchronized不可中断，除非抛出异常或正常运行完成
         * ReentrantLock可中断，1、设置超时lock.tryLock(3, TimeUnit.SECONDS)
         *                     2、lock.lockInterruptibly()放代码块中，调用interrupt()方法可中断
         *
         * Synchronized是非公平锁
         * ReentrantLock公平锁和非公平锁都支持，默认非公平锁，构造方法中传参设置, false为非公平锁；
         *
         * 锁绑定多个条件:
         * Synchronized没有
         * ReentrantLock用来实现分组唤醒需要唤醒的线程，可以精确唤醒，而不是像Synchronized要么随机唤醒一个线程要么全部唤醒
         */
        private Condition condition1 = lock.newCondition();
        private Condition condition2 = lock.newCondition();
        private Condition condition3 = lock.newCondition();

        public void print5(){
            lock.lock();

            try {
                //1、判断
                while (num != 1){
                    condition1.await();
                }
                //干活
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }
                //通知B
                num = 2;
                condition2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void print10(){
            lock.lock();
            try {
                //1、判断是不是
                while (num != 2){
                    condition2.await();
                }
                //干活
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }
                //通知C
                num = 3;
                condition3.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void print15(){
            lock.lock();
            try {
                //1、
                while (num != 3){
                    condition3.await();
                }
                //干活
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }
                //通知A
                num = 1;
                condition1.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
