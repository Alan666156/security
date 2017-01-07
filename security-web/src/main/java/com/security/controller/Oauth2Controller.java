package com.security.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.security.config.SecurityConfig;
import com.security.service.OauthClientDetailsService;
import com.security.service.OauthCodeService;

/**
 * Oauth2相关操作
 * @author Alan Fu
 */
@RequestMapping(value = "oauth2/")
@Controller
public class Oauth2Controller {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(Oauth2Controller.class);
	
	@Autowired
	private OauthCodeService oauthCodeService;
	
	@Autowired
	private OauthClientDetailsService oauthClientDetailsService;
	
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
   
    //http://localhost:8888/oauth2/authorize?response_type=code&client_id=acme&redirect_uri=http://example.com
	
    @RequestMapping("user")
    public Principal user(Principal user) {
        return user;
    }
    
	/**
	 * 获取授权码
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "authorize")
	@ResponseBody
	public String getAuthorizationCode(Model model,HttpServletRequest request, HttpServletResponse response){
		LOGGER.info("==========getOauthCode===========");
//		OauthCode oauthCode = new OauthCode();
//		oauthCode.setCode(Generate.generateUUID());
//		oauthCodeService.save(oauthCode);
		oauthCodeService.generateCode();
		return "AuthorizationCode";
	}
	
	/**
	 * 获取accessToken
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getAccessToken")
	@ResponseBody
	public String getAccessToken(Model model,HttpServletRequest request, HttpServletResponse response){
		LOGGER.info("==========getAccessToken===========");
//		tokenServices.getAccessToken(authentication);
		return "getAccessToken";
	}
	
	private String getAuthorizeUrl(String clientId, String redirectUri, String scope) {
		/*URIBuilder uri = serverRunning.buildUri("/oauth/authorize").queryParam("response_type", "code").queryParam("state", "mystateid").queryParam("scope", scope);
		if (clientId != null) {
			uri.queryParam("client_id", clientId);
		}
		if (redirectUri != null) {
			uri.queryParam("redirect_uri", redirectUri);
		}*/
		return null;
	}
//	
	
//	@UseLog(remark = "test log")
	@RequestMapping(value = "rest_token")
	@ResponseBody
	public OAuth2AccessToken postAccessToken(@RequestBody String param) {
		LOGGER.info("Get AccessToken param:{}", param);
		if(StringUtils.isEmpty(param)){
			throw new InvalidClientException("param is not null...");
		}
		JSONObject json = JSONObject.parseObject(param);
		String clientId = json.getString(OAuth2Utils.CLIENT_ID);
		Map<String,String> parameters = (Map<String,String>)JSON.parse(param);
//		String clientId = getClientId(parameters);
		ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
		
		TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);
		
		if (!StringUtils.isEmpty(clientId)) {
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
		//验证客户端信息
		if (authenticatedClient != null) {
			oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
		}
		//判断授权类型
		final String grantType = tokenRequest.getGrantType();
		if (!StringUtils.hasText(grantType)) {
			throw new InvalidRequestException("Missing grant type");
		}
		
		//隐式授权
		if (SecurityConfig.GrantType.IMPLICIT.getType().equals(grantType)) {
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
	 * 根据授权类型返回token
	 * @param grantType
	 * @return
	 */
	protected TokenGranter getTokenGranter(String grantType) {
        if (SecurityConfig.GrantType.AUTHORIZATION_CODE.getType().equals(grantType)) {
            return new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if (SecurityConfig.GrantType.PASSWORD.getType().equals(grantType)) {
            return new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if (SecurityConfig.GrantType.REFRESH_TOKEN.getType().equals(grantType)) {
            return new RefreshTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if (SecurityConfig.GrantType.CLIENT_CREDENTIALS.getType().equals(grantType)) {
            return new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else if (SecurityConfig.GrantType.IMPLICIT.getType().equals(grantType)) {
            return new ImplicitTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
        } else {
            throw new UnsupportedGrantTypeException("Unsupport grant_type: " + grantType);
        }
    }
	
	protected String getClientId(Map<String, String> parameters) {
        return parameters.get("client_id");
    }
	
	private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return SecurityConfig.GrantType.REFRESH_TOKEN.getType().equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return SecurityConfig.GrantType.AUTHORIZATION_CODE.getType().equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }
}
