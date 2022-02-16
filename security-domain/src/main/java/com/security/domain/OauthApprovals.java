package com.security.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 授权确认信息表
 * @author Alan.Fu
 *
 */
@Data
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

}    
    
