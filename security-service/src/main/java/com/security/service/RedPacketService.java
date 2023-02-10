package com.security.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.security.common.SecurityConstants;
import com.security.dto.RedPacketDto;
import com.security.util.RedPacketUtil;
import com.security.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (dto.getTotal() > 0 && dto.getAmount() > 0) {
            //生成随机金额
            List<Integer> list = RedPacketUtil.divideRedPackage(dto.getAmount(), dto.getTotal());
            log.info("生成随机红包金额列表:{}", list);
            //生成红包全局唯一标识,并将随机金额、个数入缓存
            String redId = StrUtil.format(SecurityConstants.RED_PACKET_SEND, dto.getUserId(), IdUtil.fastUUID());
            //使用redis中的list，所以取走一个少一个，不会存在多人拿到同一个的情况，所以可以忽略超发问题
            redisUtil.leftPushAll(redId, list);
            //金额
            redisUtil.set(redId + ":total", dto.getTotal(), 30L, TimeUnit.MINUTES);
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
     *
     * @throws Exception
     */
    public BigDecimal rob(Long userId, String redId) throws Exception {
        //用户是否抢过该红包
        RMap<Long, Object> rMap = redissonClient.getMap(redId + "rob");
        if (rMap.containsKey(userId)) {
            log.info("该用户[{}]已获取过红包", userId);
            return new BigDecimal(rMap.get(userId).toString());
        }

        //点红包
        if (!click(redId)) {
            return null;
        }

        //一个红包每个人只能抢一次随机金额；一个人每次只能抢到红包的一次随机金额  即要永远保证 1对1 的关系
        RLock lock = redissonClient.getLock(redId + "lock");
        try {
            if (lock.tryLock()) {
                //抢红包，redisUtil.rightPop成功红包个数减1
                String redTotalKey = redId + ":total";
                int currTotal = Objects.nonNull(redisUtil.get(redTotalKey)) ? (Integer) redisUtil.get(redTotalKey) : 0;
                redisUtil.decr(redTotalKey);
                if (currTotal < 0) {
                    log.warn("{}红包数量不够", redId);
                    return null;
                }
                Object value = redisUtil.rightPop(redId);
                if (value != null) {
                    //将红包金额返回给用户的同时，将抢红包记录入数据库与缓存
                    BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                    //数据库入库
                    redService.recordRobRedPacket(userId, redId, new BigDecimal(value.toString()));
                    //写入抢红包缓存
                    rMap.put(userId, result);
                    log.info("当前用户[{}]抢到红包了：key={}, 金额={} ", userId, redId, result);
                    return result;
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
     * 点红包-返回true，则代表红包还有，个数>0
     * 主要用于判断缓存系统中红包个数是否大于0。如果小于等于0，则意味着红包被抢完了；如果红包个数大于0，则表示缓存中还有红包，可以继续抢
     */
    private Boolean click(String redId) {
        Object total = redisUtil.get(redId + ":total");
        if (total != null && Integer.parseInt(total.toString()) > 0) {
            return true;
        }
        log.warn("{}红包已抢完，请下次参与", redId);
        return false;
    }
}
