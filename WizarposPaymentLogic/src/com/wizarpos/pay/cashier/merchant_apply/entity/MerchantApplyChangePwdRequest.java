package com.wizarpos.pay.cashier.merchant_apply.entity;

import java.io.Serializable;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-30 上午10:57:01
 * @Description:商户进件密码修改bean
 */
public class MerchantApplyChangePwdRequest implements Serializable{
	
	private String bizTel;//手机号
	private String mid;//商户号
	private String newPassword;//新密码
	private String oldPassword;//当修改密码时,给到服务端;重置密码时不用给出
	
	// 商户进件标示 1:用于商户进件 
	private String MerchantBankApplyFlag = "1";
	
	public String getBizTel() {
		return bizTel;
	}
	public void setBizTel(String bizTel) {
		this.bizTel = bizTel;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMerchantBankApplyFlag() {
		return MerchantBankApplyFlag;
	}
}
