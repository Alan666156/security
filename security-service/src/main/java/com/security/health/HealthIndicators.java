package com.security.health;


import java.util.Collection;

import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 系统健康、cpu、内存
 * @author Alan
 * @date 2016年3月1日
 * @version V1.0.0
 */
@Configuration
public class HealthIndicators {

	/**
	 * 系统参数
	 * @param systemPublicMetrics
	 * @return
	 */
	@Bean
	public HealthIndicator system(final SystemPublicMetrics systemPublicMetrics){
		return new AbstractHealthIndicator() {
			@Override
			protected void doHealthCheck(Builder builder) throws Exception {
				Collection<Metric<?>> set = systemPublicMetrics.metrics();
				builder.up();
				for (Metric<?> metric : set) {
					builder.withDetail(metric.getName(), metric.getValue());
				}
				
			}
		};
	}
	
}
