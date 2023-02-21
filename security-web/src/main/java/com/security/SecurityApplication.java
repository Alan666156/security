package com.security;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.management.*;

/**
 * @author fuhongxing
 */
//@EnableOAuth2Sso
//@EnableAdminServer
@Slf4j
@EnableAsync
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
