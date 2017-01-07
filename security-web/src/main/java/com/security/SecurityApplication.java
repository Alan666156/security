package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@EnableOAuth2Sso
@SpringBootApplication
public class SecurityApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SecurityApplication.class, args);
	}
}
