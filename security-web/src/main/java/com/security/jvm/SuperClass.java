package com.security.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * 对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过其子类来引用父类中定义的静态字段，只会触发父类的初始化，而不会初始化子类
 * @author fuhx
 */
public class SuperClass {

    static {
        System.out.println("SuperClass static init");
    }
    public static int value = 10;
    public SuperClass(){
        System.out.println("SuperClass init");
    }
}

class SubClass extends SuperClass {
    static {
        System.out.println("SubClass static init");
    }
    public SubClass(){
        System.out.println("SubClass init");
    }
}

class TestClass {
    public static void main(String[] args) {
        System.out.println(SubClass.value);

        Map<String, String> map = new HashMap<>();
        map.put("k1", "k1");
        map.put("k1", "k2");
        map.put(null, "k3");
        System.out.println("map ==" + map.toString());
        //a的值是129，转换成二进制就是10000001
        int a = 129;
        //b的值是128，转换成二进制就是10000000
        int b = 128;
        String key = "k1";
        int h = key.hashCode();
        int hash;
        System.out.println(">>>" + h + "  " + (h ^ (h >>> 16)));
        System.out.println("位移运算" + (1<<4) + "-->" +(16>>2));
        System.out.println("128转二进制=" + (Integer.toBinaryString(128)));
        System.out.println("129转二进制=" + (Integer.toBinaryString(129)));
        System.out.println("二目运算(^ 异或运算符 两个二进制数值如果在同一位上相同，则结果中该位为0，否则为1，比如1011 & 0010 = 1001) == " + (a ^ b));
        System.out.println("二目运算(& 与运算符 两个二进制数值如果在同一位上都是1，则结果中该位为1，否则为0，可以认为两个都是true(1)，结果也为true(1)，比如1011 & 0110 = 0010) == " + (a & b));
        System.out.println("二目运算(| 或运算符 两个二进制数值如果在同一位上至少有一个1，则结果中该位为1，否则为0，比如1011 & 0010 = 1011) == " + (a | b));

    }
}