package com.security.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Data
@Entity
@Table(name="oauth_refresh_token")
public class OauthRefreshToken extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tokenId;	//tokenid
	private String token;	//token
	@Column(length=4000)
	private String authentication;

}
