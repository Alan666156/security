package com.security.cas;



import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


/**
 * 
 * @author Alan Fu
 */
@Configuration
public class WebConfig {

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
			@Override
			public void customize(ConfigurableWebServerFactory factory) {
				ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/html/400.html");
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/html/401.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html");
				ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/html/405.html");
				ErrorPage error408Page = new ErrorPage(HttpStatus.REQUEST_TIMEOUT, "/html/405.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/html/500.html");
				factory.addErrorPages(error400Page, error401Page, error404Page, error405Page, error408Page, error500Page);
			}
		};
	}
	
}
