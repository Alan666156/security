package com.security.aop;

import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * AOP通过redis进行限流操作
 * 限流方案：1、google guava只能做单台应用服务
 * 	        2、redis + lua分布式限流处理
 * 	        3、Spring AOP限流，切面进行拦截处理
 */
@Slf4j
@Component
@Aspect
public class RedisLuaLimiterHandler {
    @Autowired
    private RedissonClient redissonClient;
    private DefaultRedisScript<Long> defaultRedisScript;
    @PostConstruct
    public void init(){
        defaultRedisScript = new DefaultRedisScript<>();
        //设置lua脚本返回值类型
        defaultRedisScript.setResultType(Long.class);
        //加载lua脚本
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimit.lua")));
    }

    /**
     *
     * @description 切点
     * @author fuhongxing
     */
    @Pointcut("@annotation(com.security.annotation.RateLimitLua)")
    public void rateLimitLua() {

    }


    /**
     *
     * @description 用户操作日志通知，围绕方法会记录结果
     * @param jp 连接点
     * @throws Throwable
     */
    @Around("rateLimitLua()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        Object result = null;
        try {
            result = jp.proceed();
            //限流的key
            String limitKey ="limitKey";
            //限制次数
            long limit = 10;
            //限制时间
            long expire = 60;
            //执行lua
            Long res = redissonClient.getScript(IntegerCodec.INSTANCE).eval(RScript.Mode.READ_WRITE, defaultRedisScript.getScriptAsString(), RScript.ReturnType.INTEGER, Arrays.asList(limitKey), limit, expire);
            if(res == 0){
                log.warn("请求过于频繁，请稍后再试");
                return Result.failure("请求过于频繁，请稍后再试");
            }
        } catch (Exception e) {
            log.error("aop用户操作日志记录获取方法返回值异常:{}", e.getMessage());
            throw e;
        } finally {

        }
        return result;
    }
}
