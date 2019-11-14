package com.wizarpos.pay.cashier.pay_tems.alipay.entity;

import java.io.Serializable;

public class CreatOrderReq implements Serializable {

	/**
	 * 支付宝生成订单请求实体
	 * 
	 * @author hong
	 */
	private static final long serialVersionUID = 1L;
	private String authCode;// 支付二维码号
	private String amount;// 实际金额
	private String inputAmount;// 输入金额
	private String cardNo;
	private String rechargeOn;
	private String QQKey;// /密钥
	private String patternId;// 商户号

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(String inputAmount) {
		this.inputAmount = inputAmount;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getRechargeOn() {
		return rechargeOn;
	}

	public void setRechargeOn(String rechargeOn) {
		this.rechargeOn = rechargeOn;
	}

	public String getQQKey() {
		return QQKey;
	}

	public void setQQKey(String qQKey) {
		QQKey = qQKey;
	}

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

}
