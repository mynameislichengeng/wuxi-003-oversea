package com.wizarpos.pay.cashier.pay_tems.bat.entities;

import java.io.Serializable;

/**
 * @Author: yaosong
 * @date 2015-10-30 下午3:00:30
 * @Description:BAT移动支付在会员V1_4 接口873,874使用
 */
public class BatNewReq implements Serializable {
	//微信
	private String auth_code;// 被扫支付码
	private String out_trade_no;// 商户订单号
	private String goods_info;//商品名称
	private String total_fee;// 支付金额
	private String spbill_create_ip;// 机器ip
	private String terminal_no;// 终端设备号
	//支付宝
//	private String auth_code;// 被扫支付码
	private String store_id;// 门店编号
	private String payee_term_id;// 收款方终端号
//	private String terminal_no;// 终端设备号
//	private String out_trade_no;// 商户订单号
//	private String goods_info;//商品名称
//	private String total_fee;// 支付金额
	//QQ钱包
//	private String auth_code;// 被扫支付码
//	private String out_trade_no;// 商户订单号
//	private String goods_info;//商品名称
//	private String total_fee;// 支付金额
//	private String terminal_no;// 终端设备号
	//百度钱包
//	private String auth_code;// 被扫支付码
//	private String out_trade_no;// 商户订单号
//	private String goods_info;//商品名称
//	private String total_fee;// 支付金额
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getGoods_info() {
		return goods_info;
	}
	public void setGoods_info(String goods_info) {
		this.goods_info = goods_info;
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
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getPayee_term_id() {
		return payee_term_id;
	}
	public void setPayee_term_id(String payee_term_id) {
		this.payee_term_id = payee_term_id;
	}
	public String getTerminal_no() {
		return terminal_no;
	}
	public void setTerminal_no(String terminal_no) {
		this.terminal_no = terminal_no;
	}
}