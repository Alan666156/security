package com.security.thread.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁的使用
 *
 * 对比公平锁和非公平锁的tryAcqure()方法的实现代码， 其实差别就在于非公平锁获取锁时比公平锁中少了一个判断!hasQueuedPredecessors()
 * hasQueuedPredecessors()中判断了是否需要排队，导致公平锁和非公平锁的差异如下:
 * 公平锁:公平锁讲究先来先到，线程在获取锁时，如果这个锁的等待队列中已经有线程在等待，那么当前线程就会进入等待队列中;
 * 非公平锁:不管是否有等待队列，如果可以获取锁，则立刻占有锁对象。也就是说队列的第一 个排队线程在unpark(), 之后还是需要竞争锁(存在线程竞争的情况下)
 * 可重入锁:可重复可递归调用的锁，在外层使用锁之后，在内层仍然可以使用，并且不发生死锁，这样的锁就叫做可重入锁。
 * 自旋锁：是指当一个线程在获取锁的时候，如果锁已经被其它线程获取，那么该线程将循环等待，然后不断的判断锁是否能够被成功获取，直到获取到锁才会退出循环。
 * @author fuhongxing
 */
public class UseReentrantLock {
	/**
	 * false非公平锁，true为公平锁
	 */
	private Lock lock = new ReentrantLock(false);

	/**
	 * 重入锁
	 */
	public void method1(){
		lock.lock();
		try {
			System.out.println("重入锁当前线程:" + Thread.currentThread().getName() + "进入method1..");
			Thread.sleep(1000);
			System.out.println("重入锁当前线程:" + Thread.currentThread().getName() + "退出method1..");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 重入锁
	 */
	public void method2(){
		lock.lock();
		try {
			System.out.println("重入锁当前线程:" + Thread.currentThread().getName() + "进入method2..");
			Thread.sleep(2000);
			System.out.println("重入锁当前线程:" + Thread.currentThread().getName() + "退出method2..");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 原子引用
	 */
	 AtomicReference<Thread> atomicReference = new AtomicReference();


	public static void main(String[] args) {
		final UseReentrantLock ur = new UseReentrantLock();
		new Thread(() -> {
			ur.method1();
			ur.method2();
		}, "t1").start();

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println(ur.lock.getQueueLength());

		//自旋锁
		SpinLock();
	}

	/**
	 * 自旋锁实现
	 */
	private static void SpinLock() {
		UseReentrantLock useReentrantLock = new UseReentrantLock();
		new Thread(() -> {
			try{
				useReentrantLock.myLock();
				try { TimeUnit.SECONDS.sleep(5); }catch (Exception e) {e.printStackTrace();}
//				System.out.println("-----A thread come in");
				useReentrantLock.myUnlock();
			}finally {

			}
		},"A").start();

		new Thread(() -> {
			try{
				useReentrantLock.myLock();
				try { TimeUnit.SECONDS.sleep(1); }catch (Exception e) {e.printStackTrace();}
				useReentrantLock.myUnlock();
//				System.out.println("-----B thread come in");
			}finally {

			}
		},"B").start();
	}

	/**
	 * 自旋锁
	 * 获取锁的线程不会立即阻塞，而是采用循环的方式去获取锁，然后不断的判断锁是否能够被成功获取，直到获取到锁才会退出循环。
	 * 优点是减少线程上下文的切换，缺点是循环会消耗CPU
	 */
	public void myLock(){
		Thread thread = Thread.currentThread();
		System.out.println("自旋锁当前线程:" + Thread.currentThread().getName() + " cmoe in...");
		//自旋
		while (!atomicReference.compareAndSet(null, thread)){

		}
	}

	public void myUnlock(){
		Thread thread = Thread.currentThread();
		atomicReference.compareAndSet(thread, null);
		System.out.println("自旋锁当前线程:" + Thread.currentThread().getName() + " invoked myUnLock...");
	}
}
