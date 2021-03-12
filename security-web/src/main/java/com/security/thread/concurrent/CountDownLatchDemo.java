package com.security.thread.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 的作用是：当一个线程需要另外一个或多个线程完成后，再开始执行。
 * 比如主线程要等待一个子线程完成环境相关配置的加载工作，主线程才继续执行，就可以利用 CountDownLatch 来实现。
 * 构造参数可以理解为一个计数器
 * @author fuhongxing
 *
 */
public class CountDownLatchDemo {

	public static void main(String[] args) throws Exception {
		//计数器初始大小
		final CountDownLatch countDownLatch = new CountDownLatch(10);

		countDownLatchTest(countDownLatch);

	}

	/**
	 *
	 * @param countDownLatch
	 * @throws InterruptedException
	 */
	private static void countDownLatchTest(CountDownLatch countDownLatch) throws InterruptedException {
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				System.out.println("当前线程：" + Thread.currentThread().getName() + "下班");
				//每次执行减1
				countDownLatch.countDown();
			}
		}, "t1").start();
		//阻塞线程知道计算器归零后唤醒
		countDownLatch.await();
		//这里如果不加countDownLatch，会先执行
		System.out.println("当前线程：" + Thread.currentThread().getName() + " BOSS下班");
	}

}
