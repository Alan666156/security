package com.security.thread.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁的使用
 * 对比公平锁和非公平锁的tryAcqure()方法的实现代码， 其实差别就在于非公平锁获取锁时比公平锁中少了一个判断!hasQueuedPredecessors()
 * hasQueuedPredecessors()中判断了是否需要排队，导致公平锁和非公平锁的差异如下:
 * 公平锁:公平锁讲究先来先到，线程在获取锁时，如果这个锁的等待队列中已经有线程在等待，那么当前线程就会进入等待队列中;
 * 非公平锁:不管是否有等待队列，如果可以获取锁，则立刻占有锁对象。也就是说队列的第一 个排队线程在unpark(), 之后还是需要竞争锁(存在线程竞争的情况下)
 * 可重入锁:可重复可递归调用的锁，在外层使用锁之后，在内层仍然可以使用，并且不发生死锁，这样的锁就叫做可重入锁。
 * @author fuhongxing
 */
public class UseReentrantLock {
	/**
	 * false非公平锁，true为公平锁
	 */
	private Lock lock = new ReentrantLock(false);
	
	public void method1(){
		lock.lock();
		try {
			System.out.println("当前线程:" + Thread.currentThread().getName() + "进入method1..");
			Thread.sleep(1000);
			System.out.println("当前线程:" + Thread.currentThread().getName() + "退出method1..");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void method2(){
		lock.lock();
		try {
			System.out.println("当前线程:" + Thread.currentThread().getName() + "进入method2..");
			Thread.sleep(2000);
			System.out.println("当前线程:" + Thread.currentThread().getName() + "退出method2..");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {

		final UseReentrantLock ur = new UseReentrantLock();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				ur.method1();
				ur.method2();
			}
		}, "t1");

		t1.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println(ur.lock.getQueueLength());
	}
	
	
}
