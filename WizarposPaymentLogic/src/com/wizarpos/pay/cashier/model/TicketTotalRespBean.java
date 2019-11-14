package com.wizarpos.pay.cashier.model;

import java.io.Serializable;

/**
 * 首页券统计返回实体
 * @author wu
 *
 */
public class TicketTotalRespBean implements Serializable {

	private String merchantMemNum;//新增会员数
	private String monthTranAmount;//本月收款金额
	private String ticketTranAmount;//券交易金额

	public String getMerchantMemNum() {
		return merchantMemNum;
	}

	public void setMerchantMemNum(String merchantMemNum) {
		this.merchantMemNum = merchantMemNum;
	}

	public String getMonthTranAmount() {
		return monthTranAmount;
	}

	public void setMonthTranAmount(String monthTranAmount) {
		this.monthTranAmount = monthTranAmount;
	}

	public String getTicketTranAmount() {
		return ticketTranAmount;
	}

	public void setTicketTranAmount(String ticketTranAmount) {
		this.ticketTranAmount = ticketTranAmount;
	}

}
