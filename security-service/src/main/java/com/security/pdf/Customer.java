package com.security.pdf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class Customer implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name; // 客户姓名
	private String mobile;// 移动电话
	private String certiType;// 证件类型
	private String certiNum;// 证件号
	private String bankAccount;// 银行账号
	private String postalCode;// 邮政编号
	private String address; // 地址
	private String customerType;// 客户类型
	private String bankCard;// 银行卡
	private String bankAddress;// 开户行
	private String asset;// 资产
	private BigDecimal fund = BigDecimal.ZERO;// 资金
	private Date importTime;// 数据导入时间
	private String code; // 客户编号
	private String serialNum; // 序列号
	private String province;// 省份
	private String city;// 城市
	private String certiTypeValue;// 证件类型表现值
//	private Set<BankInfo> bankInfos = new HashSet<BankInfo>();// 银行卡信息
	private String flowId; // 客户状态
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getCertiNum() {
		return certiNum;
	}
	public void setCertiNum(String certiNum) {
		this.certiNum = certiNum;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getBankAddress() {
		return bankAddress;
	}
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public BigDecimal getFund() {
		return fund;
	}
	public void setFund(BigDecimal fund) {
		this.fund = fund;
	}
	public Date getImportTime() {
		return importTime;
	}
	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCertiTypeValue() {
		return certiTypeValue;
	}
	public void setCertiTypeValue(String certiTypeValue) {
		this.certiTypeValue = certiTypeValue;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	
}
