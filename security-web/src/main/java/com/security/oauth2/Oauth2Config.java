package com.security.oauth2;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

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
	
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
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
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
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
	
	/**
	 * 
	 * @param clientDetails
	 * @param dataSource
	 * @return
	 */
	@Bean
    public ClientDetailsService createClientDetailsService(){
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
//        jdbcClientDetailsService.addClientDetails(clientDetails);
        return jdbcClientDetailsService;
    }
	
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
		
		clients.jdbc(dataSource).passwordEncoder(passwordEncoder).withClient("client")
		.authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token","password", "implicit")
		.authorities("ROLE_CLIENT")
		.resourceIds("apis")
		.scopes("read")
		.secret("secret")
		.accessTokenValiditySeconds(360);
	}

}
