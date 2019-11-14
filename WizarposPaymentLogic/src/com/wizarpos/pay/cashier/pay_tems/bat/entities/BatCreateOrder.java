package com.wizarpos.pay.cashier.pay_tems.bat.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Bat生成订单号请求
 * @author hong
 *
 */
public class BatCreateOrder implements Serializable {

	private String amount;
	private String extraAmount;
	private String cardNo;
	private String rechargeOn;
	private String masterTranLogId;
	private String mixFlag;//混合支付flag 1 表示混合支付
	private String masterPayAmount;
	private String inputAmount;
	private String isPayComingForm;
	private String payBatType; //1 微信 2支付宝 3qq 4百度 
	private String captcha;//验证码
	private String commoncashierOrderId;//销售订单号 wu@[20151012]
	private String authCode;//扫码code @Hwc 2015年12月04日10:47:19
	private List<String> ids;
	private String mnFlag;//主被扫标记 (micro:被扫;native:主扫)
	
	private BatWeimobTicket wmHxInfo;//第三方支付微盟券实体
	
	/** 被扫标示 与{@link #mnFlag}关联*/
	public static final String MN_FLAG_MICRO = "micro";
	/** 主扫标示 与{@link #mnFlag}关联*/
	public static final String MN_FLAG_NATIVE = "native";
	
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

	public String getPayBatType() {
		return payBatType;
	}

	public void setPayBatType(String payBatType) {
		this.payBatType = payBatType;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public void setCommoncashierOrderId(String commoncashierOrderId) {
		this.commoncashierOrderId = commoncashierOrderId;
	}

	public String getCommoncashierOrderId() {
		return commoncashierOrderId;
	}
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getMnFlag() {
		return mnFlag;
	}

	public void setMnFlag(String mnFlag) {
		this.mnFlag = mnFlag;
	}
	
	public BatWeimobTicket getWmHxInfo() {
		return wmHxInfo;
	}

	public void setWmHxInfo(BatWeimobTicket wmHxInfo) {
		this.wmHxInfo = wmHxInfo;
	}

}
