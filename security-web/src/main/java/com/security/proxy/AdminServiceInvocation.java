package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk动态代理
 */
@Slf4j
public class AdminServiceInvocation  implements InvocationHandler {

    private Object target;

    public AdminServiceInvocation(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("判断用户是否有权限进行操作");
        Object obj = method.invoke(target);
        log.info("记录用户执行操作的用户信息、更改内容和时间等");
        return obj;
    }
}