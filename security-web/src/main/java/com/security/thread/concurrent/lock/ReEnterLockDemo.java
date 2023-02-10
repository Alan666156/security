package com.security.thread.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 可重入锁案例
 * 可重入锁:可重复可递归调用的锁，在外层使用锁之后，在内层仍然可以使用，并且不发生死锁，这样的锁就叫做可重入锁。
 * 在一个synchronized修饰的方法或代码块的内部，调用本类的其他synchronized修饰的方法或代码块时，是永远可以得到锁的
 *
 * @author fuhongxing
 */
public class ReEnterLockDemo {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(() -> {
            // 获取锁
            lock.lock();
            //lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "外层");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "内层");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //实现加锁次数和释放次数不一样
                //由于加锁次数和释放次数不一样，第二个线程始终无法获取到锁，导致一直在等待。
                lock.unlock();
                //正在情况，加锁几次就要解锁几次
                //lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("可重入锁案例:" + Thread.currentThread().getName() + "外层调用lock");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

}
