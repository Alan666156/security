package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 代理模式（Proxy）是通过代理对象访问目标对象，这样可以在目标对象基础上增强额外的功能，如添加权限，访问控制和审计等功能
 * 静态代理模式在不改变目标对象的前提下，实现了对目标对象的功能扩展。
 * 不足：静态代理实现了目标对象的所有方法，一旦目标接口增加方法，代理对象和目标对象都要进行相应的修改，增加维护成本。
 */
@Slf4j
public class StaticProxyTest {

    public static void main(String[] args) {
        AdminService adminService = new AdminServiceImpl();
        AdminServiceProxy proxy = new AdminServiceProxy(adminService);
        proxy.update();
        log.info("=============================");
        proxy.find();
    }
}