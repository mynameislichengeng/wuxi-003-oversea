package com.wizarpos.pay.cashier.pay_tems.bat.entities;

import java.io.Serializable;

public class BatReq implements Serializable {
	//微信
	private String body;// 商品描述
	private String total_fee;// 总金额
	private String spbill_create_ip;// 终端ip
	private String out_trade_no;// 商户订单号
	private String authCode;// 授权码
	private String TERMINAL_ID;// 终端号
	private String captcha;//验证码
	//支付宝
	private String outTradeNo;
	private String subject;
	private String totalFee;
	private String notifyUrl;
	private String dynamic_id_type = "qrcode";
	private String dynamic_id;
//	private String authCode;
	private String extend_params;// 公用业务扩展信息
//	private String TERMINAL_ID;// 终端设备号extend_params
	private String payeeTermId;// 收款方终端号 channel_parameters	
	//QQ钱包
//	private String out_trade_no;//订单号
//	private String total_fee;
//	private String authCode;
	private String sp_device_id;
//	private String body;
	private String rechargeOn;
	//百度钱包
	private String sp_no; // 商户号
	private String order_create_time; // 订单创建时间
	private String order_no; // 订单号
	private String goods_name; // 商品名称
	private String total_amount; // 交易总金额
	private String expire_time; // 订单失效时间
	private String pay_code;	
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
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
	public String getTERMINAL_ID() {
		return TERMINAL_ID;
	}
	public void setTERMINAL_ID(String tERMINAL_ID) {
		TERMINAL_ID = tERMINAL_ID;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getDynamic_id_type() {
		return dynamic_id_type;
	}
	public void setDynamic_id_type(String dynamic_id_type) {
		this.dynamic_id_type = dynamic_id_type;
	}
	public String getDynamic_id() {
		return dynamic_id;
	}
	public void setDynamic_id(String dynamic_id) {
		this.dynamic_id = dynamic_id;
	}
	public String getExtend_params() {
		return extend_params;
	}
	public void setExtend_params(String extend_params) {
		this.extend_params = extend_params;
	}
	public String getPayeeTermId() {
		return payeeTermId;
	}
	public void setPayeeTermId(String payeeTermId) {
		this.payeeTermId = payeeTermId;
	}
	public String getSp_device_id() {
		return sp_device_id;
	}
	public void setSp_device_id(String sp_device_id) {
		this.sp_device_id = sp_device_id;
	}
	public String getRechargeOn() {
		return rechargeOn;
	}
	public void setRechargeOn(String rechargeOn) {
		this.rechargeOn = rechargeOn;
	}
	public String getSp_no() {
		return sp_no;
	}
	public void setSp_no(String sp_no) {
		this.sp_no = sp_no;
	}
	public String getOrder_create_time() {
		return order_create_time;
	}
	public void setOrder_create_time(String order_create_time) {
		this.order_create_time = order_create_time;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	public String getPay_code() {
		return pay_code;
	}
	public void setPay_code(String pay_code) {
		this.pay_code = pay_code;
	}
}
