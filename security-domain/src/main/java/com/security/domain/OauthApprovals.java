package com.security.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 授权确认信息表
 * @author Alan.Fu
 *
 */
@Entity
@Table(name="oauth_approvals")
public class OauthApprovals extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
    private String clientId;
    private String scope;	
    private String status;	//状态
    private Date expiresAt;	//失效时间
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}
    
}    
    
