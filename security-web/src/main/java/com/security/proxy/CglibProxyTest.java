package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CglibProxyTest {
    public static void main(String[] args) {

        AdminCglibService target = new AdminCglibService();
        AdminServiceCglibProxy proxyFactory = new AdminServiceCglibProxy(target);
        AdminCglibService proxy = (AdminCglibService)proxyFactory.getProxyInstance();

        log.info("代理对象：" + proxy.getClass());

        Object obj = proxy.find();
        log.info("find 返回对象：" + obj.getClass());
        log.info("----------------------------------");
        proxy.update();
    }
}