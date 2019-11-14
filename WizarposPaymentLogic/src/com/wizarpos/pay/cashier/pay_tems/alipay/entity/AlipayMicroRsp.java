package com.wizarpos.pay.cashier.pay_tems.alipay.entity;

import java.io.Serializable;

/**
 * 
 * @author hong 支付宝支付返回的数据
 * 
 */
public class AlipayMicroRsp implements Serializable {
	public static final String CODE_WAITE = "ORDER_SUCCESS_PAY_INPROCESS";
	public static final String CODE_SUCCESS = "0";

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

	private String ret;
	private String msg;
	private String thirdTradeNo;

}
