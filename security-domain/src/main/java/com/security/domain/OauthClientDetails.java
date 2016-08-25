package com.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * oauth client details
 * JdbcClientDetailsService
 * @author Alan Fu
 */
@Entity
@Table(name="oauth_client_details")
public class OauthClientDetails extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String resourceIds;
	private String clientSecret;
	private String scope;//权限范围,可选值包括read,write,trust
	private String authorizedGrantTypes;//授权类型,可选值authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔
	private String webServerRedirectUri;//客户端重定向地址 
	private String authorities;			//用户权限 
	private Long accessTokenValidity; //设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). 
	private Long refreshTokenValidity;//设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天).
	@Column(length=4000)
	private String additionalInformation;//这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:{"country":"CN","country_code":"086"}
	private String autoapprove;//设置用户是否自动Approval操作, 默认值为 'false', 可选值包括 'true','false', 'read','write'. 
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}
	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}
	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}
	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}
	public String getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
	public Long getAccessTokenValidity() {
		return accessTokenValidity;
	}
	public void setAccessTokenValidity(Long accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}
	public Long getRefreshTokenValidity() {
		return refreshTokenValidity;
	}
	public void setRefreshTokenValidity(Long refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public String getAutoapprove() {
		return autoapprove;
	}
	public void setAutoApprove(String autoapprove) {
		this.autoapprove = autoapprove;
	}
	
	 
}
