package com.security.cas;


import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.security.userdetails.CustomUserDetailsService;

/**
 * spring security相关配置
 * 
 * @author fuhongxing
 * @date 2016年5月26日
 * @version 1.0.0
 */
//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${cas.local.url}")
	private String casLocalUrl;
	@Value("${cas.service.login.url}")
	private String casLoginUrl;
	@Value("${cas.service.validate.url}")
	private String casValidateUrl;

	@Autowired
	private LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;
	@Autowired
	private LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;


	// @Bean
	// public AuthenticationUserDetailsService<CasAssertionAuthenticationToken>
	// customUserDetailsService() {
	// return new CustomUserDetailsService();
	// }

	/**
	 * session管理
	 * 
	 * @return
	 */
	/*@Bean
	public SessionAuthenticationStrategy sessionStrategy() {
		SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
		return sessionStrategy;
	}*/

	/**
	 * Request层面的配置，对应XML Configuration中的<http>元素
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/resources/**", "/html/**", "/index/**", "/health/**").permitAll().anyRequest()
				.authenticated();
		// cas登录
		http.addFilter(casAuthenticationFilter()).addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
				.addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);
		;
		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());
		// http.logout().logoutRequestMatcher(new
		// AntPathRequestMatcher("/logout"));
		http.logout().logoutUrl("/j_spring_cas_security_logout").logoutSuccessUrl("/").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID");
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(casLocalUrl + "/j_spring_cas_security_check");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(casValidateUrl);
	}
	
	@Bean
	public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
		return new CustomUserDetailsService();
	}
	
	/**
	 * cas认证提供器，定义客户端的验证方式
	 * 
	 * @return
	 */
	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setKey("id_for_this_auth_provider_only");
		casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
		return casAuthenticationProvider;
	}

	/**
	 * cas 登录切点
	 * 
	 * @return
	 */
	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(casLoginUrl);
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	/********************* 过滤器 **********************/

	/**
	 * cas 认证过滤器
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		// 登录失败处理
		casAuthenticationFilter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);
		// 登录成功处理
		casAuthenticationFilter.setAuthenticationFailureHandler(loginAuthenticationFailureHandler);
		// casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		return casAuthenticationFilter;
	}

	/**
	 * 客户端注销
	 * 
	 * @return
	 */
	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(casValidateUrl);
		return singleSignOutFilter;
	}

	/**
	 * 服务端注销退出
	 * 
	 * @return
	 */
	@Bean
	public LogoutFilter requestCasGlobalLogoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(casLoginUrl + "logout?reloginUrl=" + casLocalUrl,
				new SecurityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl("/j_spring_cas_security_logout");
		// logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
		return logoutFilter;
	}

	/**
	 * 身份验证配置，用于注入自定义身份验证Bean和密码校验规则
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(casAuthenticationProvider());
	}

}
