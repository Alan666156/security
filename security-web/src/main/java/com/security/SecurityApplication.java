package com.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.*;

//@EnableOAuth2Sso
@Slf4j
@SpringBootApplication
public class SecurityApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SecurityApplication.class, args);
		//操作系统信息
		OperatingSystemMXBean operateSystemMBean = ManagementFactory.getOperatingSystemMXBean();
		// 运行时信息
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		// 类信息
		ClassLoadingMXBean classLoadMXBean = ManagementFactory.getClassLoadingMXBean();
		// 内存
		MemoryMXBean memoryUsage = ManagementFactory.getMemoryMXBean();
		log.info("=========START SUCCESS=========");
	}
}
