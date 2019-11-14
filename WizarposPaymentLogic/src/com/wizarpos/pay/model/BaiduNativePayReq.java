package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 百度扫码支付请求参数 Created by wu on 2015/6/29.
 */
public class BaiduNativePayReq implements Serializable {

	private String sp_no; // 商户号
	private String order_create_time; // 订单创建时间
	private String order_no; // 订单号
	private String goods_name; // 商品名称
	private String total_amount; // 交易总金额
	private String expire_time; // 订单失效时间
	private String pay_code;

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
