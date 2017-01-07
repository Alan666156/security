package com.security.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.security.domain.OauthClientDetails;
import com.security.service.OauthClientDetailsService;
import com.security.util.Generate;
import com.security.vo.PageFormVo;

/**
 * 用户信息管理
 *
 * @author Alan Fu
 */
@RequestMapping("client/")
@Controller
public class ClientDetailsController {

	private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
    @Autowired
    private OauthClientDetailsService oauthService;

//    @Autowired
//    private OauthClientDetailsDtoValidator clientDetailsDtoValidator;


    @RequestMapping("clientdetails")
    public String clientDetails(Model model) {
    	LOGGER.info("加载用户信息");
    	Page<OauthClientDetails> page = oauthService.findAll(null, new PageFormVo());
    	LOGGER.info(JSON.toJSONString(page.getContent().get(0)));
        model.addAttribute("clientDetailsList", page.getContent());
        return "clientdetails";
    }


    /*
    * Logic delete
    * */
    @RequestMapping("archive_client/{clientId}")
    public String archiveClient(@PathVariable("clientId") String clientId) {
//        oauthService.archiveOauthClientDetails(clientId);
        return "redirect:../clientdetails";
    }

    /*
    * Test client
    * */
    @RequestMapping("test_client/{clientId}")
    public String testClient(@PathVariable("clientId") String clientId, Model model) {
//        OauthClientDetailsDto clientDetailsDto = oauthService.loadOauthClientDetailsDto(clientId);
//        model.addAttribute("clientDetailsDto", clientDetailsDto);
        return "clientdetails/test_client";
    }


    /*
     * 跳转注册页面
    * Register client
    * */
    @RequestMapping(value = "registerClient")
    public String registerClient(Model model) {
    	OauthClientDetails formDto = new OauthClientDetails();
    	formDto.setClientId(Generate.generateUUID());
    	formDto.setClientSecret(Generate.generateClientSecret());
        model.addAttribute("formDto", formDto);
        return "registerClient";
    }


    /*
    * Submit register client
    * */
    @RequestMapping(value = "addClient", method = RequestMethod.POST)
    public String submitRegisterClient(@ModelAttribute("formDto") OauthClientDetails formDto) {
//        clientDetailsDtoValidator.validate(formDto, result);
    	LOGGER.info("register client");
//    	formDto.setClientId(Generate.generateUUID());
//    	formDto.setClientSecret(Generate.generateClientSecret());
        oauthService.save(formDto);
        return "redirect:clientdetails";
    }
    
    /*
     * Submit register client
     * */
//     @RequestMapping(value = "register_client", method = RequestMethod.POST)
//     public String submitRegisterClient(@ModelAttribute("formDto") OauthClientDetails formDto, BindingResult result) {
////         clientDetailsDtoValidator.validate(formDto, result);
//         
//     	if (result.hasErrors()) {
//             return "clientdetails/register_client";
//         }
//         oauthService.save(formDto);
//         return "redirect:client_details";
//     }

}