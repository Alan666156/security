package com.security.thread.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 场景：现实生活中我们经常会遇到这样的情景，在进行某个活动前需要等待人全部都齐了才开始。例如吃饭时要等全家人都上座了才动筷子，旅游时要等全部人都到齐了才出发，比赛时要等运动员都上场后才开始。
 * CyclicBarrier 相比 CountDownLatch 来说，要简单很多，其源码没有什么高深的地方，它是 ReentrantLock 和 Condition 的组合使用。
 *
 * 实现原理：在CyclicBarrier的内部定义了一个Lock对象，每当一个线程调用await方法时，将拦截的线程数减1，然后判断剩余拦截数是否为初始值parties，如果不是，进入Lock对象的条件队列等待。如果是，执行barrierAction对象的Runnable方法，然后将锁的条件队列中的所有线程放入锁等待队列中，这些线程会依次的获取锁、释放锁。
 * @author fuhongxing
 *
 */
public class CyclicBarrierDemo {

	public static void main(String[] args) throws Exception {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(10, ()->{
			System.out.println("当前线程：" + Thread.currentThread().getName() + "人员到齐，开饭");
		});
		cclicBarrierTest(cyclicBarrier);
	}

    /**
     * 允许一组线程全部等待彼此达到共同屏障点的同步辅助
	 * @param cyclicBarrier
     * @throws InterruptedException
	 */
	private static void cclicBarrierTest(CyclicBarrier cyclicBarrier) throws Exception {
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					System.out.println("当前线程：" + Thread.currentThread().getName() + "开饭集合");
					//每次执行加1
					cyclicBarrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}, String.valueOf(i)).start();
		}
	}

}
