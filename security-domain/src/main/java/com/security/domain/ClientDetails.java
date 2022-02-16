package com.security.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Data
@Entity
@Table(name="client_details")
public class ClientDetails extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(unique=true)
	private String appId;		//appid
	@Column(length=2000)
	private String resourceIds; 
	@Column(unique=true)
	private String appSecret;	//secret
	private String scope;		//权限范围,可选值包括read,write,trust
	private String grantTypes; 	//授权类型
	private String redirectUrl;	//回调地址
	private String authorities;	//用户权限
	private Long accessTokenValidity;	//access token 是否有效
	private Long refreshTokenValidity;	//refresh token 是否有效
	@Column(length=4000)
	private String additionalInformation;	//附加信息

	  
}
