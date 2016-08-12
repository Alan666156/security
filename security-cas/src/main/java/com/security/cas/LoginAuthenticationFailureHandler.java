package com.security.cas;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 单点认证失败回调
 * @author fuhongxing
 * @date 2016年7月30日
 * @version 1.0.0
 */
@Configuration
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);
		LOGGER.error("authenticationFailure:" + exception,exception);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
	}
	
	
}
