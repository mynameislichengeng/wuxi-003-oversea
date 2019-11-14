package com.wizarpos.pay.cashier.model;

import java.io.Serializable;

public class SaleOrder implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private String id;
	private String sn;
	private String order_no;// 销售单号
	private String mid;
	private String storage_id;// 仓库标识
	private String order_time;// 交易时间
	private String amount;// 应付金额
	private String operator_id;// 操作员标识
	private String order_flag;// 单据标识（0销售单，1退货单,2已退单 当前库里记录0/2状态）
	private String order_id;// 引用的销售单id(退单)
	private String remark;// 备注
	private String last_time;// 最后修改时间
	private String order_source;// 订单来源('1' pos销售订单 '2' 微信订单)
	private String master_tran_log_id;// 支付交易流水号
	private String offline_tran_log_id;// 离线销售流水号(与pay_tran_log的支付交易流水号关联)
	private String order_catalog_id;// 订单类型编号
	private String day_order_seq;// 小票打印序号
	private String operator_name;// 操作员姓名
	private String pay_status;// 销售单状态(0未支付1已支付)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getStorage_id() {
		return storage_id;
	}

	public void setStorage_id(String storage_id) {
		this.storage_id = storage_id;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public String getOrder_flag() {
		return order_flag;
	}

	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}

	public String getOrder_source() {
		return order_source;
	}

	public void setOrder_source(String order_source) {
		this.order_source = order_source;
	}

	public String getMaster_tran_log_id() {
		return master_tran_log_id;
	}

	public void setMaster_tran_log_id(String master_tran_log_id) {
		this.master_tran_log_id = master_tran_log_id;
	}

	public String getOffline_tran_log_id() {
		return offline_tran_log_id;
	}

	public void setOffline_tran_log_id(String offline_tran_log_id) {
		this.offline_tran_log_id = offline_tran_log_id;
	}

	public String getOrder_catalog_id() {
		return order_catalog_id;
	}

	public void setOrder_catalog_id(String order_catalog_id) {
		this.order_catalog_id = order_catalog_id;
	}

	public String getDay_order_seq() {
		return day_order_seq;
	}

	public void setDay_order_seq(String day_order_seq) {
		this.day_order_seq = day_order_seq;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
