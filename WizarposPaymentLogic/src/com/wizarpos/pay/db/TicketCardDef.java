package com.wizarpos.pay.db;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "ticket_card_def")
public class TicketCardDef {

	private String id;
	private int mid;
	private String merchantId;
	private int payId;
	private String ticketName;
	private String ticketCode;
	private int balance;
	private int reusedFlag;
	private int usedFlag;
	private int validPeriod;
	private int activeAmount;
	private String description;
	private long createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getReusedFlag() {
		return reusedFlag;
	}

	public void setReusedFlag(int reusedFlag) {
		this.reusedFlag = reusedFlag;
	}

	public int getUsedFlag() {
		return usedFlag;
	}

	public void setUsedFlag(int usedFlag) {
		this.usedFlag = usedFlag;
	}

	public int getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(int validPeriod) {
		this.validPeriod = validPeriod;
	}

	public int getActiveAmount() {
		return activeAmount;
	}

	public void setActiveAmount(int activeAmount) {
		this.activeAmount = activeAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
