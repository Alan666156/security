package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CglibProxyTest {
    public static void main(String[] args) {

//        AdminCglibService target = new AdminCglibService();
//        AdminServiceCglibProxy proxyFactory = new AdminServiceCglibProxy(target);
//        AdminCglibService proxy = (AdminCglibService)proxyFactory.getProxyInstance();
//
//        log.info("代理对象：" + proxy.getClass());
//
//        Object obj = proxy.find();
//        log.info("find 返回对象：" + obj.getClass());
//        log.info("----------------------------------");
//        proxy.update();

        int i = 0;
        aow(i++);
        System.out.println(i);
        String temp = "123";
        temp.concat("456");
        System.out.println(temp);
        if("abc" instanceof Object){
            System.out.println("abc");
        }
        if(null instanceof Object){
            System.out.println("null");
        }
    }

    private static void aow(int i){
        System.out.println(" i = " + i);
        i+=2;
    }
}