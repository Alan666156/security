package com.security.pdf;

import com.security.SecurityApplication;
import com.security.dto.RedPacketDto;
import com.security.service.RedPacketService;
import com.security.util.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = SecurityApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedPacketTest {
    @Autowired
    private RedPacketService redPacketService;

    /**
     * 二倍均值法自测
     * @throws Exception
     */
    @Test
    public void one() throws Exception{
        //总金额单位为分
        Integer amout = 1000;
        //总人数-红包个数
        Integer total = 10;
        //得到随机金额列表
        List<Integer> list = RedPacketUtil.divideRedPackage(amout, total);
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
     * @throws Exception
     */
    @Test
    public void send() throws Exception{
        log.info("=========发红包=========");
        RedPacketDto dto = new RedPacketDto();
        dto.setAmount(100);
        dto.setTotal(10);
        dto.setUserId(1001L);
        log.info("request: {}", redPacketService.handOut(dto));
    }

    /**
     * 抢红包
     * @throws Exception
     */
    @Test
    public void rob() throws Exception{
        log.info("=========抢红包=========");
        for (long i = 0; i < 20; i++) {
            long temp = i;
            new Thread(() -> {
                try {
                    log.info("第{}个人抢红包开始，红包金额：{}", temp, redPacketService.rob(temp, "red:packet:1001:1208637858279500"));
                }catch (Exception e){
                    log.error("抢异常", e);
                }
            }, String.valueOf(i)).start();
            TimeUnit.SECONDS.sleep(1);
        }
    }

}