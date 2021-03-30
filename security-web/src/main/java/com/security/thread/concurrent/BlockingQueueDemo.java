package com.security.thread.concurrent;

import java.util.concurrent.*;

/**
 * 阻塞队列
 * 阻塞队列应用场景：
 * 	1、线程池
 * 	2、生产者消费者模式
 * @author fuhongxing
 */
public class BlockingQueueDemo {

	
	public static void main(String[] args) throws Exception{

//		arrayBlockQueue();
		synchronousQueue();

		PriorityBlockingQueue<Task> q = new PriorityBlockingQueue<Task>();
		
		Task t1 = new Task();
		t1.setId(3);
		t1.setName("id为3");
		Task t2 = new Task();
		t2.setId(4);
		t2.setName("id为4");
		Task t3 = new Task();
		t3.setId(1);
		t3.setName("id为1");
		
		//return this.id > task.id ? 1 : 0;
		q.add(t1);	//3
		q.add(t2);	//4
		q.add(t3);  //1
		
		// 1 3 4
		System.out.println("容器：" + q);
		System.out.println(q.take().getId());
		System.out.println("容器：" + q);
//		System.out.println(q.take().getId());
//		System.out.println(q.take().getId());
		

		
	}

	/**
	 * ArrayBlockingQueue是一个阻塞式的队列，底层以数组的形式保存数据(实际上可看作一个循环数组)。常用的操作包括 add，offer，put，remove，poll，take，peek。
	 * 特点：
	 * 	先进先出队列（队列头的是最先进队的元素；队列尾的是最后进队的元素）
	 * 	有界队列（即初始化时指定的容量，就是队列最大的容量，不会出现扩容，容量满，则阻塞进队操作；容量空，则阻塞出队操作）
	 * 	队列不支持空元素
	 *
	 * add(E e)：把 e 加到 BlockingQueue 里，即如果 BlockingQueue 可以容纳，则返回 true，否则报异常
	 * offer(E e)：表示如果可能的话，将 e 加到 BlockingQueue 里，即如果 BlockingQueue 可以容纳，则返回 true，否则返回 false
	 * put(E e)：把 e 加到 BlockingQueue 里，如果 BlockQueue 没有空间，则调用此方法的线程被阻断直到 BlockingQueue 里面有空间再继续
	 * poll(time)：取走 BlockingQueue 里排在首位的对象，若不能立即取出，则可以等 time 参数规定的时间,取不到时返回 null
	 * take()：取走 BlockingQueue 里排在首位的对象，若 BlockingQueue 为空，阻断进入等待状态直到 Blocking 有新的对象被加入为止
	 * remainingCapacity()：剩余可用的大小。等于初始容量减去当前的 size
	 */
	private static void arrayBlockQueue() {
		BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
		//add方法如果 BlockingQueue 可以容纳，则返回 true，否则报异常
		System.out.println(blockingQueue.add("a"));
		System.out.println(blockingQueue.add("b"));
		System.out.println(blockingQueue.add("c"));
		System.out.println(blockingQueue.add("e"));
		//add方法如果 BlockingQueue 可以容纳，则返回 true，否则返回 false
		System.out.println(blockingQueue.offer("a"));
		System.out.println(blockingQueue.offer("b"));
		System.out.println(blockingQueue.offer("c"));
		System.out.println(blockingQueue.offer("d"));
	}

	/**
	 * SynchronousQueue是无界的，是一种无缓冲的等待队列（生产消费模式）
	 * SynchronousQueue作为阻塞队列的时候，对于每一个take的线程会阻塞直到有一个put的线程放入元素为止，反之亦然。
	 * 在SynchronousQueue内部没有任何存放元素的能力,在某次添加元素后必须等待其他线程取走后才能继续添加
	 * 注意1：它一种阻塞队列，其中每个 put 必须等待一个 take，反之亦然。
	 *        同步队列没有任何内部容量，甚至连一个队列的容量都没有。
	 *  注意2：它是线程安全的，是阻塞的。
	 *  注意3:不允许使用 null 元素。
	 *  注意4：公平排序策略是指调用put的线程之间，或take的线程之间。
	 *  公平排序策略可以查考ArrayBlockingQueue中的公平策略。
	 *  注意5:SynchronousQueue的以下方法很有趣：
	 *  iterator() 永远返回空，因为里面没东西。
	 *  peek() 永远返回null。
	 *  put() 往queue放进去一个element以后就一直wait直到有其他thread进来把这个element取走。
	 *  offer() 往queue里放一个element后立即返回，如果碰巧这个element被另一个thread取走了，offer方法返回true，认为offer成功；否则返回false。
	 *  offer(2000, TimeUnit.SECONDS) 往queue里放一个element但是等待指定的时间后才返回，返回的逻辑和offer()方法一样。
	 *  take() 取出并且remove掉queue里的element（认为是在queue里的。。。），取不到东西他会一直等。
	 *  poll() 取出并且remove掉queue里的element（认为是在queue里的。。。），只有到碰巧另外一个线程正在往queue里offer数据或者put数据的时候，该方法才会取到东西。否则立即返回null。
	 *  poll(2000, TimeUnit.SECONDS) 等待指定的时间然后取出并且remove掉queue里的element,其实就是再等其他的thread来往里塞。
	 *  isEmpty()永远是true。
	 *  remainingCapacity() 永远是0。
	 *  remove()和removeAll() 永远是false。
	 */
	private static void synchronousQueue() {
		BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
		new Thread(() -> {
			try {
				System.out.println(Thread.currentThread().getName() + ": put 1" );
				blockingQueue.put("1");
				System.out.println(Thread.currentThread().getName() + ": put 2" );
				blockingQueue.put("2");
				System.out.println(Thread.currentThread().getName() + ": put 3" );
				blockingQueue.put("3");
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		},"t1").start();

		//t1线程执行queue.put(1) 后就被阻塞了，只有t2线程take()进行了消费，put线程才可以返回。可以认为这是一种线程与线程间一对一传递消息的模型
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(3);
				System.out.println(Thread.currentThread().getName() + ": " + blockingQueue.take());

				TimeUnit.SECONDS.sleep(2);
				System.out.println(Thread.currentThread().getName() + ": " + blockingQueue.take());

				TimeUnit.SECONDS.sleep(2);
				System.out.println(Thread.currentThread().getName() + ": " + blockingQueue.take());
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		},"t2").start();

	}
}
