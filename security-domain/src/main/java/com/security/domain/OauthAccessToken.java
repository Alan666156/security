package com.security.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 存储客户accesstoken信息
 * @author Alan Fu
 */
@Data
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
	private String userName;
	private String clientId;
	private String authentication;
	private String refreshToken;

}
