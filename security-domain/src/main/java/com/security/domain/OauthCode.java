package com.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * oauth code
 * @author Alan Fu
 */
@Entity
@Table(name="oauth_code")
public class OauthCode extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String authentication;
	
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
