package com.security.thread.concurrent;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1、ArrayList 为什么是线程不安全的
 * 为了提升数据处理的效率，非线程安全的
 * 2、并发环境怎么处理
 *
 * 3、如何优化
 * 可以使用Vector、Collections.synchronizedList()
 * 4、解决方案
 * 使用CopyOnWriteArrayList替换
 * @Author: fuhongxing
 * @Date: 2021/3/11
 **/
public class ListTest {
//    private static List<String> list  = new Vector<>();
    //将list变成同步线程安全的
//    private static List<String> list  = Collections.synchronizedList(new ArrayList<>());
//    private static List<String> list  = new CopyOnWriteArrayList<>();
      //并发环境下替代HasMap
      Map<String, Object> map = new ConcurrentHashMap<>();
     public static void main(String[] args) {
         List<TestObj> list  = new ArrayList<>();

         //初始化集合类
         ArrayList<TestObj> test = new ArrayList<>();
         for (int i = 0; i < 100; i++) {
             test.add(new TestObj(i));
         }

         //遍历时删除元素
         for (TestObj obj : test) {
             if (obj.getValue() < 10) {
                 //这里会抛出ConcurrentModificationException
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
