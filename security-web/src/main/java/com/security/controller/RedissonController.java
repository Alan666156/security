package com.security.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.security.annotation.RateLimitLua;
import com.security.annotation.UseLog;
import com.security.common.SecurityConstants;
import com.security.service.OauthCodeService;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.MapEntryListener;
import org.redisson.client.codec.Codec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author fuongxing
 */
@Slf4j
@RestController
@RequestMapping("redisson")
public class RedissonController {
	
	@Autowired
	private RedissonClient redissonClient;
	@PostConstruct
	public void init(){
		RMapCache<String, Object> rMapCache = redissonClient.getMapCache(SecurityConstants.REDIS_KEY_MAP_PRE);
		/**
		 * 过期事件处理，比如订单支付超期取消，可以通过该功能实现，过期事件不一定及时触发，可能存在延时
		 */
		rMapCache.addListener((EntryExpiredListener<String, Object>) event -> {
			log.info("超期事件处理，{}已过期，旧值：{}，当前值：{}", event.getKey(), event.getOldValue(), event.getValue());
		});
		log.info("初始化rMapCache={}", SecurityConstants.REDIS_KEY_MAP_PRE);
	}

	/**
	 * 限流方案：1、google guava只能做单台应用服务
	 * 			2、redis + lua分布式限流处理
	 * 			3、Spring AOP限流，切面进行拦截处理
	 * @param request
	 * @return
	 */
	@GetMapping("/home")
	public Result home(HttpServletRequest request){
		RLock lock = redissonClient.getLock(StrUtil.format(SecurityConstants.REDIS_KEY_PRE, "test001", "user"));
		try {
			String ip = getIP(request);
			if (!lock.tryLock(0, 30, TimeUnit.SECONDS)) {
				log.info("并发锁，加入黑名单5分钟，ip:{}, coupon_code:{}, customerId:{}", ip, "test001", "user");
				redissonClient.getBucket(StrUtil.format(SecurityConstants.REDIS_KEY_BUCKET_USER_ID, "user")).set("1", 5, TimeUnit.MINUTES);
				return Result.failure("失败");
			}
			/**
			         * 基于Redis的分布式限流器可以用来在分布式环境下现在请求方的调用频率。
			         * 既适用于不同Redisson实例下的多线程限流，也适用于相同Redisson实例下的多线程限流。
			         * 该算法不保证公平性。
			         */
			RRateLimiter rRateLimiter = redissonClient.getRateLimiter(StrUtil.format(SecurityConstants.REDIS_KEY_PRE, "test001", "user"));
			//作用在同一个Redisson实例创建的 RRateLimiter上面，每秒10个库存
			rRateLimiter.trySetRate(RateType.PER_CLIENT, 10, 1, RateIntervalUnit.SECONDS);
			return Result.success("success");
		} catch (Exception e) {
			log.error("执行异常", e);
			return Result.failure("请求失败，请稍后再试");
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	/**
	 * lua + redis 限流
	 * @param model
	 * @param name
	 * @return
	 */
	@RateLimitLua()
	@GetMapping("/test-rateLimitLua")
	public Result rateLimitLua(Model model, String name){
		System.out.println(name);
		log.info("==========api===========");
		return Result.success("success");
	}

	@PostMapping("test")
	public Result test(){
		log.info("==========redisson 使用===========");
		RMap<String, Object> rMap = redissonClient.getMap("sec:map");
		RMapCache<String, Object> rMapCache = redissonClient.getMapCache(SecurityConstants.REDIS_KEY_MAP_PRE);
		//2秒后过期
		rMapCache.put("test", "下单", 2, TimeUnit.SECONDS);


		//数据唯一性或进行排序的场景, 不能设置有效期
		RSet<String> rSet = redissonClient.getSet("sec:rSet");
		//可以设置元素过期，但是不能触发过期事件，可以实现排行榜
		RScoredSortedSet rScoredSortedSet = redissonClient.getScoredSortedSet("score");
//		rScoredSortedSet.addListener((ExpiredObjectListener) name -> {
//			log.info("超期事件处理，不会触发{}", name);
//		});

		rScoredSortedSet.addScore("A",90);
		rScoredSortedSet.addScore("B",100);
		rScoredSortedSet.addScore("C",80);
		//设置过期
		if(!rScoredSortedSet.isExists()){
			rScoredSortedSet.expire(60, TimeUnit.SECONDS);
		}
		//获取元素在集合中的位置
		Integer index = rScoredSortedSet.revRank("C");
		//获取分数
		Double score = rScoredSortedSet.getScore("C");

		rScoredSortedSet.stream().forEach(e ->{
			log.info("index:{}, score:{}", rScoredSortedSet.revRank(e), rScoredSortedSet.getScore(e));
		});

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
