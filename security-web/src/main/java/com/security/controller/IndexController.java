package com.security.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.security.domain.OauthCode;
import com.security.service.OauthCodeService;
import com.security.util.Generate;

@Controller
public class IndexController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private OauthCodeService oauthCodeService;
	
	
	@RequestMapping("/")
	public String index(Model model){
		LOGGER.info("==========index==========");
		return "index";
	}
	
	@RequestMapping("home")
	public String home(Model model){
		LOGGER.info("==========home===========");
		return "home";
	}
	
	/**
	 * 获取code
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "oauth/getOauthCode")
	@ResponseBody
	public String getOauthCode(Model model,HttpServletRequest request, HttpServletResponse response){
		LOGGER.info("==========getOauthCode===========");
		OauthCode oauthCode = new OauthCode();
		oauthCode.setCode(Generate.generateUUID());
		oauthCodeService.save(oauthCode);
		return "home";
	}
	
	/**
	 * 获取accessToken
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "oauth/getAccessToken")
	@ResponseBody
	public String getAccessToken(Model model,HttpServletRequest request, HttpServletResponse response){
		LOGGER.info("==========getAccessToken===========");
		return "getAccessToken";
	}
	
	private String getAuthorizeUrl(String clientId, String redirectUri, String scope) {
		/*UriBuilder uri = serverRunning.buildUri("/oauth/authorize").queryParam("response_type", "code").queryParam("state", "mystateid").queryParam("scope", scope);
		if (clientId != null) {
			uri.queryParam("client_id", clientId);
		}
		if (redirectUri != null) {
			uri.queryParam("redirect_uri", redirectUri);
		}*/
		return null;
	}
}
