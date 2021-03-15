package com.security.config;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


/**
 * 
 * @author Alan Fu
 */
@Configuration
public class WebConfig {
	
	/**
	 * 错误异常页面
	 * @return
	 */
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//		return new EmbeddedServletContainerCustomizer() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//				ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/html/400.html");
//				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/html/401.html");
//				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html");
//				ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/html/405.html");
//				ErrorPage error408Page = new ErrorPage(HttpStatus.REQUEST_TIMEOUT, "/html/405.html");
//				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/html/500.html");
//				container.addErrorPages(error400Page, error401Page, error404Page, error405Page, error408Page, error500Page);
//			}
//		};
//	}

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {
			ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/html/400.html");
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/html/401.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html");
			ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/html/405.html");
			ErrorPage error408Page = new ErrorPage(HttpStatus.REQUEST_TIMEOUT, "/html/405.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/html/500.html");
			factory.addErrorPages(error400Page, error401Page, error404Page, error405Page, error408Page, error500Page);
		};
	}
	/**
	 * redis config
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<Serializable, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Serializable, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		// 设置 value 的转化格式和 key 的转化格式
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}

	/**
	 * 设备
	 * @return
	 */
//	@Bean
//	public LiteDeviceDelegatingViewResolver liteDeviceAwareViewResolver() {
//	    InternalResourceViewResolver delegate = new InternalResourceViewResolver();
//	    delegate.setPrefix("/WEB-INF/views/");
//	    delegate.setSuffix(".jsp");
//	    LiteDeviceDelegatingViewResolver resolver = new LiteDeviceDelegatingViewResolver(delegate);
//	    resolver.setMobilePrefix("mobile/");
//	    resolver.setTabletPrefix("tablet/");
//	    return resolver;
//	}
	
}
