package com.wizarpos.pay.cashier.model;

import java.io.Serializable;
import java.util.List;

public class PayTranRsp implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private String masterTranLogId;
	private List<SaleOrder> saleOrder;
	private List<TranLog> payTran;
	private int mixedFlag; // 是否是 混合支付 0/非混合支付 1混合支付
	private String refundAmount;//撤销金额
	public String getMasterTranLogId() {
		return masterTranLogId;
	}

	public void setMasterTranLogId(String masterTranLogId) {
		this.masterTranLogId = masterTranLogId;
	}

	public int getMixedFlag() {
		return mixedFlag;
	}

	public void setMixedFlag(int mixedFlag) {
		this.mixedFlag = mixedFlag;
	}

	public List<SaleOrder> getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(List<SaleOrder> saleOrder) {
		this.saleOrder = saleOrder;
	}

	public List<TranLog> getPayTran() {
		return payTran;
	}

	public void setPayTran(List<TranLog> payTran) {
		this.payTran = payTran;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
}
