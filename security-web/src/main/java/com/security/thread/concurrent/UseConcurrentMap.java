package com.security.thread.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * jdk1.5后使用ConcurrentHashMap替换传统的HashTable,是一个线程安全且高效的HashMap实现
 * jdk1.8使用的Segment 分段锁，最大16个段;
 * jdk1.8抛弃了原有的 Segment 分段锁，而采用了 CAS + synchronized 来保证并发安全性
 * @author fhx
 * @date 2019年12月6日
 */
public class UseConcurrentMap {

	public static void main(String[] args) {
		ConcurrentHashMap<String, Object> chm = new ConcurrentHashMap<String, Object>();
		chm.put("k1", "v1");
		chm.put("k2", "v2");
		chm.put("k3", "v3");
		chm.putIfAbsent("k4", "vvvv");
		//System.out.println(chm.get("k2"));
		//System.out.println(chm.size());
		
		for(Map.Entry<String, Object> me : chm.entrySet()){
			System.out.println("key:" + me.getKey() + ",value:" + me.getValue());
		}
	}
}
