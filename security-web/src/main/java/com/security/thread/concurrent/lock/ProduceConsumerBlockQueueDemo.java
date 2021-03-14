package com.security.thread.concurrent.lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者消费
 *
 * @author fuhongxing
 */
public class ProduceConsumerBlockQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(3));
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " --> 生产启动");
                myResource.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"produce").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " --> 消费启动");
                myResource.consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"consumer").start();

        TimeUnit.SECONDS.sleep(5);
        System.out.println("5秒时间到 " + Thread.currentThread().getName() + " --> 活动结束\n");
        myResource.stop();
    }

    static class MyResource{
        //默认开启，进行生成 + 消费
        private volatile Boolean flag = true;
        private AtomicInteger atomicInteger = new AtomicInteger();
        BlockingQueue<String> blockingQueue = null;

        public MyResource() {
        }

        public MyResource(BlockingQueue<String> blockingQueue) {
            this.blockingQueue = blockingQueue;
            System.out.println("队列:" + blockingQueue.getClass().getName());
        }

        /**
         * 生产
         */
        public void produce() throws InterruptedException {
            String data = null;
            while (flag){
                data = atomicInteger.incrementAndGet() + "";
                if(blockingQueue.offer(data, 2, TimeUnit.SECONDS)){
                    System.out.println(Thread.currentThread().getName() + " -->" + data + "入队成功");
                }else {
                    System.out.println(Thread.currentThread().getName() + " -->" + data + "入队失败");
                }
                TimeUnit.SECONDS.sleep(1);
            }
            System.out.println(Thread.currentThread().getName() + " 生产叫停，flag=false，生产者结束\n");
        }

        /**
         * 消费
         */
        public void consumer() throws InterruptedException {
            String data = null;
            while (flag){
                data = blockingQueue.poll(2, TimeUnit.SECONDS);
                if (data == null || data.equalsIgnoreCase("")) {
                    flag = false;
                    System.out.println(Thread.currentThread().getName() + " -->" + data + "超过2秒，没有消费到信息，消费退出");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " -->" + data + "消费成功");
            }
        }

        public void stop() throws InterruptedException {
            flag = false;
        }
    }

}
