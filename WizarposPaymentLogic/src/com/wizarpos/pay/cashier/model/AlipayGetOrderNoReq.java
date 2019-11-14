package com.wizarpos.pay.cashier.model;

import java.io.Serializable;

/**
 * 支付宝生成订单号请求
 * @author wu
 *
 */
public class AlipayGetOrderNoReq implements Serializable {

	private String amount;
	private String extraAmount;
	private String cardNo;
	private String rechargeOn;
	private String masterTranLogId;
	private String mixFlag;//混合支付flag 1 表示混合支付
	private String masterPayAmount;//第三方应用带过来的数据 wu@[20150923]
	private String inputAmount;//混合支付总金额 wu@[20150923]
	private String isPayComingForm;
	private String patternId;
	private String QQKey;
	private String appid;

	private String userId;
	private String userPasswd;
	private String payBatType;//bat支付方式

	private String commomcashierOrderId; //销售单ID wu@[20151012]

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(String extraAmount) {
		this.extraAmount = extraAmount;
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

	public String getMasterTranLogId() {
		return masterTranLogId;
	}

	public void setMasterTranLogId(String masterTranLogId) {
		this.masterTranLogId = masterTranLogId;
	}

	public String getMixFlag() {
		return mixFlag;
	}

	public void setMixFlag(String mixFlag) {
		this.mixFlag = mixFlag;
	}

	public String getMasterPayAmount() {
		return masterPayAmount;
	}

	public void setMasterPayAmount(String masterPayAmount) {
		this.masterPayAmount = masterPayAmount;
	}

	public String getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(String inputAmount) {
		this.inputAmount = inputAmount;
	}

	public String getIsPayComingForm() {
		return isPayComingForm;
	}

	public void setIsPayComingForm(String isPayComingForm) {
		this.isPayComingForm = isPayComingForm;
	}

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public String getQQKey() {
		return QQKey;
	}

	public void setQQKey(String qQKey) {
		QQKey = qQKey;
	}

	public String getPayBatType() {
		return payBatType;
	}

	public void setPayBatType(String payBatType) {
		this.payBatType = payBatType;
	}

	public void setCommomcashierOrderId(String commomcashierOrderId) {
		this.commomcashierOrderId = commomcashierOrderId;
	}

	public String getCommomcashierOrderId() {
		return commomcashierOrderId;
	}
}
