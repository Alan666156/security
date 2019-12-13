package com.security.thread.concurrent;

import java.util.concurrent.SynchronousQueue;

/**
 * 高并发队列的使用
 * http://benjaminwhx.com/categories/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/
 * @author fhx
 * @date 2019年12月7日
 */
public class UseQueue {

	public static void main(String[] args) throws Exception {
		
		//高性能无阻塞无界队列：ConcurrentLinkedQueue,性能算是最好的 
		/**
		ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<String>();
		q.offer("a");
		q.offer("b");
		q.offer("c");
		q.offer("d");
		q.add("e");
		
		System.out.println(q.poll());	//a 从头部取出元素，并从队列里删除
		System.out.println(q.size());	//4
		System.out.println(q.peek());	//b
		System.out.println(q.size());	//4
		*/
		
		//使用ArrayBlockingQueue时必须指定长度，也就是它是一个有界队列
		//ArrayBlockingQueue是采用数组实现的有界阻塞线程安全队列。如果向已满的队列继续塞入元素，将导致当前的线程阻塞。如果向空队列获取元素，那么将导致当前线程阻塞
		/**
		ArrayBlockingQueue<String> array = new ArrayBlockingQueue<String>(5);
		array.put("a");
		array.put("b");
		array.add("c");
		array.add("d");
		array.add("e");
		array.add("f");
		//System.out.println(array.offer("a", 3, TimeUnit.SECONDS));
		*/
		
		//阻塞队列,内部由两个ReentrantLock来实现出入队列的线程安全,如果不指定容量，如果不指定队列的容量大小 默认为Integer.MAX_VALUE,如果存在添加速度大于删除速度时候，有可能会内存溢出，这点在使用前希望慎重考虑。
		/**
		LinkedBlockingQueue<String> q = new LinkedBlockingQueue<String>();
		q.offer("a");
		q.offer("b");
		q.offer("c");
		q.offer("d");
		q.offer("e");
		q.add("f");
		//System.out.println(q.size());
		
//		for (Iterator iterator = q.iterator(); iterator.hasNext();) {
//			String string = (String) iterator.next();
//			System.out.println(string);
//		}
		
		List<String> list = new ArrayList<String>();
		System.out.println(q.drainTo(list, 3));
		System.out.println(list.size());
		for (String string : list) {
			System.out.println(string);
		}
		*/
		
		
		final SynchronousQueue<String> q = new SynchronousQueue<String>();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(q.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				q.add("test");
			}
		});
		t2.start();		
	}
}
