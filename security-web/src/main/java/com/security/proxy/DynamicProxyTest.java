package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理
 * 1.Proxy对象不需要implements接口；
 * 2.Proxy对象的生成利用JDK的Api，在JVM内存中动态的构建Proxy对象。需要使用java.lang.reflect.Proxy类的
 */
@Slf4j
public class DynamicProxyTest {
    public static void main(String[] args) {
        // 方法一
        System.out.println("============ 方法一 ==============");
        AdminService adminService = new AdminServiceImpl();
        System.out.println("代理的目标对象：" + adminService.getClass());

        AdminServiceInvocation adminServiceInvocation = new AdminServiceInvocation(adminService);

        AdminService proxy = (AdminService) new AdminServiceDynamicProxy(adminService, adminServiceInvocation).getPersonProxy();

        log.info("代理对象：" + proxy.getClass());

        Object obj = proxy.find();
        log.info("find 返回对象：" + obj.getClass());
        log.info("----------------------------------");
        proxy.update();

        //方法二
        log.info("============ 方法二 ==============");
        AdminService target = new AdminServiceImpl();
        AdminServiceInvocation invocation = new AdminServiceInvocation(adminService);
        AdminService proxy2 = (AdminService) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), invocation);

        Object obj2 = proxy2.find();
        log.info("find 返回对象：" + obj2.getClass());
        log.info("----------------------------------");
        proxy2.update();

        //方法三
        log.info("============ 方法三 ==============");
        final AdminService target3 = new AdminServiceImpl();
        AdminService proxy3 = (AdminService) Proxy.newProxyInstance(target3.getClass().getClassLoader(), target3.getClass().getInterfaces(), new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("判断用户是否有权限进行操作");
                Object obj = method.invoke(target3, args);
                log.info("记录用户执行操作的用户信息、更改内容和时间等");
                return obj;
            }
        });

        Object obj3 = proxy3.find();
        log.info("find 返回对象：" + obj3.getClass());
        log.info("----------------------------------");
        proxy3.update();


    }
}