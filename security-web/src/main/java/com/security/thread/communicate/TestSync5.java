package com.security.thread.communicate;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
/**
 * 多线程通信五：基本 LockSupport 实现线程间的阻塞和唤醒
 * LockSupport 是一种非常灵活的实现线程间阻塞和唤醒的工具，使用它不用关注是等待线程先进行还是唤醒线程先运行，但是得知道线程的名字。
 *
 * @author fuhongxing
 */
public class TestSync5 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        //线程B
        final Thread threadB = new Thread(() -> {
            if (list.size() != 5) {
                //等同于wait()方法,阻塞等待通知放行需要许可证；如果先unpark，这里不会生效，相当于提前获取到了许可证
                //线程阻塞需要消耗凭证(permit)，这个凭证最多只有1个，调用park方法如果有凭证则直接消耗这个凭证并退出；如果无凭证，则阻塞等待凭证可用
                LockSupport.park();
            }
            System.out.println("线程B收到通知，开始执行自己的业务...");
        });
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
                    //发放许可证，接触线程阻塞;连续多次调用unpark和调用一次unpark的效果一样，因为凭证的上限是1，但是调用多次park就会出现凭证不够，阻塞等待
                    LockSupport.unpark(threadB);
                }
            }
        });
        threadA.start();
        threadB.start();
    }
}