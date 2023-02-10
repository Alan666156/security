package com.security.thread.volatiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 线程之间的通信:使用wait/notify方法实现线程间的通信
 * wait/notify必须配合synchronized关键字使用
 * wait notfiy 方法，wait释放锁，notfiy不释放锁
 *
 * @author fuhongxing
 */
public class ListAdd2 {
    private volatile static List<String> list = new ArrayList();

    public void add() {
        list.add("bjsxt");
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {

        final ListAdd2 list2 = new ListAdd2();

        // 1 实例化出来一个 lock
        // 当使用wait 和 notify 的时候 ， 一定要配合着synchronized关键字去使用
        //final Object lock = new Object();

        /**
         * CountDownLatch 的作用是：当一个线程需要另外一个或多个线程完成后，再开始执行。
         * 比如主线程要等待一个子线程完成环境相关配置的加载工作，主线程才继续执行，就可以利用 CountDownLatch 来实现。
         * 参数可以理解为一个计数器，这里为 1
         */
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
			try {
				//synchronized (lock) {
				for (int i = 0; i < 10; i++) {
					list2.add();
					System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素..");
					Thread.sleep(500);
					if (list2.size() == 5) {
						System.out.println("已经发出通知..");
						//countDown() 方法: 表示一个等待已经完成，把计数器减一，直到减为 0，主线程又开始执行
						countDownLatch.countDown();
						//notify()不释放锁，会导致循环必须执行完，T2才会执行，此时t2线程中list2.size()条件不会满足
						//lock.notify();
					}
				}
				//}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}, "t1");

        Thread t2 = new Thread(() -> {
			//synchronized (lock) {
			if (list2.size() != 5) {
				try {
					//System.out.println("t2进入...");
					//wait会释放锁，此时t1会获取锁
					//lock.wait();

					//await() 方法表示阻塞主线程
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("当前线程：" + Thread.currentThread().getName() + "收到通知线程停止..");
			throw new RuntimeException();
		}, "t2");

        t2.start();
        t1.start();

    }

}
