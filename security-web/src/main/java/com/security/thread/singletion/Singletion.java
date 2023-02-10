package com.security.thread.singletion;

/**
 * 单例模式（懒汉式、饿汉式、双重检索）
 *
 * @author fhx
 * @date 2019年12月6日
 */
public class Singletion {

    /**
     * 静态内部类方式，多线程单例模式(标准)
     * 只有第一次调用getInstance方法时，虚拟机才加载 Inner 并初始化instance ，只有一个线程可以获得对象的初始化锁，其他线程无法进行初始化，保证对象的唯一性。目前此方式是所有单例模式中最推荐的模式
     */
    private static class InnerSingletion {
        private static Singletion single = new Singletion();
    }

    public static Singletion getInstance() {
        return InnerSingletion.single;
    }

    /**
     * 饿汉式
     */
//	private static Singletion instance = new Singletion();

    /**
     * 懒汉式
     */
    private static Singletion instance;

    public static Singletion getInstance2() {
        if (instance == null) {
            instance = new Singletion();
        }
        return instance;
    }

    /**
     * DCL双端检锁机制（多线程）
     * 线程安全，延迟初始化。这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
     * 双重检查模式，进行了两次的判断，第一次是为了避免不要的实例和上锁，第二次是为了进行同步，避免多线程问题。由于singleton=new Singleton()对象的创建在JVM中可能会进行重排序，在多线程访问下存在风险，使用volatile修饰signleton实例变量有效，解决该问题
     *
     * @return
     */
    public static Singletion getDSL() {
        if (instance == null) {
            synchronized (Singletion.class) {
                if (instance == null) {
                    instance = new Singletion();
                }
            }
        }
//		if(instance == null){
//			try {
//				//模拟初始化对象的准备时间...
//				TimeUnit.SECONDS.sleep(3);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			synchronized (Singletion.class) {
//				//这里如果不加判断可能会导致多线程环境下，每个线程获取都会一个新的实例对象
//				if(instance == null){
//					instance = new Singletion();
//				}
//			}
//		}
        return instance;
    }

}
