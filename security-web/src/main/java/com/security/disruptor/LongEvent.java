package com.security.disruptor;


/**
 * Disruptor是一个异步并发处理框架。是由LMAX公司开发的一款高效的无锁内存队列。它使用无锁的方式实现了一个环形队列，非常适合于实现生产者和消费者模式，比如事件和消息的发布。
 * Disruptor最大特点是高性能，其LMAX架构可以获得每秒6百万订单，用1微秒的延迟获得吞吐量为100K+。	
 * http://ifeve.com/disruptor-getting-started/
 * @author fhx
 * @date 2019年12月13日
 */
public class LongEvent { 
    private long value;
    public long getValue() { 
        return value; 
    } 
 
    public void setValue(long value) { 
        this.value = value; 
    } 
} 