package com.security.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.security.annotation.UseLog;
import com.security.domain.OauthCode;
import com.security.service.OauthCodeService;
import com.security.util.Generate;
@Controller
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
   
    
	/*@RequestMapping("/")
	public String index(Model model){
		LOGGER.info("==========index==========");
		return "index";
	}*/
	
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
	/**
	 * 获取授权码
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "oauth/getAuthorizationCode")
	@ResponseBody
	public String getAuthorizationCode(Model model,HttpServletRequest request, HttpServletResponse response){
		LOGGER.info("==========getOauthCode===========");
		OauthCode oauthCode = new OauthCode();
		oauthCode.setCode(Generate.generateUUID());
		oauthCodeService.save(oauthCode);
		return "AuthorizationCode";
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
//	
	
	@UseLog(remark = "test log")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/oauth/rest_token", method = RequestMethod.POST)
	@ResponseBody
	public OAuth2AccessToken postAccessToken(@RequestBody String param) {
		JSONObject json = JSONObject.parseObject(param);
		String clientId = json.getString(OAuth2Utils.CLIENT_ID);
		Map<String,String> parameters = (Map<String,String>)JSON.parse(param);
//		String clientId = getClientId(parameters);
		ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
		
		TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);

		if (clientId != null && !"".equals(clientId)) {
			// Only validate the client details if a client authenticated during
			// this
			// request.
			if (!clientId.equals(tokenRequest.getClientId())) {
				// double check to make sure that the client ID in the token
				// request is the same as that in the
				// authenticated client
				throw new InvalidClientException("Given client ID does not match authenticated client");
			}
		}

		if (authenticatedClient != null) {
			oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
		}

		final String grantType = tokenRequest.getGrantType();
		if (!StringUtils.hasText(grantType)) {
			throw new InvalidRequestException("Missing grant type");
		}
		//隐式授权
		if ("implicit".equals(grantType)) {
			throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
		}

		if (isAuthCodeRequest(parameters)) {
			// The scope was requested or determined during the authorization
			// step
			if (!tokenRequest.getScope().isEmpty()) {
				LOGGER.debug("Clearing scope of incoming token request");
				tokenRequest.setScope(Collections.<String> emptySet());
			}
		}

		if (isRefreshTokenRequest(parameters)) {
			// A refresh token has its own default scopes, so we should ignore
			// any added by the factory here.
			tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
		}

		OAuth2AccessToken token = getTokenGranter(grantType).grant(grantType, tokenRequest);
		if (token == null) {
			throw new UnsupportedGrantTypeException("Unsupported grant type: " + grantType);
		}
		LOGGER.info("OAuth2AccessToken=" + JSON.toJSONString(token));
		return token;

	}
	/**
	 * 
	 * @param grantType
	 * @return
	 */
	protected TokenGranter getTokenGranter(String grantType) {
        if ("authorization_code".equals(grantType)) {
            return new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if ("password".equals(grantType)) {
            return new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if ("refresh_token".equals(grantType)) {
            return new RefreshTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if ("client_credentials".equals(grantType)) {
            return new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if ("implicit".equals(grantType)) {
            return new ImplicitTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else {
            throw new UnsupportedGrantTypeException("Unsupport grant_type: " + grantType);
        }
    }
	
	protected String getClientId(Map<String, String> parameters) {
        return parameters.get("client_id");
    }
	private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }
}
