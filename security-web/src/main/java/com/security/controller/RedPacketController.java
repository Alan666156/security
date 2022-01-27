package com.security.controller;

import com.security.dto.RedPacketDto;
import com.security.service.RedPacketService;
import com.security.util.RedisUtil;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 抢红包
 *
 * @author: fuhx
 */
@Slf4j
@RequestMapping("red/packet")
@RestController
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
    @PostMapping(value = "/send")
    public Result handOut(@RequestBody RedPacketDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return Result.failure("StatusCode.InvalidParams");
        }
        Result response = Result.success();
        try {
            String redId = redPacketService.handOut(dto);
            response.setData(redId);

        } catch (Exception e) {
            log.error("发红包发生异常：dto={} ", dto, e.fillInStackTrace());
            response = Result.failure("获取红包异常");
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