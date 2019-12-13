package com.security.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 事件消费者
 * @author fhx
 * @date 2019年12月13日
 */
public class LongEventHandler implements EventHandler<LongEvent>  {
	/**
	 * 事件回调方法
	 */
	@Override
	public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
		System.out.println(longEvent.getValue()); 		
	}

}
