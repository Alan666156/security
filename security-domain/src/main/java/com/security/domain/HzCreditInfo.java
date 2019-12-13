package com.security.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 华征数据
 * @author fuhongxing
 */
@Entity
@Table(name="hz_credit_info")
public class HzCreditInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HzCreditInfo() {
		super();
	}
	
	public HzCreditInfo(String name, String idCard) {
		super();
		this.name = name;
		this.idCard = idCard;
	}
	@Id @GeneratedValue
	private Long id;// 主键id
	
	private String name; //姓名
	
	private String idCard; //身份证
	
	private String mobile;//手机号
	
	private String queryStatus;//查询状态
	
	private String status;//状态(数据是否过期:0过期，1未过期)
	
	private String code;//状态码
	
	private String message;//消息提示
	
	private String productNo;//调用接口号
	
	private String chargeFlag; //扣费说明
	@Lob
	@Basic(fetch = FetchType.LAZY) 
	private byte[] result; //华征返回结果
//	private String result;//华征返回结果
	
	private String memo;//备注
	
	private String carNumber;//车牌号
	
	private String mobilePassword;//手机服务密码
	
	private String mobileService;//手机运营商
	/** 创建人 **/
	private String creator;
	/** 更新人 **/
	private String updator;
	/** 创建时间 **/
	private Date createTime = new Date();
	/** 更新时间 **/
	private Date updateTime = new Date();
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getChargeFlag() {
		return chargeFlag;
	}

	public void setChargeFlag(String chargeFlag) {
		this.chargeFlag = chargeFlag;
	}

	public byte[] getResult() {
		return result;
	}

	public void setResult(byte[] result) {
		this.result = result;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getMobilePassword() {
		return mobilePassword;
	}

	public void setMobilePassword(String mobilePassword) {
		this.mobilePassword = mobilePassword;
	}

	public String getMobileService() {
		return mobileService;
	}

	public void setMobileService(String mobileService) {
		this.mobileService = mobileService;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public enum ProductNo{
		/**手机在网时长*/
		HZ_PE00026,	
		/**学历查询*/
		HZ_PE00008_0303,
		/**车辆信息*/
		HZ_PE00008_0302,
		/**个人通讯报告*/
		HZ_PE00008_0301
	}
	
}
