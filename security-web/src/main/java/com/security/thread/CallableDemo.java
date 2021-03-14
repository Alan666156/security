package com.security.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Callable使用
 * Callable 和 Future接口
 * 优点：可以获取返回值
 *      Callable是类似于Runnable的接口，实现Callable接口的类和实现Runnable的类都是可被其它线程执行的任务。
 *      Callable和Runnable有几点不同：
 *     （1）Callable规定的方法是call()，而Runnable规定的方法是run().
 *     （2）call()方法可抛出异常，而run()方法是不能抛出异常的。
 *     （3）Callable的任务执行后可返回值，运行Callable任务可拿到一个Future对象，而Runnable的任务是不能返回值的。
 *      Future 表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。
 *      通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果。
 * 缺点 :繁琐
 * @author fuhongxing
 * @date 2021/3/14 15:53
 */
public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //两个线程，一个main线程，一个FutureTask
        MyCallable callable = new MyCallable();
        FutureTask<String> futureTask = new FutureTask<>(callable);
        //同一个futureTask给到多个线程，只会执行一次
        new Thread(futureTask, "AA").start();
        new Thread(futureTask, "AB").start();

        //要求获得callable的结算结果，如果没有结算完成就要去强求，会导致阻塞，值得计算完成
//        while (!futureTask.isDone()){
//
//        }

        //线程返回结果
        String result = futureTask.get();
        System.out.println("thread result = " + result);
    }

    static class MyCallable implements Callable<String> {
        /**
         * 线程返回结果
         * @return
         * @throws Exception
         */
        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " myCallable come in");
            TimeUnit.SECONDS.sleep(2);
            return "test";
        }
    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            System.out.println("myThread");
        }
    }
}
