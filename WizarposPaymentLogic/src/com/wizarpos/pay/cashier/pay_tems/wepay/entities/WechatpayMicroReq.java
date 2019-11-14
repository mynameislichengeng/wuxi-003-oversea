package com.wizarpos.pay.cashier.pay_tems.wepay.entities;

import java.io.Serializable;

/**
 * 微信消费请求实体 2015-7-14 20:58:39
 * 
 * @author hong
 * 
 */
public class WechatpayMicroReq implements Serializable {
	private String body;// 商品描述
	private String total_fee;// 总金额
	private String spbill_create_ip;// 终端ip
	private String out_trade_no;// 商户订单号
	private String authCode;// 授权码
	private String TERMINAL_ID;// 终端号
	

	public String getTERMINAL_ID() {
		return TERMINAL_ID;
	}

	public void setTERMINAL_ID(String tERMINAL_ID) {
		TERMINAL_ID = tERMINAL_ID;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

}
