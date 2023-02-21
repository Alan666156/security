package com.security.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * jvm gc测试
 * idea配置options中输入-XX:+PrintGCDetails
 *
 * @author fuhongxing
 */
public class ReferenceCountingGc {
    public Object instance = null;

    private static final int SIZE = 1024 * 1024;

    public static void main(String[] args) {
        ReferenceCountingGc referenceCountingGc = new ReferenceCountingGc();
        ReferenceCountingGc countingGc = new ReferenceCountingGc();
        referenceCountingGc.instance = countingGc;
        countingGc.instance = referenceCountingGc;
        //手动出发垃圾回收
        System.gc();

        // jvm 引用测试
        referenceCountingGc.referenceTest();

    }

    /**
     * 强引用、软引用、弱引用、虚引用
     */
    public void referenceTest() {
        // 强引用：只要强引用还存在，垃圾收集器永远不会回收掉被引用的对象
        User user = new User();
        System.gc();
        System.out.println("强引用 user=" + user);

        // 软引用:用来描述一些还有用但并非必须的对象，在系统将要发生内存溢出前回收
        User user2 = new User();
        SoftReference<User> softReference = new SoftReference<>(user2);
        System.out.println("软引用 user=" +softReference.get());
        user2 = null;
        System.gc();
        System.out.println("软引用垃圾回收后 user=" + softReference.get());

        // 软引用：被弱引用关联的对象只能生存到下一次垃圾收集发生之前
        User user3 = new User();
        WeakReference<User> weakReference = new WeakReference<>(user3);
        System.out.println("弱引用 user=" + weakReference.get());
        user3 = null;
        System.gc();
        System.out.println("弱引用垃圾回收后 user=" + weakReference.get());

        // 虚引用：是最弱的一种引用关系。一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例。它的作用是能在这个对象被收集器回收时收到一个系统通知（用来得知对象是否被GC）
        User user4 = new User();
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        PhantomReference<User> phantomReference = new PhantomReference<>(user4, referenceQueue);
        System.out.println("虚引用 user=" + phantomReference.get());
        user4 = null;
        System.gc();
        System.out.println("虚引用垃圾回收后 user=" + phantomReference.get());


    }
}
