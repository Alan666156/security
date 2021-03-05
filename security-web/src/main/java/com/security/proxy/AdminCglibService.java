package com.security.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminCglibService {
    public void update() {
        log.info("修改管理系统数据");
    }

    public Object find() {
        log.info("查看管理系统数据");
        return new Object();
    }
}