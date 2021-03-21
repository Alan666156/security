package com.security.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 如何优雅的结束线程
 * 1、使用stop()方法，已被弃用。原因是：stop()是立即终止，会导致一些数据被到处理一部分就会被终止，而用户并不知道哪些数据被处理，哪些没有被处理，产生了不完整的“残疾”数据，不符合完整性，所以被废弃
 * 2、实现一个Runnable接口，在其中定义volatile标志位，在run()方法中使用标志位控制程序运行（使用volatile目的是保证可见性，一处修改了标志，处处都要去主存读取新的值）
 * 3、使用interrupt()中断的方式，注意使用interrupt()方法中断正在运行中的线程只会修改中断状态位，可以通过isInterrupted()判断。如果使用interrupt()方法中断阻塞中的线程，那么就会抛出InterruptedException异常，可以通过catch捕获异常，然后进行处理后终止线程。有些情况，我们不能判断线程的状态，所以使用interrupt()方法时一定要慎重考虑。
 * @author fuhongxing
 */
@Slf4j
public class MyRunnable implements Runnable {

	/**
	 * 定义退出标志，true会一直执行，false会退出循环
	 * 使用volatile目的是保证可见性，一处修改了标志，处处都要去主存读取新的值，而不是使用缓存
	 */
	public volatile boolean flag = true;
 
	@Override
	public void run() {
		log.info("第{}个线程创建", Thread.currentThread().getName());
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//退出标志生效位置
		while (flag) {
		}
		//运行时的线程被中断后，只会修改中断标记，不会抛出异常
		while(Thread.currentThread().isInterrupted()){

		}
		log.info("第{}个线程终止", Thread.currentThread().getName());
	}

	public static void main(String[] arg) throws InterruptedException {
		MyRunnable runnable = new MyRunnable();
		//创建3个线程
		for (int i = 1; i <= 3; i++) {
			Thread thread = new Thread(runnable, i + "");
			thread.start();
		}
		//线程休眠
		TimeUnit.SECONDS.sleep(2);
		//修改退出标志，使线程终止
		runnable.flag = false;

		//interrupt实现线程停止
		log.info("——————————————————————————");
		//创建3个线程
		Thread thread1 = new Thread(runnable, "1");
		Thread thread2 = new Thread(runnable, "2");
		Thread thread3 = new Thread(runnable, "3");
		thread1.start();
		thread2.start();
		thread3.start();

		//线程休眠
		TimeUnit.SECONDS.sleep(2);

		//修改退出标志，使线程终止
		thread1.interrupt();
		thread2.interrupt();
		thread3.interrupt();

	}
}