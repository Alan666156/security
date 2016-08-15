package com.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户注册信息
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
	private String email;	//邮箱
	private String mobile; 	//电话号码
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
