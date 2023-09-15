package com.security.thread.concurrent;

import cn.hutool.core.util.IdUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合线程不安全
 *
 * @author fuhongxing
 */
public class UseCopyOnWrite {

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
//		Set<String> set  = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 1230; i++) {
            new Thread(() -> {
                set.add(IdUtil.objectId());
                //并发条件下java.util.ConcurrentModificationException异常
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}
