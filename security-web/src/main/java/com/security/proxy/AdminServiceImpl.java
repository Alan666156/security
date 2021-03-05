package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class AdminServiceImpl implements AdminService{
    @Override
    public void update() {
        System.out.println("修改管理系统数据");
    }

    @Override
    public Object find() {
        log.info("查看管理系统数据");
        return new Object();
    }
}