package com.security.thread.volatiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS是英文单词CompareAndSwap的缩写，中文意思是：比较并替换。CAS需要有3个操作数：内存地址V，旧的预期值A，即将要更新的目标值B
 * CAS指令执行时，当且仅当内存地址V的值与预期值A相等时，将内存地址V的值修改为B，否则就什么都不做。整个比较并替换的操作是一个原子操作
 */
public class AtomicCasDemo {

	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	
	//Atomic类只能保证本身方法具备原子性，并不能保证多次操作的原子性；多个addAndGet在一个方法内是非原子性的，需要加synchronized进行修饰，保证4个addAndGet整体原子性
	/**synchronized*/
	public synchronized int multiAdd(){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//count的添加结果一定是整数10
			atomicInteger.addAndGet(1);
			atomicInteger.addAndGet(2);
			atomicInteger.addAndGet(3);
			//+10
			atomicInteger.addAndGet(4);
			return atomicInteger.get();
	}
	
	
	public static void main(String[] args) {
		//默认主内存数据为5
		AtomicInteger test = new AtomicInteger(5);
		//如果内存地址的值5与内存地址5对比相等，修改为200，打印结果true，100
		System.out.println(test.compareAndSet(5, 100) + "data:" + test.get());
		//上一步内存地址的值已经改为100，所以这里不相等，打印结果false，200
		System.out.println(test.compareAndSet(5, 200) + "data:" + test.get());

		final AtomicCasDemo au = new AtomicCasDemo();

		List<Thread> ts = new ArrayList<Thread>();
		for (int i = 0; i < 100; i++) {
			ts.add(new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(au.multiAdd());
				}
			}));
		}

		for(Thread t : ts){
			t.start();
		}


	
		

		
	}
}
