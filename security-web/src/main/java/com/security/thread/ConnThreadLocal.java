package com.security.thread;
/**
 * 
 * @author fhx
 * @date 2019年12月6日
 */
public class ConnThreadLocal {
	/**
	 * ThreadLocal为解决多线程程序的并发问题提供了一种新的思路
	 * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
	 */
	public static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
	public void setThreadLocal(String value){
		threadLocal.set(value);
	}
	public void getThreadLocal(){
		System.out.println(Thread.currentThread().getName() + ":" + this.threadLocal.get());
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		final ConnThreadLocal ct = new ConnThreadLocal();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				ct.setThreadLocal("张三");
				ct.getThreadLocal();
			}
		}, "t1");
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					//这里获取的为null，因为线程间变量独立
					ct.getThreadLocal();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t2");
		
		t1.start();
		t2.start();
	}
	
}
