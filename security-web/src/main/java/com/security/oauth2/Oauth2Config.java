package com.security.oauth2;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.security.util.Generate;

/**
 * security oauth2.0
 * 
 * @author Alan Fu
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {
	// Authorization Server：授权服务器，能够成功验证资源拥有者和获取授权，并在此之后分发令牌的服务器；
	// Resource Server：资源服务器，存储用户的数据资源，能够接受和响应受保护资源请求的服务器；
	// Client：客户端，获取授权和发送受保护资源请求的第三方应用；
	// Resource Owner：资源拥有者，能够对受保护资源进行访问许可控制的实体；
	// Protected Resource：受保护资源，能够使用OAuth请求获取的访问限制性资源；
	// Authorization Code：授权码；
	// Refresh Token：刷新令牌；
	// Access Token：访问令牌。
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Autowired
//	private BaseClientDetails baseClientDetails;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
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
    
//    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
//    private WebResponseExceptionTranslator providerExceptionHandler = new DefaultWebResponseExceptionTranslator();
	/**
	 * token
	 * @return
	 */
	@Bean
	public JdbcTokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}
	
	@Bean
	protected AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}
	
	@Bean
	public OAuth2RequestFactory oAuth2RequestFactory(){
		return new DefaultOAuth2RequestFactory(clientDetailsService);
	}
	
	@Bean
	public OAuth2RequestValidator oAuth2RequestValidator(){
		return new DefaultOAuth2RequestValidator();
	}
	
	/**
	 * 
	 * @return
	 */
	/*@Bean
    public ClientDetailsService createClientDetailsService(){
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
//        jdbcClientDetailsService.addClientDetails(clientDetails);
        return jdbcClientDetailsService;
    }*/
	
	/*@Bean(name = "myDefaultBaseClientDetails")
    public BaseClientDetails createBaseClientDetails(){
        BaseClientDetails baseClientDetails = new BaseClientDetails();

        baseClientDetails.setClientId("whc_client_id");
        baseClientDetails.setClientSecret("whc_client_secret");

        LinkedList<String> scope = new LinkedList<String>();
        scope.add("whc");
        baseClientDetails.setScope(scope);

        Set<String> registeredRedirectUris = new HashSet<String>();
        registeredRedirectUris.add("http://localhost:8080/test");
        baseClientDetails.setRegisteredRedirectUri(registeredRedirectUris);

        LinkedList<String> grant_types = new LinkedList<String>();
        grant_types.add("client_credentials");
        baseClientDetails.setAuthorizedGrantTypes(grant_types);

        baseClientDetails.setAccessTokenValiditySeconds(4 * 60 * 60);
        baseClientDetails.setRefreshTokenValiditySeconds(24 * 60 * 60);

        LinkedList<String> autoApproveScopes = new LinkedList<String>();
        autoApproveScopes.add("whc");
        baseClientDetails.setAutoApproveScopes(autoApproveScopes);
        return baseClientDetails;
    }*/
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//设置加密方式
		security.passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
		/*endpoints.authenticationManager(new AuthenticationManager() {
			@Override
			public Authentication authenticate(Authentication authentication) {
				return authenticationManagerBuilder.getOrBuild().authenticate(authentication);
			}
		});*/
	}
	
	// Client settings
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		/*clients.inMemory().withClient(config.clientId)
				.authorizedGrantTypes("password", "refresh_token")
				.authorities("ROLE_USER").scopes("write")
				.secret(config.clientSecret);*/
		
		clients.jdbc(dataSource).passwordEncoder(passwordEncoder).withClient(Generate.generateUUID())
		/**
		 * Oauth支持的grant_type(授权方式) 
		 * 1、authorization_code 授权码模式(即先登录获取code,再获取token)
		 * 2、client_credentials客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向'服务端'获取资源)
		 * 3、refresh_token 刷新access_token
		 * 4、implicit 简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)
		 * 5、password 密码模式(将用户名,密码传过去,直接获取token)
		 */
		
		.authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token","password", "implicit")
		.authorities("ROLE_CLIENT")
		.resourceIds("apis")
		.scopes("read")
		.secret("secret")
		.accessTokenValiditySeconds(300);
	}

}
