package com.security.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.security.common.SecurityConstants;
import com.security.domain.User;
import com.security.dto.RedPacketDto;
import com.security.dto.RobRedPacketDto;
import com.security.service.RedPacketService;
import com.security.service.UserService;
import com.security.util.RedisUtil;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

/**
 * 抢红包
 *
 * @author: fuhx
 */
@Slf4j
@RestController
@RequestMapping("red/packet")
public class RedPacketController {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedPacketService redPacketService;
    @Resource
    private UserService userService;
    private final Cache<Long, User> userCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).maximumSize(20).build();

    /**
     * 发红包
     */
    @PostMapping("/send")
    public Result handOut(@RequestBody RedPacketDto dto) {
        final long start = System.currentTimeMillis();
        //1、验证用户，正常情况用户进入发红包页面说明都是已经登录成功的用户
        User user = null;
        // 如果并发较高，临时缓存，同一用户减少数据库开销；一级缓存redis
        Object o = redisUtil.get(StrUtil.format(SecurityConstants.RED_PACKET_USER, dto.getUserId()));
        if (Objects.isNull(o)) {
            // 二级缓存Caffeine
            user = userCache.get(dto.getUserId(), userId -> {
                User userTemp = userService.findById(userId);
                // 写入一级缓存
                if (Objects.nonNull(userTemp)) {
                    redisUtil.set(StrUtil.format(SecurityConstants.RED_PACKET_USER, userId), JSON.toJSONString(userTemp));
                }
                return userTemp;
            });
        } else {
            user = JSON.parseObject(redisUtil.get(StrUtil.format(SecurityConstants.RED_PACKET_USER, dto.getUserId())).toString(), User.class);
        }
        if (Objects.isNull(user)) {
            log.warn("{}用户不存在！", dto.getUserId());
            return Result.failure(dto.getUserId() + "用户不存在！");
        }

        //2、先验证用户余额是否足够
        BigDecimal amount = BigDecimal.valueOf(dto.getAmount());
        if (user.getTotalAmount().compareTo(amount) < 0) {
            log.warn("{}用户余额不足！", dto.getUserId());
            return Result.failure(dto.getUserId() + "余额不足！");
        }

        Result response = null;
        //同一时间同一用户只能成功发一个红包，涉及钱包不存在多平台登录的情况可以忽略
        RLock lock = redissonClient.getLock(StrUtil.format(SecurityConstants.RED_PACKET_USER_LOCK, dto.getUserId()));
        try {
            if (lock.tryLock()) {
                String redId = redPacketService.handOut(dto);
                response = Result.success(redId);
            } else {
                response = Result.failure("并发请求！");
            }

        } catch (Exception e) {
            log.error("[{}]发红包异常", dto.getUserId(), e);
            response = Result.failure("发红包异常!");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        long interval = System.currentTimeMillis() - start;
        log.debug("[{}]发红包总耗时: {}", dto.getUserId(), interval);
        return response;
    }

    /**
     * 抢红包
     */
    @PostMapping("/test")
    public Result test(@RequestBody RedPacketDto dto) {
        log.info("test---->{}", Thread.currentThread().getName());
        return Result.success();
    }


    /**
     * 抢红包
     */
    @PostMapping("/rob")
    public Result rob(@RequestBody RobRedPacketDto dto) {
        Result response = null;
        try {
            BigDecimal result = redPacketService.rob(dto);
            if (result != null) {
                response = Result.success(result);
            } else {
                response = Result.failure("红包已被抢完!");
            }
        } catch (Exception e) {
            log.error("抢红包发生异常：userId={} redId={}", dto.getUserId(), dto.getRedId(), e.fillInStackTrace());
            response = Result.failure("网络异常！");
        }
        return response;
    }

}