package com.security.domain;


import lombok.Data;

@Data
//@Entity
//@Table(name="oauth_authorities", uniqueConstraints = @UniqueConstraint(columnNames = { "appId", "appSecret"}))
public class Authorities extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String authority;

}
