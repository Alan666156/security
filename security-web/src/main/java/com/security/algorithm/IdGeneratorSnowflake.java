package com.security.algorithm;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Twitter snowflake 雪花算法
 * @author fuhongxing
 * @date 2021/3/21 14:26
 */
@Data
public class IdGeneratorSnowflake {

    private static Snowflake snowflake = null;

    /**
     * 手动指定workId相关信息
     * @param workerId
     * @param datacenterId
     */
    public void idGenerator(long workerId, long datacenterId){
        if(snowflake == null){
            Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        }
        snowflake.nextId();
    }

    public static void main(String[] args) {
        SnowflakeTest snowflakeTest = new SnowflakeTest();
        snowflake = IdUtil.createSnowflake(snowflakeTest.getWorkerId(), snowflakeTest.getDatacenterId());

        IdGeneratorSnowflake idGeneratorSnowflake = new IdGeneratorSnowflake();
        System.out.println("当前机器的workId:" + NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()));

        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            service.execute(() -> {
                // ... do something inside runnable task
                System.out.println(Thread.currentThread().getName() + " id info：" + snowflake.nextId());
            });
        }

        service.shutdown();
    }
    @Data
    static class SnowflakeTest{
        /**下面两个每个5位，加起来就是10位的工作机器id*/
        /**工作id(0 - 31之间)*/
        private long workerId = 0;
        /**数据中心id*/
        private long datacenterId = 1;
        /**12位的序列号*/
        private long sequence;
    }
}
