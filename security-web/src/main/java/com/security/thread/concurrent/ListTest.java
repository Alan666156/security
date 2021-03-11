package com.security.thread.concurrent;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集合不安全问题:
 * ArrayList 为什么是线程不安全的？
 * 为了提升数据处理的效率性能，非线程安全的
 * 1、故障现象：
 *  java.util.ConcurrentModificationException异常
 * 2、导致原因
 *  并发争抢修改导致(一个人正在写入，另外一个人过来争抢，导致数据不一致。并发异常)
 * 3、解决方案
 *  可以使用Vector、Collections.synchronizedList()、CopyOnWriteArrayList
 * 4、如何优化
 *  使用CopyOnWriteArrayList替换
 *
 * @Author: fuhongxing
 * @Date: 2021/3/11
 **/
public class ListTest {
    //Vector加了锁，线程安全
//    private static List<String> list  = new Vector<>();
    //将list变成同步线程安全的
//    private static List<String> list  = Collections.synchronizedList(new ArrayList<>());
    //写时复制，读写分离思想；CopyOnWrite即写时复制的容器。往一个容器里面添加数据时，不直接往容器里面添加，而是先将当前容器copy，往复制的新容器中添加数据；添加完后再将原来的容器直线复制的这个容器
//    private static List<String> list  = new CopyOnWriteArrayList<>();

     public static void main(String[] args) {
         List<String> list  = new CopyOnWriteArrayList<>();
         for (int i = 0; i < 30; i++) {
             new Thread(() -> {
                 list.add(IdUtil.objectId());
                 System.out.println(list);
             }, String.valueOf(i)).start();
         }
     }

    /**
     * java.util.ConcurrentModificationException重现
     */
    public static void concurrentModificationException(){
        //重现方式一
        List<String> arrayList  = new ArrayList<>();
//         arrayList.forEach(System.out::println);
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                arrayList.add(IdUtil.objectId());
                //java.util.ConcurrentModificationException异常
                System.out.println(arrayList);
            }, String.valueOf(i)).start();
        }

        ////重现方式二
        ArrayList<TestObj> test = new ArrayList<>();
        List<TestObj> list  = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            test.add(new TestObj(i));
        }
        //遍历时删除元素
        for (TestObj obj : test) {
            if (obj.getValue() < 10) {
                //这里会抛出java.util.ConcurrentModificationException
                test.remove(obj);
            }
        }
    }
@Data
static class TestObj {
    int value;
    public TestObj() {
    }
    public TestObj(int value) {
        this.value = value;
    }

}
}
