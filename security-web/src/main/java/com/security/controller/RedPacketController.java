package com.security.controller;

import cn.hutool.core.util.StrUtil;
import com.security.common.SecurityConstants;
import com.security.dto.RedPacketDto;
import com.security.service.RedPacketService;
import com.security.util.RedisUtil;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 抢红包
 *
 * @author: fuhx
 */
@Slf4j
@RestController
@RequestMapping("red/packet")
public class RedPacketController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedPacketService redPacketService;

    /**
     * 发红包
     */
    @PostMapping("/send")
    public Result handOut(@RequestBody RedPacketDto dto) {
        //此处省略验证用户，正常情况用户进入发红包页面说明都是已经登录成功的用户
        //1、先验证用户余额是否足够

        Result response = Result.success();
        //同一时间用户只能成功发一个红包，涉及钱包不存在多平台登录的情况可以忽略
        RLock lock = redissonClient.getLock(StrUtil.format(SecurityConstants.RED_PACKET_USER, dto.getUserId()));
        try {
            if (lock.tryLock()) {
                String redId = redPacketService.handOut(dto);
                response.setData(redId);
            }
        } catch (Exception e) {
            log.error("{}发红包异常", dto.getUserId(), e);
            response = Result.failure("发红包异常");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return response;
    }


    /**
     * 抢红包
     */
    @GetMapping("/rob")
    public Result rob(@RequestParam Long userId, @RequestParam String redId) {
        Result response = Result.success();
        try {
            BigDecimal result = redPacketService.rob(userId, redId);
            if (result != null) {
                response.setData(result);
            } else {
                response = Result.failure("红包已被抢完!");
            }
        } catch (Exception e) {
            log.error("抢红包发生异常：userId={} redId={}", userId, redId, e.fillInStackTrace());
            response = Result.failure("获取红包异常");
        }
        return response;
    }

}