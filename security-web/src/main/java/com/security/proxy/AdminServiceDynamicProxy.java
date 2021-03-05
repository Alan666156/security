package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理
 * 1.Proxy对象不需要implements接口；
 * 2.Proxy对象的生成利用JDK的Api，在JVM内存中动态的构建Proxy对象。需要使用java.lang.reflect.Proxy类的
 */
@Slf4j
public class AdminServiceDynamicProxy {
    private Object target;
    private InvocationHandler invocationHandler;

    public AdminServiceDynamicProxy(Object target,InvocationHandler invocationHandler){
        this.target = target;
        this.invocationHandler = invocationHandler;
    }

    public Object getPersonProxy() {
        Object obj = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), invocationHandler);
        return obj;
    }
}