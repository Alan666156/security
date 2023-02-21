package com.security.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.security.common.SecurityConstants;
import com.security.dto.RedPacketDto;
import com.security.dto.RobRedPacketDto;
import com.security.util.RedPacketUtil;
import com.security.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 抢红包service
 *
 * @author fuhongxing
 * @date 2021/4/5 13:44
 */
@Slf4j
@Service
public class RedPacketService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedService redService;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发红包
     *
     * @throws Exception
     */
    public String handOut(RedPacketDto dto) throws Exception {
        // 红包个数和金额必须大于0
        if (dto.getQuantity() > 0 && dto.getAmount() > 0) {
            //生成随机金额, 二倍均值法计算
            List<Integer> list = RedPacketUtil.divideRedPackage2(dto.getAmount(), dto.getQuantity());
            log.info("开始发红包啦！[{}]生成随机红包金额列表:{}", dto.getUserId(), list);
            //生成红包全局唯一标识,并将随机金额、个数入缓存
            String redId = StrUtil.format(SecurityConstants.RED_PACKET_SEND, dto.getUserId(), IdUtil.fastUUID());
            //使用redis中的list，所以取走一个少一个，不会存在多人拿到同一个的情况，所以可以忽略超发问题
            redisUtil.leftPushAll(redId, list);
            //红包个数,临时缓存,也可以通过redisUtil.size(dto.getRedId())获取队列个数;
//            redisUtil.set(redId + ":total", dto.getQuantity(), 30L, TimeUnit.MINUTES);

            //异步记录红包发出的记录-包括个数与随机金额
            redService.recordRedPacket(dto, redId, list);
            //生成一个抢红包的缓存集合，存放抢红包的用户id和红包金额，默认24小时过期
            RMap<Long, Object> rMap = redissonClient.getMap(redId + "rob");
            return redId;
        } else {
            throw new Exception("系统异常-分发红包-参数不合法!");
        }
    }

    /**
     * 抢红包---> 分 “点” 与 “抢” 处理逻辑
     * 主要是用于从缓存系统的红包随机金额队列中弹出一个随机金额，如果金额不为空，则表示该用户抢到红包了，缓存系统中红包个数减1，同时异步记录用户抢红包的记录并结束流程；如果金额为空，则意味着用户来晚一步，红包已经被抢完了。
     */
    public BigDecimal rob(RobRedPacketDto dto) {
        // 用户是否抢过该红包；查询redis缓存，减少数据库交互，可以设置过期时间
        RMap<Long, BigDecimal> rMap = redissonClient.getMap(dto.getRedId() + ":rob");
        rMap.expire(1, TimeUnit.DAYS);
        if (rMap.containsKey(dto.getUserId())) {
            log.info("该用户[{}]已获取过红包", dto.getUserId());
            return rMap.get(dto.getUserId());
        }
        // 此处需要验证一下红包的真实性，如果缓存中为匹配到，还需到数据库去核对一下

        //点红包
        if (!click(dto.getRedId())) {
            return null;
        }
        //一个红包每个人只能抢一次随机金额；一个人每次只能抢到红包的一次随机金额  即要永远保证 1对1 的关系
        RLock lock = redissonClient.getLock(dto.getRedId() + ":lock");
        try {
            if (lock.tryLock(1, 10, TimeUnit.SECONDS)) {
                // 方案一：通过队列长度获取剩余的红包个数
                Long currentTotal = redisUtil.size(dto.getRedId());
                // 方案二：通过decr的方式扣减；
//                String redTotalKey = dto.getRedId() + ":total";
//                Object obj = redisUtil.get(redTotalKey);
//                int currentTotal = Objects.nonNull(obj) ? (Integer) obj : 0;
//                redisUtil.decr(redTotalKey);

                if (currentTotal < 0) {
                    log.warn("{}红包数量不够", dto.getRedId());
                    return null;
                }
                // 抢红包，成功红包个数减1，如果返回null说明已经抢完
                Object robRedPacketResult = redisUtil.rightPop(dto.getRedId());
                if (Objects.nonNull(robRedPacketResult)) {
                    //将红包金额返回给用户的同时，将抢红包记录入数据库与缓存
                    BigDecimal result = new BigDecimal(robRedPacketResult.toString()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    //数据库入库
                    redService.recordRobRedPacket(dto.getUserId(), dto.getRedId(), new BigDecimal(robRedPacketResult.toString()));
                    //写入抢红包缓存
                    rMap.put(dto.getUserId(), result);
                    log.info("当前用户[{}]抢到红包了：key={}, 金额={} ", dto.getUserId(), dto.getRedId(), result);
                    return result;
                } else {
                    log.warn("很遗憾，红包已抢完！");
                }
            }
        } catch (Exception e) {
            log.error("抢红包异常", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    /**
     * 点红包-返回true，则代表红包还未抢完 && 个数 > 0
     * 主要用于判断缓存系统中红包个数是否大于0。如果小于等于0，则意味着红包被抢完了；如果红包个数大于0，则表示缓存中还有红包，可以继续抢
     *
     * @return 红包是否已抢完
     */
    private Boolean click(String redId) {
        Long size = redisUtil.size(redId);
        if (size != null && size > 0) {
            return true;
        }
        log.warn("{}红包已抢完，请下次参与", redId);
        return false;
    }
}
