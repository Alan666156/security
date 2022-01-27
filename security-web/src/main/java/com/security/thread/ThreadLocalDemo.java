package com.security.thread;

import java.util.concurrent.TimeUnit;

/**
 *
 * Thread中维护了ThreadLocalMap，所以ThreadLocalMap的生命周期和Thread（当前线程）一样长。使用不当就可能会导致内存泄漏问题。但是，在ThreadLocal中，进行get，set操作的时候会清除Map里所有key为null的value。
 *
 * ThreadLocal的实现原理是每一个Thread维护一个ThreadLocalMap映射表，映射表的key是ThreadLocal实例，并且使用的是ThreadLocal的弱引用 ，value是具体需要存储的Object
 *
 * Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value，导致value对应的Object一直无法被回收，产生内存泄露。
 * @author fhx
 * @date 2019年12月6日
 */
public class ThreadLocalDemo {
	/**
	 * ThreadLocal为解决多线程程序的并发问题提供了一种新的思路
	 * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
	 */
	public static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
	public void setThreadLocal(String value){
		threadLocal.set(value);
	}
	public void getThreadLocal(){
		System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		final ThreadLocalDemo ct = new ThreadLocalDemo();
		
		new Thread(() -> {
			ct.setThreadLocal("张三");
			ct.getThreadLocal();
		}, "t1").start();
		
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
				//这里获取的为null，因为线程间变量独立
				ct.getThreadLocal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "t2").start();
	}
	
}
