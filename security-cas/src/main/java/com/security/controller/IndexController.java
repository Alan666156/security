package com.security.controller;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
    
	@RequestMapping("/index")
	public String index(Model model){
		LOGGER.info("==========index==========");
		return "index";
	}
	
	@RequestMapping("home")
	public String home(Model model){
		LOGGER.info("==========home===========");
		return "home";
	}
	
	@RequestMapping("api")
	public String api(Model model, String name){
		System.out.println(name);
		LOGGER.info("==========api===========");
		return "api";
	}
	@RequestMapping("properties")
    @ResponseBody
    public Properties properties() {
        return System.getProperties();
    }
}
