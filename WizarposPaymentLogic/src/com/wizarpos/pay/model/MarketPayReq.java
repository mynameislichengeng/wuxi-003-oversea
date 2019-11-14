package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 
 * @author hong
 * 营销活动请求实体
 *
 */
public class MarketPayReq implements Serializable {
//	(1、现金支付  2、会员卡支付  3、微信支付  4、支付宝支付  5、银行卡支付  6、百度钱包 7 、QQ钱包)
	public final static String PAY_MODE_CASH = "1";
	public final static String PAY_MODE_MEMBER_CARD = "2";
	public final static String PAY_MODE_WEIXIN = "3";
	public final static String PAY_MODE_ALIPAY = "4";
	public final static String PAY_MODE_BANKCARD = "5";
	public final static String PAY_MODE_BAIDU = "6";
	public final static String PAY_MODE_TENPAY = "7";
	private String mid;
	private String cardNo;//卡号
	private String payMethod;//支付方式(1、现金支付  2、会员卡支付  3、微信支付  4、支付宝支付  5、银行卡支付  6、百度钱包 7 、QQ钱包)
	private String payAmount;//支付金额  
	private String reChangeOn;//是否是会员卡充值
	public String getReChangeOn() {
		return reChangeOn;
	}
	public void setReChangeOn(String reChangeOn) {
		this.reChangeOn = reChangeOn;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	
	
}
