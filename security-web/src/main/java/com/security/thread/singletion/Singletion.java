package com.security.thread.singletion;

/**
 * 多线程单例模式(标准),使用静态内部类的方式
 * @author fhx
 * @date 2019年12月6日
 */
public class Singletion {
	
	private static class InnerSingletion {
		private static Singletion single = new Singletion();
	}
	
	public static Singletion getInstance(){
		return InnerSingletion.single;
	}
	
}
