package com.wizarpos.pay.cashier.thrid_app_controller.model;

import java.io.Serializable;

public class ThirdAppTransactionEXJsonResponse implements Serializable {

	private String extraAmount;
	private String inputAmount;
	private String masterTranLogId;
	private String offline;
	private String offlineTranLogId;
	private String payAmount;
	private String payMode;
	private String payModeDesc;
	private String reduceAmount;
	private String returnAmount;
	private String tradeNo;
	private String cardNo;//会员卡卡号
	private String cardBalance;//@hong 会员卡余额
	private String ticketReduceAmount;//券扣减总计金额
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardBalance() {
		return cardBalance;
	}

	public void setCardBalance(String cardBalance) {
		this.cardBalance = cardBalance;
	}

	public String getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(String extraAmount) {
		this.extraAmount = extraAmount;
	}

	public String getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(String inputAmount) {
		this.inputAmount = inputAmount;
	}

	public String getMasterTranLogId() {
		return masterTranLogId;
	}

	public void setMasterTranLogId(String masterTranLogId) {
		this.masterTranLogId = masterTranLogId;
	}

	public String getOffline() {
		return offline;
	}

	public void setOffline(String offline) {
		this.offline = offline;
	}

	public String getOfflineTranLogId() {
		return offlineTranLogId;
	}

	public void setOfflineTranLogId(String offlineTranLogId) {
		this.offlineTranLogId = offlineTranLogId;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayModeDesc() {
		return payModeDesc;
	}

	public void setPayModeDesc(String payModeDesc) {
		this.payModeDesc = payModeDesc;
	}

	public String getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTicketReduceAmount() {
		return ticketReduceAmount;
	}

	public void setTicketReduceAmount(String ticketReduceAmount) {
		this.ticketReduceAmount = ticketReduceAmount;
	}
	
	
}
