package com.security.thread.communicate;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 多线程通信一：volatile
 * 基于 volatile 关键字来实现线程间相互通信是使用共享内存的思想。大致意思就是多个线程同时监听一个变量，当这个变量发生变化的时候 ，线程能够感知并执行相应的业务。
 *
 * @author fuhongxing
 */
public class TestSync1 {
    /**
     * 定义共享变量来实现通信，它需要volatile修饰，否则线程不能及时感知
     */
    static volatile boolean notice = false;

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        //线程A
        Thread threadA = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                list.add(RandomUtil.randomStringUpper(4));
                System.out.println("线程A添加元素，此时list的size为：" + list.size());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list.size() == 5) {
                    notice = true;
                }
            }
        });
        //线程B
        Thread threadB = new Thread(() -> {
            while (true) {
                if (notice) {
                    System.out.println("线程B收到通知，开始执行自己的业务...");
                    break;
                }
            }
        });
        //需要先启动线程B
        threadB.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 再启动线程A
        threadA.start();
    }
}