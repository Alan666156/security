package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 静态代理
 * @author fuhongxing
 */
@Slf4j
public class AdminServiceProxy implements AdminService {

    private AdminService adminService;

    public AdminServiceProxy(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void update() {
        log.info("判断用户是否有权限进行update操作");
        adminService.update();
        log.info("记录用户执行update操作的用户信息、更改内容和时间等");
    }

    @Override
    public Object find() {
        log.info("判断用户是否有权限进行find操作");
        log.info("记录用户执行find操作的用户信息、查看内容和时间等");
        return adminService.find();
    }
}