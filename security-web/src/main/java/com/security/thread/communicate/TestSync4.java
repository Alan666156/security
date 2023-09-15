package com.security.thread.communicate;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程通信四：使用 ReentrantLock 结合 Condition
 * 这种方式使用起来并不是很好，代码编写复杂，而且线程 B 在被 A 唤醒之后由于没有获取锁还是不能立即执行，也就是说，A 在唤醒操作之后，并不释放锁。这种方法跟 Object 的 wait()/notify() 一样。
 *
 * @author fuhongxing
 */
public class TestSync4 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        List<String> list = new ArrayList<>();
        //线程A
        Thread threadA = new Thread(() -> {
            lock.lock();
            for (int i = 1; i <= 10; i++) {
                list.add(RandomUtil.randomStringUpper(4));
                System.out.println("线程A添加元素，此时list的size为：" + list.size());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list.size() == 5) {
                    condition.signal();
                }
            }
            lock.unlock();
        });
        //线程B
        Thread threadB = new Thread(() -> {
            lock.lock();
            if (list.size() != 5) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("线程B收到通知，开始执行自己的业务...");
            lock.unlock();
        });
        threadB.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.start();
    }
}