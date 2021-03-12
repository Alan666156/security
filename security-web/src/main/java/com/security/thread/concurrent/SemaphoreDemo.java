package com.security.thread.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 场景：信号灯
 * Semaphore也是一个线程同步的辅助类，可以维护当前访问自身的线程个数，并提供了同步机制。使用Semaphore可以控制同时访问资源的线程个数，例如，实现一个文件允许的并发访问数。
 * @author fuhongxing
 */
public class SemaphoreDemo {

	public static void main(String[] args) throws Exception {
		Semaphore semapHore = new Semaphore(5);
		semapHoreTest(semapHore);
	}

    /**
     *
	 * @param semapHore
     * @throws InterruptedException
	 */
	private static void semapHoreTest(Semaphore semapHore) throws Exception {
		//模拟10辆车，只有5个车位
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					System.out.println("当前线程：" + Thread.currentThread().getName() + "抢到车位");
					//从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
					semapHore.acquire();
					TimeUnit.SECONDS.sleep(3);
					System.out.println("当前线程：" + Thread.currentThread().getName() + "抢到车位");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					//释放一个许可，将其返回给信号量
					semapHore.release();
				}
			}, String.valueOf(i)).start();
		}


	}

}
