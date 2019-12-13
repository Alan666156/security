package com.security.disruptor;

import com.lmax.disruptor.EventFactory;
/**
 * 产生LongEvent的工厂类，它会在Disruptor系统初始化时，构造所有的缓冲区中的对象实例（预先分配空间
 * @author fhx
 * @date 2019年12月13日
 */
public class LongEventFactory implements EventFactory { 

    @Override 
    public Object newInstance() { 
        return new LongEvent(); 
    } 
} 