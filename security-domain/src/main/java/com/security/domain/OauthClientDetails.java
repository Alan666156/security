package com.security.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * oauth client details
 * JdbcClientDetailsService
 * @author Alan Fu
 */
@Data
@Entity
@Table(name="oauth_client_details")
public class OauthClientDetails extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;	//用于唯一标识每一个客户端(client); 在注册时必须填写(也可由服务端自动生成). 
	private String resourceIds; //客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: "unity-resource,mobile-resource". 
	private String clientSecret; //用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成). 
	private String scope;//权限范围,可选值包括read,write,trust
	private String authorizedGrantTypes;//授权类型,可选值authorization_code, password, refresh_token, implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔
	private String webServerRedirectUri;//客户端重定向地址 
	private String authorities;			//指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_UNITY,ROLE_USER".
	private Long accessTokenValidity; //设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). 
	private Long refreshTokenValidity;//设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天).
	@Column(length=4000)
	private String additionalInformation;//这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:{"country":"CN","country_code":"086"}
	private String autoapprove;//设置用户是否自动Approval操作, 默认值为 'false', 可选值包括 'true','false', 'read','write'. 
	
	
	public OauthClientDetails() {
		super();
	}
	
	
	public OauthClientDetails(String clientId, String clientSecret,
			String authorizedGrantTypes, Long accessTokenValidity,
			Long refreshTokenValidity) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.authorizedGrantTypes = authorizedGrantTypes;
		this.accessTokenValidity = accessTokenValidity;
		this.refreshTokenValidity = refreshTokenValidity;
	}

}
