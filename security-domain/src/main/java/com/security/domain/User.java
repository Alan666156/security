package com.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 
 * @author Alan Fu
 */
@Entity
@Table(name="oauth_user")
public class User extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;//昵称
	private String passward;//密码
	private Long enabled; //是否启用(0启用，1未启用)
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public Long getEnabled() {
		return enabled;
	}
	public void setEnabled(Long enabled) {
		this.enabled = enabled;
	}
	
}
