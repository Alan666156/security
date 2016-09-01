package com.security.exception;

/**
 * 全局系统业务异常(unchecked)
 * 
 * @author
 * 
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -8058289372452353570L;

	/** 错误码 */
	private String status;
	private String memo;

	/** 错误码 */
	public String getStatus() {
		return status;
	}

	public BusinessException() {
		super();
	}

	public BusinessException(Throwable e) {
		super(e);
	}

	public BusinessException(String msg) {
		super(msg);
	}
	
	public BusinessException(String msg, Throwable e) {
		super(msg, e);
	}
	
	/** 错误备注 */
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return "【错误码：" + status + ",描述：" + memo + "】";
	}

}