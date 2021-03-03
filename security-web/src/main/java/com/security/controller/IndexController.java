package com.security.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.security.annotation.UseLog;
import com.security.service.OauthCodeService;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private OauthCodeService oauthCodeService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationServerTokenServices tokenServices;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;
    @Autowired
    private OAuth2RequestValidator oAuth2RequestValidator;
    @Autowired
    private TokenStore tokenStore;
    //生成一個令牌桶，同時每秒放入2个令牌
    final RateLimiter limiter = RateLimiter.create(2);
	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 限流方案：1、google guava只能做单台应用服务
	 * 			2、redis + lua分布式限流处理
	 * 			3、Spring AOP限流，切面进行拦截处理
	 * @param model
	 * @return
	 */
	@RequestMapping("/home")
	public Result home(Model model){
		//判断是否拿到令牌
		if (limiter.tryAcquire()){
			LOGGER.info("==========请求成功===========");
		}else {
			LOGGER.info("==========请求过于频繁，请稍后再试===========");
		}
		return Result.success("success");
	}

	@RequestMapping("/api")
	public String api(Model model, String name){
		System.out.println(name);
		LOGGER.info("==========api===========");
		return "api";
	}
	@UseLog(remark="useLog test login")
	@RequestMapping("login")
	public String login(Model model){
		LOGGER.info("==========login===========");
		return "login";
	}
	
	/**
	 * 判断请求设备类型
	 * @param device
	 */
	@RequestMapping("/api/device")
	@ResponseBody
    public String home(Device device) {
		LOGGER.info("divce :"+device.getDevicePlatform().name());
		String type = null;
        if (device.isMobile()) {//手机
            LOGGER.info("Hello mobile user!");
            type = "mobile ";
        } else if (device.isTablet()) {//平板
        	LOGGER.info("Hello tablet user!");
        	type = "tablet ";
        } else {//pc电脑
        	LOGGER.info("Hello desktop user!");
        	type = "desktop";
        }
        return type + device.getDevicePlatform().name();
    }
	/**
	 * 判断请求设备类型方式二
	 * @param device
	 */
	@RequestMapping("/")
	@ResponseBody
    public String home(SitePreference sitePreference, Model model) {
        if (sitePreference == SitePreference.NORMAL) {
        	LOGGER.info("Site preference is normal");
            return "home";
        } else if (sitePreference == SitePreference.MOBILE) {
        	LOGGER.info("Site preference is mobile");
            return "home-mobile";
        } else if (sitePreference == SitePreference.TABLET) {
        	LOGGER.info("Site preference is tablet");
            return "home-tablet";
        } else {
        	LOGGER.info("no site preference");
            return "home";
        }
    }
	
}
