package com.wizarpos.pay.common.pay;

import java.io.Serializable;

/**
 * pos信息
 * 
 * @author wu
 */
public class PosInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String merchantId;// 收单商户号
	private String terminalId;// 终端号
	private String operatorId;// 操作员号
	private String securityId;//安全码
	private String SignOn;// 是否已签到 ,可能为空 ( 融信 讯联 才有这个字段, 通联 和 银联 没有这个字段)

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getSignOn() {
		return SignOn;
	}

	public void setSignOn(String signOn) {
		SignOn = signOn;
	}
	
	public String getSecurityId() {
		return securityId;
	}
	
	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

}
