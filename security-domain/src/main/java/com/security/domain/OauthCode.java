package com.security.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 授权code(只有当grant_type为"authorization_code"时,该表中才会有数据产生; )
 * @author Alan Fu
 */
@Data
@Entity
@Table(name="oauth_code")
public class OauthCode extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code; //授权码
	@Column(length=4000)
	//存储将AuthorizationRequestHolder.java对象序列化后的二进制数据.
	private String authentication;

}
