package com.security.controller;

import cn.hutool.core.util.StrUtil;
import com.security.annotation.RateLimitLua;
import com.security.common.SecurityConstants;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.map.event.EntryExpiredListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * redisson使用
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
		log.info("初始化redisson rMapCache={}", SecurityConstants.REDIS_KEY_MAP_PRE);

		RMapCache<String, Object> rMapCache = redissonClient.getMapCache(SecurityConstants.REDIS_KEY_MAP_PRE);
		/**
		 * 过期事件处理，比如订单支付超期取消，可以通过该功能实现，过期事件不一定及时触发，可能存在延时
		 */
		rMapCache.addListener((EntryExpiredListener<String, Object>) event -> {
			log.info("超期事件处理，{}已过期，旧值：{}，当前值：{}", event.getKey(), event.getOldValue(), event.getValue());
		});

		//订阅指定话题
		RTopic rTopic = redissonClient.getTopic("myTopic");
		//指定表达式订阅多个话题
//		RPatternTopic rPatternTopic = redissonClient.getPatternTopic("*myTopic*");
		rTopic.addListener(String.class, (channel, msg) -> {
			log.info("接收到消息：{}", msg);
		});
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
	 * @returnv
	 */
	@RateLimitLua()
	@GetMapping("/test-rateLimitLua")
	public Result rateLimitLua(Model model, String name){
		System.out.println(name);
		log.info("==========lua + redis 限流, 注解实现===========");
		return Result.success("success");
	}

	/**
	 * 集合的使用
	 * @return
	 */
	@GetMapping("test")
	public Result test() {
		log.info("==========redisson 集合使用===========");
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

	@GetMapping("queue")
	public Result queue() throws InterruptedException {
		log.info("==========redisson 队列使用===========");
		//队列
		RQueue<String> rQueue = redissonClient.getQueue("sec:queue");
		//延迟队列，数据临时存放，发出后删除该元素, 60秒后往目标队列rQueue发送
		RDelayedQueue<String> rDelayedQueue = redissonClient.getDelayedQueue(rQueue);
		rDelayedQueue.offer("A", 60, TimeUnit.SECONDS);
		rDelayedQueue.readAll();

		//阻塞式无界队列
		RBlockingDeque<String> rBlockingDeque = redissonClient.getBlockingDeque("sec:block:queue");
		//从队列的头部获取一个元素，并将队列中该元素删除，队列为空时返回null
		rBlockingDeque.poll();
		//从队列的头部获取一个元素，并将队列中该元素删除，队列为空时返回阻塞线程
		rBlockingDeque.take();
		//从队列的头部获取一个元素，但不会将该元素从队列中删除，队列为空时返回null
		rBlockingDeque.peek();

		return Result.success("success");
	}

	@GetMapping("await")
	public Result awaitThread() {
		log.info("==========redisson 队列使用===========");
		try {
			RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch("myCountDownLatch");
			//计数器初始大小
			rCountDownLatch.trySetCount(3);
			//阻塞线程知道计算器归零后唤醒
			rCountDownLatch.await();
			log.info("所有子线程运行完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success("success");
	}

	@GetMapping("thread")
	public Result thread() {
		log.info("==========redisson RCountDownLatch使用:{}===========",Thread.currentThread().getName());
		try {
			RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch("myCountDownLatch");
			TimeUnit.SECONDS.sleep(5);
			//计数器减1，当计数器归零后通知所有等待的线程恢复执行
			rCountDownLatch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Result.success("success");
	}

	@GetMapping("produce")
	public Result produce() {
		log.info("==========redisson RTopic使用===========");
		RTopic rTopic = redissonClient.getTopic("myTopic");
		rTopic.publish("redisson message produce");
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
