package com.security.thread.volatiles;
/**
 * volatile关键字:多个线程共享可见，不具备原子性
 * volatile保证可见性的原理是在每次访问变量时都会进行一次刷新，因此每次访问都是主内存中最新的版本。所以volatile关键字的作用之一就是保证变量修改的实时可见性。
 * 参考：https://blog.csdn.net/zixiao217/article/details/72833716
 * @author fhx
 * @date 2019年12月6日
 */
public class RunThread extends Thread{
	
	//volatile
	private volatile boolean isRunning = true;
	private void setRunning(boolean isRunning){
		this.isRunning = isRunning;
	}
	
	public void run(){
		System.out.println("进入run方法..");
		int i = 0;
		while(isRunning == true){
			//..
		}
		System.out.println("线程停止");
	}
	/**
	 * 分析：jdk1.5以后，可以使用volatile变量禁止指令重排序；
	 * Java内存模型规定，对于多个线程共享的变量，存储在主内存当中，每个线程都有自己独立的工作内存，线程只能访问自己的工作内存，不可以访问其它线程的工作内存。
	 * 工作内存中保存了主内存共享变量的副本，线程要操作这些共享变量，只能通过操作工作内存中的副本来实现，操作完毕之后再同步回到主内存当中。
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		RunThread rt = new RunThread();
		rt.start();
		Thread.sleep(1000);
		rt.setRunning(false);
		System.out.println("isRunning的值已经被设置了false");
	}
	
	
}
