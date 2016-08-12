package com.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="oauth_refresh_token")
public class OauthRefreshToken extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tokenId;	//tokenid
	private String token;	//token
	private String authentication;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAuthentication() {
		return authentication;
	}
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	
}
