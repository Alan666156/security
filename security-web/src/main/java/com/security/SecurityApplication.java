package com.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@EnableOAuth2Sso
@Slf4j
@SpringBootApplication
public class SecurityApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SecurityApplication.class, args);
		log.info("=========START SUCCESS=========");
	}
}
