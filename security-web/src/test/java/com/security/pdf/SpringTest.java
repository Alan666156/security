package com.security.pdf;

import com.security.SecurityApplication;
import com.security.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
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
		int i = 1;
		i = i++;
		int j = i++;
		int k = i + ++i * i++;
		log.info("i=" + i);
		log.info("j=" + j);
		log.info("k=" + k);
	}

}
