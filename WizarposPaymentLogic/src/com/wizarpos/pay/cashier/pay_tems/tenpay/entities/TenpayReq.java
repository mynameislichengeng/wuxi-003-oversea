package com.wizarpos.pay.cashier.pay_tems.tenpay.entities;

import java.io.Serializable;
/**
 * 
 * QQ钱包支付请求实体
 * @author Administrator
 *
 */

public class TenpayReq implements Serializable{
	private String out_trade_no;//订单号
	private String total_fee;
	private String authCode;
	private String sp_device_id;
	private String body;
	private String rechargeOn;
	private String orderId;//销售传入的销售单id wu@[20151012]
	
	public String getReChargeOn() {
		return rechargeOn;
	}
	public void setReChargeOn(String rechargeOn) {
		this.rechargeOn = rechargeOn;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getSp_device_id() {
		return sp_device_id;
	}
	public void setSp_device_id(String sp_device_id) {
		this.sp_device_id = sp_device_id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}
}
