package com.security.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.security.annotation.UseLog;
import com.security.common.SecurityConstants;
import com.security.service.OauthCodeService;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class IndexController {
	
	@Autowired
	private OauthCodeService oauthCodeService;
	

    //生成一個令牌桶，同時每秒放入2个令牌
    final RateLimiter limiter = RateLimiter.create(2);
	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 限流方案：1、google guava只能做单台应用服务
	 * 			2、redis + lua分布式限流处理
	 * 			3、Spring AOP限流，切面进行拦截处理
	 * @param model
	 * @return
	 */
	@GetMapping("/home")
	@UseLog(remark = "test")
	public Result home(Model model, HttpServletRequest request){
		RLock lock = redissonClient.getLock(StrUtil.format(SecurityConstants.REDIS_KEY_PRE, "test001", "user"));
		try {
			String ip = getIP(request);
			if (!lock.tryLock(0, 30, TimeUnit.SECONDS)) {
				log.info("并发锁，加入黑名单5分钟，ip:{}, coupon_code:{}, customerId:{}", ip, "test001", "user");
				redissonClient.getBucket(StrUtil.format(SecurityConstants.REDIS_KEY_BUCKET_USER_ID, "user")).set("1", 5, TimeUnit.MINUTES);
				return Result.failure("失败");
			}
		} catch (Exception e) {
			log.error("执行异常", e);
			return Result.failure("请求失败，请稍后再试");
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}

		//判断是否拿到令牌
		if (limiter.tryAcquire()){
			log.info("==========请求成功===========");
		}else {
			log.info("==========请求过于频繁，请稍后再试===========");
		}
		return Result.success("success");
	}

	@GetMapping("/api")
	public Result api(Model model, String name){
		System.out.println(name);
		log.info("==========api===========");
		return Result.success("success");
	}

	@UseLog(remark="useLog test login")
	@PostMapping("login")
	public Result login(Model model){
		log.info("==========login===========");
		return Result.success("success");
	}

	private String getIP(HttpServletRequest request) {
		String ip = null;
		if (request != null) {
			ip = request.getHeader("X-Forwarded-For");
			if (StringUtils.hasText(ip)) {
				String[] p = ip.split(",");
				if (p.length > 0) {
					ip = p[0];
				}
			}
			if (!StringUtils.hasText(ip)) {
				ip = request.getHeader("RemoteIp");
			}
			if (!StringUtils.hasText(ip) && request.getRemoteAddr() != null) {
				ip = request.getRemoteAddr();
			}
		}
		return ip;
	}



}
