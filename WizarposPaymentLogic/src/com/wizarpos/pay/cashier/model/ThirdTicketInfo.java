package com.wizarpos.pay.cashier.model;

import java.io.Serializable;

/**
 * 第三方券信息实体 Created by wu on 2015/5/11 0011.
 */
public class ThirdTicketInfo implements Serializable {
	private String name;
	private String ticketType;
	private String amount;
	private String expiryTime;
	private String code;
	private String callBackUrl;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
}
