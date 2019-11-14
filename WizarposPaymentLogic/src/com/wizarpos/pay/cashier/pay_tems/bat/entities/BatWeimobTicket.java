package com.wizarpos.pay.cashier.pay_tems.bat.entities;

import java.io.Serializable;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-11 下午5:17:26
 * @Description:微盟第三方支付用券
 */
public class BatWeimobTicket implements Serializable{
	private String mid;
	private String sn;
	private long amount;//交易金额
	private String confirmCardCode;//相当于券号
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getConfirmCardCode() {
		return confirmCardCode;
	}
	public void setConfirmCardCode(String confirmCardCode) {
		this.confirmCardCode = confirmCardCode;
	}
	
	
}
