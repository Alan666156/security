package com.security.thread.concurrent.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 读写锁
 * 多个线程读一个资源类没有任何问题，所以为了满足并发量，读取共享资源应该可以同时进行
 * ReadWriteLock管理一组锁，一个是只读的锁，一个是写锁。读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
 * 总结:
 * 1、如果当前没有写锁或读锁时，第一个获取锁的线程都会成功，无论该锁是写锁还是读锁。
 * 2、如果当前已经有了读锁，那么这时获取写锁将失败，获取读锁有可能成功也有可能失败
 * 2、如果当前已经有了写锁，那么这时获取读锁或写锁，如果线程相同（可重入），那么成功；否则失败
 * <p>
 * 可重入: 允许读锁可写锁可重入。写锁可以获得读锁，读锁不能获得写锁。
 * 锁降级: 允许写锁降低为读锁
 * 中断锁的获取: 在读锁和写锁的获取过程中支持中断
 * 支持Condition: 写锁提供Condition实现
 * 监控: 提供确定锁是否被持有等辅助方法
 *
 * @author fuhongxing
 */
public class UseReentrantReadWriteLock {
    /**
     * 读写锁比互斥锁允许对于共享数据更大程度的并发。每次只能有一个写线程，但是同时可以有多个线程并发地读数据。ReadWriteLock适用于读多写少的并发情况。
     */
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReadLock readLock = rwLock.readLock();
    private WriteLock writeLock = rwLock.writeLock();

    public void read() {
        readLock.lock();
        try {
            System.out.println("当前线程:" + Thread.currentThread().getName() + "进入...");
            Thread.sleep(3000);
            System.out.println("当前线程:" + Thread.currentThread().getName() + "退出...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write() {
        writeLock.lock();
        try {
            System.out.println("当前write线程:" + Thread.currentThread().getName() + "进入...");
            Thread.sleep(3000);
            System.out.println("当前write线程:" + Thread.currentThread().getName() + "退出...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {

        final UseReentrantReadWriteLock urrw = new UseReentrantReadWriteLock();

        Thread t1 = new Thread(() -> urrw.read(), "t1");
        Thread t2 = new Thread(() -> urrw.read(), "t2");
        Thread t3 = new Thread(() -> urrw.write(), "t3");
        Thread t4 = new Thread(() -> urrw.write(), "t4");

//		t1.start();
//		t2.start();

//		t1.start(); // R 
//		t3.start(); // W

        t3.start();
        t4.start();

        urrw.readWriteLock();

    }

    /**
     * 读写锁案例
     */
    public void readWriteLock() {
        MyCache myCache = new MyCache();
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.put(temp + "", temp + "");
            }, String.valueOf(i)).start();
        }

        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.get(temp + "");
            }, String.valueOf(i)).start();
        }
    }

    /**
     *
     */
    static class MyCache {
        private volatile Map<String, Object> map = new HashMap<>();
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        public void put(String key, Object value) {
            reentrantReadWriteLock.writeLock().lock();
            try {
                System.out.println("当前write线程:" + Thread.currentThread().getName() + "开始...");
                TimeUnit.SECONDS.sleep(3);
                map.put(key, value);
                System.out.println("当前write线程:" + Thread.currentThread().getName() + "结束...");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }

        }

        public void get(String key) {
            reentrantReadWriteLock.readLock().lock();
            try {
                System.out.println("当前read线程:" + Thread.currentThread().getName() + "开始...");
                TimeUnit.SECONDS.sleep(3);
                map.get(key);
                System.out.println("当前read线程:" + Thread.currentThread().getName() + "结束...");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }

        }
    }
}
