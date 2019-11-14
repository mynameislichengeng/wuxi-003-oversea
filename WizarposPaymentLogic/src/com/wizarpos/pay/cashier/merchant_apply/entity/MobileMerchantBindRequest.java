package com.wizarpos.pay.cashier.merchant_apply.entity;

import java.io.Serializable;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-30 上午9:20:25
 * @Description:商户进件--加入已有商户
 */
public class MobileMerchantBindRequest implements Serializable{
	
	private String bizTel;//手机号
	private String mid;//商户号
	private String imei;//imei
	private String vcode;//验证码
	// 商户进件标示 1:用于商户进件 
	private String MerchantBankApplyFlag = "1";
	
	public String getBizTel() {
		return bizTel;
	}
	public void setBizTel(String bizTel) {
		this.bizTel = bizTel;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMerchantBankApplyFlag() {
		return MerchantBankApplyFlag;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	
}
