package com.security.jvm;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * java.lang.OutOfMemoryError 常见内存溢出场景
 * 查看默认的jvm默认的垃圾收集器： java -XX:+PrintCommandLineFlags -version
 * 常用jvm调优参数：
 * -Xms2g：初始化推大小为 2g；
 * -Xmx2g：堆最大内存为 2g；
 * -XX:NewRatio=4：设置年轻的和老年代的内存比例为 1:4；
 * -XX:SurvivorRatio=8：设置新生代 Eden 和 Survivor 比例为 8:2；
 * –XX:+UseParNewGC：指定使用 ParNew + Serial Old 垃圾回收器组合；
 * -XX:+UseParallelOldGC：指定使用 ParNew + ParNew Old 垃圾回收器组合；
 * -XX:+UseConcMarkSweepGC：指定使用 CMS + Serial Old 垃圾回收器组合；
 * -XX:+PrintGC：开启打印 gc 信息；
 * -XX:+PrintGCDetails：打印 gc 详细信息
 *
 * @author fuhongxing
 */
public class OutOfMemoryDump {

    /**
     * 设置JVM参数
     * -Xms10m
     * -Xmx10m
     * -XX:+PrintGCDetails
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:HeapDumpPath=路径
     */
    public static void main(String[] args) throws Exception {
        //默认使用物理内存堆的4分之1
        //heapError();
        stackOverFlowError();
//        directMemoryError();
//        metaspaceError();

    }

    /**
     * 栈溢出异常 jvm.OutOfMemoryDump.stackOverFlowError
     * -Xss 去调整JVM栈的大小
     * 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常，方法递归调用产生这种结果
     */
    public static void stackOverFlowError() {
        stackOverFlowError();
    }

    /**
     * 堆异常
     * java.lang.OutOfMemoryError: Java heap space
     * 堆的大小可以通过参数 –Xms、-Xmx 来指定
     */
    public static void heapError() {
        List<byte[]> buffer = new ArrayList<byte[]>();
        while (true) {
            buffer.add(new byte[10 * 1024 * 1024]);
        }
//        List<Object> list = new ArrayList<>();
//        int i = 0;
//        while(true){
//            list.add(new User(i++,UUID.randomUUID().toString()));
//        }
    }

    /**
     * GC overhead limt exceed检查是Hotspot VM 1.6定义的一个策略，通过统计GC时间来预测是否要OOM了，提前抛出异常，防止OOM发生。
     * Sun 官方对此的定义是：并行/并发回收器在GC回收时间过长时会抛出OutOfMemroyError。
     * 过长的定义是，超过98%的时间用来做GC并且回收了不到2%的堆内存。用来避免内存过小造成应用不能正常工作。
     */
    public static void gcOverheadError() {
        int i = 0;
        List<String> buffer = new ArrayList<String>();
        try {
            while (true) {
                buffer.add(String.valueOf(i++).intern());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

//        List list = new ArrayList();
//        for (int i = 0; i < 1024; i++) {
//            list.add(new byte[1024 * 1024]);
//        }
    }

    /**
     * Java的NIO中，支持直接内存的使用，可以使用Java代码获得一块堆外内存空间，这块空间是直接向操作系统申请的
     * Direct Memory堆外内存,受GC控制的
     * 通过 -XX：MaxDirectMemorySize 指定，默认与Java堆最大值（-Xmx指定）一样
     * -XX:MaxDirectMemorySize=10m
     * <p>
     * 使用堆外内存，就是为了能直接分配和释放内存，提高效率。JDK5.0之后，代码中能直接操作本地内存的方式有2种：使用未公开的Unsafe和NIO包下ByteBuffer。
     * 堆外内存的好处：
     * 可以扩展至更大的内存空间。比如超过1TB甚至比主存还大的空间
     * 理论上能减少GC暂停时间（节约了大量的堆内内存）
     * 可以在进程间共享，减少JVM间的对象复制，使得JVM的分割部署更容易实现
     * 它的持久化存储可以支持快速重启，同时还能够在测试环境中重现生产数据
     * 堆外内存能够提升IO效率
     * 堆内内存由JVM管理，属于“用户态”；而堆外内存由OS管理，属于“内核态”。
     * 如果从堆内向磁盘写数据时，数据会被先复制到堆外内存，即内核缓冲区，然后再由OS写入磁盘，使用堆外内存避免了数据从用户内向内核态的拷贝
     */
    public static void directMemoryError() throws IllegalAccessException {
        System.out.println("maxdirectMemery:" + (sun.misc.VM.maxDirectMemory() / 1024 / 1024) + "M");
        //这段代码的执行会在堆外占用1k的内存，Java堆内只会占用一个对象的指针引用的大小，堆外的这1k的空间只有当bb对象被回收时，才会被回收，
        // 这里会发现一个明显的不对称现象，就是堆外可能占用了很多，而堆内没占用多少，导致还没触发GC，那就很容易出现Direct Memory造成物理内存耗光。
        Field[] declaredFields = Unsafe.class.getDeclaredFields();
        Field field = declaredFields[0];
        field.setAccessible(true);
        Object obj = field.get(null);
        Unsafe unsafe = (Unsafe) obj;
        while (true) {
            unsafe.allocateMemory(10 * 1024 * 1024);
        }
//        for (int i = 0; i < 10240; i++) {
//            ByteBuffer.allocateDirect(10 * 1024 * 1024);
//            System.gc();
//        }
    }

    /**
     * 元空间
     * java8 及以后的版本使用Metaspace来代替永久代，Metaspace是方法区在HotSpot中的实现，它与持久代最大区别在于，Metaspace并不在虚拟机内存中而是使用本地内存也就是在JDK8中
     * 永久代（java 8 后被元空间Metaspace取代了）存放了以下信息：
     * 1）虚拟机加载的类信息
     * 2）常量池
     * 3）静态变量
     * 4）即时编译后的代码
     * <p>
     * java.lang.OutOfMemoryError: Metaspace 错误的主要原因, 是加载到内存中的 class 数量太多或者体积太大。
     * 查看jvm参数信息 java -XX:+PrintFlagsInitial （找到MetaspaceSize参数）
     * <p>
     * 修改jvm默认参数 -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
     */
    public static void metaspaceError() {
        //模拟计数多少次以后发生异常
        int i = 0;
        try {
            while (true) {
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOM.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o, new Object[]{});
                    }
                });
                enhancer.create();
            }
        } catch (Throwable e) {
            System.out.println("=================>多少次后发生异常：" + i);
            e.printStackTrace();
        }
    }

    /**
     * 内存泄漏
     * 代码栈中存在Vector对象的引用v和Object对象的引用o。在For循环中，我们不断的生成新的对象，然后将其添加到Vector对象中，之后将o引用置空。
     * 问题是当o引用被置空后，如果发生GC，我们创建的Object对象是否能够被GC回收呢？答案是否定的。因为，GC在跟踪代码栈中的引用时，会发现v引用，而继续往下跟踪，就会发现v引用指向的内存空间中又存在指向Object对象的引用。
     * 也就是说尽管o引用已经被置空，但是Object对象仍然存在其他的引用，是可以被访问到的，所以GC无法将其释放掉。如果在此循环之后，Object对象对程序已经没有任何作用，那么我们就认为此Java程序发生了内存泄漏。
     */
    public void test() {
        Vector v = new Vector(10);
        for (int i = 1; i < 100; i++) {
            Object o = new Object();
            v.add(o);
            o = null;
        }
    }

    static class OOM {

    }


}
