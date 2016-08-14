package com.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
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
	 * 获取accessToken
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "oauth/getAccessToken" , method = RequestMethod.POST)
	@ResponseBody
	public String getAccessToken(Model model){
		LOGGER.info("==========getToken===========");
		return "home";
	}
}
