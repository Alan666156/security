package com.security.pdf;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.security.SecurityApplication;
import com.security.dto.RedPacketDto;
import com.security.dto.RobRedPacketDto;
import com.security.service.RedPacketService;
import com.security.util.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = SecurityApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedPacketTest {
    @Autowired
    private RedPacketService redPacketService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 二倍均值法自测
     *
     * @throws Exception
     */
    @Test
    public void one() throws Exception {
        //总金额单位为分
        Integer amout = 1000;
        //总人数-红包个数
        Integer total = 10;
        //得到随机金额列表
        List<Integer> list = RedPacketUtil.divideRedPackage2(amout, total);
        log.info("总金额={}分，总个数={}个", amout, total);

        //用于统计生成的随机金额之和是否等于总金额
        Integer sum = 0;
        //遍历输出每个随机金额
        for (Integer i : list) {
            log.info("随机金额为：{}分，即 {}元", i, new BigDecimal(i.toString()).divide(new BigDecimal(100)));
            sum += i;
        }
        log.info("所有随机金额叠加之和={}分", sum);
    }

    /**
     * 发红包
     *
     * @throws Exception
     */
    @Test
    public void send() throws Exception {
        log.info("=========发红包=========");
        RedPacketDto dto = new RedPacketDto();
//        log.info("request: {}", redPacketService.handOut(dto));

        List<Long> userList = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 1797L, 1788L, 1621L, 1325L, 1808L, 1311L, 1645L, 1419L, 1694L);
        final long start = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Long userId : userList) {
            // 多线程无返回结果
            final CompletableFuture<Void> async = CompletableFuture.runAsync(() -> {
                try {
                    redPacketService.handOut(new RedPacketDto(userId, 10, RandomUtil.randomInt(10, 2000)));
                } catch (Exception e) {
                    log.error("发红包异常！", e);
                }
            }, threadPoolTaskExecutor);
            futures.add(async);
        }
        final CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        all.get();
        long interval = System.currentTimeMillis() - start;
        log.info(" {} 个人发红包总耗时 {}", userList.size(), interval);
    }

    /**
     * 抢红包
     *
     * @throws Exception
     */
    @Test
    public void rob() throws Exception {
        log.info("=========抢红包=========");
//        for (long i = 0; i < 20; i++) {
//            long temp = i;
//            new Thread(() -> {
//                try {
//                    log.info("第{}个人抢红包开始，红包金额：{}", temp, redPacketService.rob(temp, "red:packet:24:6f6a6f64-ec46-4770-b2c7-4b826f29c667"));
//                } catch (Exception e) {
//                    log.error("抢异常", e);
//                }
//            }, String.valueOf(i)).start();
//            TimeUnit.SECONDS.sleep(1);
//        }

        // 接收处理结果
        ConcurrentSkipListMap<Long, CompletableFuture<BigDecimal>> skipListMap = new ConcurrentSkipListMap<>();
        for (int i = 1; i < 150; i++) {
            Long temp = (long) i;
            skipListMap.put(temp, CompletableFuture.supplyAsync(() -> redPacketService.rob(new RobRedPacketDto(temp, "red:packet:1:1b9c9dd1-25ab-43d1-87bf-c73e422877a8")), threadPoolTaskExecutor));
        }
        // 等待所有处理结束
        CompletableFuture.allOf(skipListMap.values().toArray(new CompletableFuture[0])).join();
        skipListMap.forEach((k, v) -> {
            try {
                log.info("抢红包处理结果-->k: " + k + ", v : " + (Objects.isNull(v) ? null : v.get()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

}