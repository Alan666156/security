package com.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="client_details", uniqueConstraints = @UniqueConstraint(columnNames = { "appId", "appSecret"}))
public class ClientDetails extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appId;		//appid
	@Column(length=2000)
	private String resourceIds; 
	private String appSecret;	//secret
	private String scope;		//权限范围,可选值包括read,write,trust
	private String grantTypes; 	//授权类型
	private String redirectUrl;	//回调地址
	private String authorities;	//用户权限
	private Long accessTokenValidity;	//access token 是否有效
	private Long refreshTokenValidity;	//refresh token 是否有效
	@Column(length=4000)
	private String additionalInformation;	//附加信息
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getResourceIds() {
		return resourceIds;
	}
	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getGrantTypes() {
		return grantTypes;
	}
	public void setGrantTypes(String grantTypes) {
		this.grantTypes = grantTypes;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
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
	  
}
