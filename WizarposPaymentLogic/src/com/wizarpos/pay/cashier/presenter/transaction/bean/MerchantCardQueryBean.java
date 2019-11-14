package com.wizarpos.pay.cashier.presenter.transaction.bean;

import java.io.Serializable;

/**
 * 
 * @Author: Huangweicai
 * @date 2016-1-26 上午10:42:23
 * @Description:341请求接口请求参数对象
 */
public class MerchantCardQueryBean implements Serializable{
	private String cardNo;
	private String mobileNo;
	private String username;
	private String birthday;
//	卡类型
//	99 微信公众号
//	98 支付宝服务窗
//	97 QQ公众号 
//	96 新浪微博 
//	95 百度糯米
//	1  实体会员卡
//	2   电子会员卡
	private String cardType;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
