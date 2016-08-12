package com.security.cas;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * 单点认证成功回调
 * @author fuhongxing
 * @date 2016年7月30日
 * @version 1.0.0
 */
@Configuration
public class LoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthenticationSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        /** 记录登陆信息 **/
		LOGGER.info("=======CAS单点登录成功======");
		response.setStatus(HttpServletResponse.SC_OK);
		super.onAuthenticationSuccess(request, response, authentication);
    }
}
