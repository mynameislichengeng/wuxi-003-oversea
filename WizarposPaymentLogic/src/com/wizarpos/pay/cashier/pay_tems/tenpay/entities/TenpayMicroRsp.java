package com.wizarpos.pay.cashier.pay_tems.tenpay.entities;

import java.io.Serializable;

/**
 * 
 * @author hong QQ钱包支付返回的数据
 * 
 */
public class TenpayMicroRsp implements Serializable {
	public static final String CODE_WAITE = "66227005";//需支付密码
	public static final String CODE_SUCCESS = "0";
	private String ret;
	private String msg;
	private String thirdTradeNo;
	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}

}
