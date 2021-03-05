package com.security.pdf;

import com.security.SecurityApplication;
import com.security.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecurityApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringTest {
	
	@Test
	public void test() {
		//通过获取Bean对象
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringBeanUtil.class);
		SpringBeanUtil springBeanUtil  = (SpringBeanUtil) annotationConfigApplicationContext.getBean("springUtils");
		log.info("===>bean name: {}", springBeanUtil);
	}
	
	public static void main(String[] args) {

	}
}
