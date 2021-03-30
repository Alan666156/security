package com.security.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户注册信息
 * @author Alan Fu
 */
@Data
@Entity
@Table(name="oauth_user")
public class User extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**昵称*/
	private String userName;
	/**密码*/
	private String passward;
	/**邮箱*/
	private String email;
	/**电话号码*/
	private String mobile;
	/**是否启用(0启用，1未启用)*/
	private Long enabled;

	public User() {
	}

	public User(String userName, String passward) {
		this.userName = userName;
		this.passward = passward;
	}
}
