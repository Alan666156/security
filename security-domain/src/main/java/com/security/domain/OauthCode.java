package com.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 授权code(只有当grant_type为"authorization_code"时,该表中才会有数据产生; )
 * @author Alan Fu
 */
@Entity
@Table(name="oauth_code")
public class OauthCode extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code; //授权码
	@Column(length=4000)
	private String authentication; //存储将AuthorizationRequestHolder.java对象序列化后的二进制数据.
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAuthentication() {
		return authentication;
	}
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	
}
