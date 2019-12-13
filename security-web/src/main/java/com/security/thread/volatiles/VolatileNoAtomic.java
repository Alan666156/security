package com.security.thread.volatiles;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile关键字不具备synchronized关键字的原子性（同步）
 * @author alienware
 *
 */
public class VolatileNoAtomic extends Thread{
	//volatile只具有可见性，不具备原子性
	//private static volatile int count;
	
	//Atomic类可以保证原子性
	private static AtomicInteger count = new AtomicInteger(0);
	private static void addCount(){
		for (int i = 0; i < 1000; i++) {
			//count++ ;
			count.incrementAndGet();
		}
		System.out.println(count);
	}
	
	public void run(){
		addCount();
	}
	
	public static void main(String[] args) {
		
		VolatileNoAtomic[] arr = new VolatileNoAtomic[100];
		for (int i = 0; i < 10; i++) {
			arr[i] = new VolatileNoAtomic();
		}
		
		for (int i = 0; i < 10; i++) {
			arr[i].start();
		}
	}
	
	
	
	
}
