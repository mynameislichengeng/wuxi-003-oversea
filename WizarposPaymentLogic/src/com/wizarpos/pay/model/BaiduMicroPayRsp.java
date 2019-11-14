package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 百度被扫支付返回参数 Created by wu on 2015/6/29.
 */
public class BaiduMicroPayRsp implements Serializable {
	public static final String CODE_WAITE = "69556";
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
