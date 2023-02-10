package com.security.thread.concurrent.lock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS是英文单词CompareAndSwap的缩写，中文意思是：比较并替换。CAS需要有3个操作数：内存地址V，旧的预期值A，即将要更新的目标值B
 * CAS指令执行时，当且仅当内存地址V的值与预期值A相等时，将内存地址V的值修改为B，否则就什么都不做。整个比较并替换的操作是一个原子操作
 * CAS的缺点，CAS虽然很高效的解决了原子操作问题，但是CAS仍然存在三大问题：
 * 1、循环时间长开销很大。
 * 2、只能保证一个共享变量的原子操作。
 * 3、ABA问题。
 */
@Slf4j
public class AtomicCasDemo {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    //Atomic类只能保证本身方法具备原子性，并不能保证多次操作的原子性；多个addAndGet在一个方法内是非原子性的，需要加synchronized进行修饰，保证4个addAndGet整体原子性

    /**
     * synchronized
     */
    public synchronized int multiAdd() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //count的添加结果一定是整数10
        ATOMIC_INTEGER.addAndGet(1);
        ATOMIC_INTEGER.addAndGet(2);
        ATOMIC_INTEGER.addAndGet(3);
        //+10
        ATOMIC_INTEGER.addAndGet(4);
        return ATOMIC_INTEGER.get();
    }

    @Data
    @ToString
    @AllArgsConstructor
    static class Users {
        String name;
        int age;
    }

    /**
     * 原子引用类型，针对自定义class
     */
    public static void atomicReference() {
        Users sam = new Users("Sam", 20);
        Users alan = new Users("Alan", 26);
        AtomicReference<Users> atomicReference = new AtomicReference<>();
        atomicReference.set(sam);
        System.out.println(atomicReference.compareAndSet(sam, alan) + "data:" + atomicReference.get().toString());
        //上一步内存地址的值已经改为alan
        System.out.println(atomicReference.compareAndSet(sam, alan) + "data:" + atomicReference.get().toString());
    }

    /**
     * ABA问题重现与解决
     * 如果内存地址V初次读取的值是A，并且在准备赋值的时候检查到它的值仍然为A，那我们就能说它的值没有被其他线程改变过了吗？
     */
    public static void atomicStampedReference() {
        System.out.println("============ABA问题重现============");
        //ABA问题重现
        AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
        new Thread(() -> {
            //修改成功后，再改回原值
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            //修改成功后，再改回原值
            try {
                //暂停2秒t2线程，保证上面t1线程完成ABA操作
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + "-->" + atomicReference.compareAndSet(100, 200) + "，data:" + atomicReference.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2").start();

        System.out.println("============ABA问题解决============");
        //ABA问题解决
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

        new Thread(() -> {
            try {
                //获取版本号后休眠1秒，保证t4同时也拿到首次的版本号
                int stamp = atomicStampedReference.getStamp();
                System.out.println(Thread.currentThread().getName() + "第1次版本号：" + stamp);
                TimeUnit.SECONDS.sleep(1);
                //修改成功后，再改回原值
                atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + "第2次版本号：" + atomicStampedReference.getStamp());
                atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
                //暂停1秒t3线程，保证上面t1线程完成ABA操作
                System.out.println(Thread.currentThread().getName() + "第3次版本号：" + atomicStampedReference.getStamp() + ", 实际最新值:" + atomicStampedReference.getReference());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();

        new Thread(() -> {
            try {
                //与t3同时获取版本号
                int stamp = atomicStampedReference.getStamp();
                System.out.println(Thread.currentThread().getName() + "第1次版本号：" + stamp);
                TimeUnit.SECONDS.sleep(3);
                //暂停3秒t4线程，保证上面t1线程完成ABA操作
                System.out.println(Thread.currentThread().getName() + "修改是否成功：" + atomicStampedReference.compareAndSet(100, 200, stamp, stamp + 1) +
                        ", 实际最新值:" + atomicStampedReference.getReference() + "，版本号：" + atomicStampedReference.getStamp());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t4").start();

    }

    public static void main(String[] args) {
        //默认主内存数据为5
        AtomicInteger test = new AtomicInteger(5);
        //如果内存地址的值5与内存地址5对比相等，修改为200，打印结果true，100
        System.out.println(test.compareAndSet(5, 100) + " ,data:" + test.get());
        //上一步内存地址的值已经改为100，所以这里不相等，打印结果false，200
        System.out.println(test.compareAndSet(5, 200) + " ,data:" + test.get());
        final AtomicCasDemo au = new AtomicCasDemo();
        List<Thread> ts = new ArrayList<Thread>();
//		for (int i = 0; i < 100; i++) {
//			ts.add(new Thread(() -> System.out.println(au.multiAdd())));
//		}
//
//		for(Thread t : ts){
//			t.start();
//		}

        //aba问题处理
        AtomicCasDemo.atomicStampedReference();
    }
}
