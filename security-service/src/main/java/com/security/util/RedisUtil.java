package com.security.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 */
@Slf4j
@Service
public class RedisUtil {

    @Resource
    private RedisTemplate<Serializable, Object> redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        log.info("批量删除...");
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 删除所有value
     */
    public void removeAll() {
        log.info("清空redis");
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        Object                                result     = operations.get(key);
        return result;
    }


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("redis缓存写入异常...", e);
        }

        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 有效时间
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("redis缓存写入异常...", e);
        }

        return result;
    }

    /**
     * 增加值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(String key) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key);
        } catch (Exception e) {
            log.error("redis缓存增加值异常...", e);
        }
        return null;
    }

    /**
     * 增加值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(String key, Long quantity) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            return operations.increment(key, quantity);
        } catch (Exception e) {
            log.error("redis缓存增加值异常...", e);
        }
        return null;
    }

    /**
     * 减少值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(String key) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            return operations.decrement(key);
        } catch (Exception e) {
            log.error("redis缓存减少值异常...", e);
        }
        return null;
    }

    /**
     * 减少指定值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(String key, Long quantity) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            return operations.decrement(key, quantity);
        } catch (Exception e) {
            log.error("redis缓存减少值异常...", e);
        }
        return null;
    }

    /**
     * 写入hash
     *
     * @param key
     * @param map
     */
    public void setHash(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 写入hash value值
     *
     * @param key
     * @param field
     * @param value
     */
    public void setHashValue(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 写入hash
     *
     * @param key
     * @param map
     */
    public void setHash(String key, Map<String, Object> map, Date expire) {
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, DateUtil.between(new Date(), expire, DateUnit.SECOND), TimeUnit.SECONDS);
    }

    /**
     * 读取hash值
     *
     * @param key
     * @return
     */
    public Map getHash(String key) {
        System.out.println(redisTemplate.getExpire(key));
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 读取hash属性值
     *
     * @param key
     * @param field
     * @return
     */
    public Object getHashFieldValue(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * hash value 新增
     *
     * @param key   hash key
     * @param field hash field
     * @param value add value
     * @return
     */
    public long hincrby(String key, String field, long value) {
        return redisTemplate.opsForHash().increment(key, field, value);
    }

    /**
     * set写入
     *
     * @param key
     * @param value
     */
    public void sSet(String key, Object... value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    /**
     * 无序集合，随机移除一个元素
     *
     * @param key
     */
    public Object sPop(String key)
    {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * set 根据给定的元素，去移除
     *
     * @param key
     * @param values
     */
    public Long sRemove(String key, String...values)
    {
        return redisTemplate.opsForSet().remove(key, (Object[])values);
    }

    /**
     * set 交集
     * 找出两个集合的相同部分
     *
     * @param key
     * @param otherKey
     * @return
     */
    public Set<Object> intersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 找出两个集合的并集
     *
     * @param key
     * @param otherKey
     */
    public Set<Object> union(String key, String otherKey)
    {
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 找出两个集合不同的部分
     *
     * @param key
     * @param otherKey
     */
    public Set<Object> diff(String key, String otherKey)
    {
        return redisTemplate.opsForSet().difference(key,otherKey);
    }

    /**
     * 找出两个集合不同的部分，并存到destKey集合中
     *
     * @param destKey
     * @param key
     * @param otherKey
     */
    public Long diffStore(String destKey, String key, String otherKey)
    {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    /**
     * 找出两个集合的并集，并存放到新集合destKey中
     *
     * @param destKey
     * @param key
     * @param otherKey
     */
    public Long unionStore(String destKey, String key, String otherKey)
    {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }
    /**
     * 找出两个集合的相同部分，存到新集合destKey中
     *
     * @param destKey 存储目标新集合
     * @param key
     * @param otherKey
     */
    public Long interStore(String destKey, String key, String otherKey)
    {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }
    /**
     * 查询set值
     *
     * @param key
     * @return
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.boundSetOps(key).members();
    }

    /**
     * 缓存list
     *
     * @param key
     * @param list
     */
    public void setList(String key, Collection<Object> list) {
        redisTemplate.opsForList().leftPushAll(key, list);
    }

    /**
     * 移除并返回集合中左边的元素
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object leftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除集合中的左边第一个元素
     * @param key
     * @return
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，如果中间参数值存在的话。否则返回-1
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object leftPush(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPush(key, timeout, unit);
    }

    /**
     * 在变量左边添加元素值
     * @param key
     * @param value
     * @return
     */
    public Object leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }
    /**
     * 移除集合中的右边第一个元素
     * @param key
     * @return
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }
    /**
     * 移除并返回集合中左边的元素
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object rightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，如果中间参数值存在的话。否则返回-1
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object rightPush(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPush(key, timeout, unit);
    }
    public Object rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }



    /**
     * 重置key到期时间
     *
     * @param key
     * @param expire
     */
    public void expire(String key, Date expire) {
        redisTemplate.expire(key, DateUtil.between(new Date(), expire, DateUnit.SECOND), TimeUnit.SECONDS);
    }

    public void zadd(String key, double score, Object value) {
        redisTemplate.boundZSetOps(key).add(value, score);
    }

    public Set<Object> rangeByScore(String key, double minScore, double maxScore, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore, (offset - 1) * count, count);
    }

    public Long zrem(String key, Object value) {
        return redisTemplate.boundZSetOps(key).remove(value);
    }

    /**
     * 利用Redis的原子性去实现幂等性
     * 在接收到消息后将消息ID作为key执行 setnx命令，如果执行成功就表示没有消费过这条消息，可以进行消费了，执行失败表示消息已经被消费了
     * setnx如果key存在不错任何操作，如果不存在就赋值
     * 在setnx和setex中间发生了服务down机 那么key将没有超时时间 会一直存在，新的请求永远进不来
     * @param key
     * @param value
     * @param expire 有效期
     */
    public void setNx(String key, String value, Long expire) {
        try {
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, value);
            if(!lock){
                ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
                log.warn("key have exist belong to : {}", operations.get(key));
            }else{
                redisTemplate.opsForValue().set(key, value,expire, TimeUnit.SECONDS);
                //获取锁成功
//                log.info("start lock lockNxExJob success");
            }
        }catch (Exception e){
            log.error("{} setNx error!", key, e);
        }finally {
            redisTemplate.delete(key);
        }
    }

    /**
     * lua脚本执行
     * @param luaScript
     */
    public void setNx(String luaScript) {
        try {
            DefaultRedisScript<Serializable> script = new DefaultRedisScript<Serializable>();
            script.setScriptText(luaScript);
            script.setResultType(Serializable.class);
            // 封装参数
            List<Serializable> keyList = new ArrayList<Serializable>();
            redisTemplate.execute(script, keyList);
        }catch (Exception e){
            log.error("setNx luaScript error!", e);
        }finally {

        }
    }
}
