package com.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping("oauth/token")
	@ResponseBody
	public String getToken(Model model){
		LOGGER.info("==========getToken===========");
		return "home";
	}
}
