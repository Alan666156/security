package com.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="oauth_access_token")
public class OauthAccessToken extends AbstractEntity{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tokenId;
	private String token;
	private String authenticationId;
	private String user_name;
	private String clientId;
	private String authentication;
	private String refreshToken;
	
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
	public String getAuthenticationId() {
		return authenticationId;
	}
	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getAuthentication() {
		return authentication;
	}
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	 
}
