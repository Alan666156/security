package com.security.thread.singletion;

/**
 * 多线程单例模式两次判断
 * 多线程环境不进行Unsafe双重检查，结果不一致可能会产生多个
 * @author fhx
 * @date 2019年12月6日
 */
public class DubbleSingleton {

	private volatile static DubbleSingleton ds;

	/**
	 * DCL双端检锁机制
	 * @return
	 */
	public  static DubbleSingleton getDs(){
		if (ds == null) {
			synchronized(DubbleSingleton.class) {
				if (ds == null) {
					ds = new DubbleSingleton();
				}
			}
		}
//		if(ds == null){
//			try {
//				//模拟初始化对象的准备时间...
//				TimeUnit.SECONDS.sleep(3);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			synchronized (DubbleSingleton.class) {
//				//这里如果不加判断可能会导致多线程环境下，每个线程获取都会一个新的实例对象
//				if(ds == null){
//					ds = new DubbleSingleton();
//				}
//			}
//		}
		return ds;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()),String.valueOf(i)).start();

		}
//		Thread t1 = new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()),"t1");
//		Thread t2 = new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()),"t2");
//		Thread t3 = new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()),"t3");
//		t1.start();
//		t2.start();
//		t3.start();
	}
	
}
