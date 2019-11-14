package com.wizarpos.pay.cashier.model;

import java.util.Date;

public class TicketPublishBean {

	private static final long serialVersionUID = 6462868570477526081L;
	private String id;
	private String mid;
	private String merchantId;
	private Integer payId;
	private Integer cardType;
	private String cardNo;
	private Date startTime;
	private Date expriyTime;
	private Boolean validFlag;
	private Date cancelTime;
	private String ticketDefId;
	private String ticketCode;
	private Date createTime;
	private String description;
	private String type;//判断券是否属于券包，0 不属于  1 属于

	private String ticketName;
	private Integer balance;
	private Boolean reusedFlag;
	private Boolean usedFlag;
	private Integer validPeriod;
	private Integer activeAmount;
	private Integer usingBalance = 0;

	public boolean usableFlag = true;
	public boolean invalidFlag = false;

	public TicketDef ticketDef;

	public String wxAdded;
	public String ids = "0";

	public TicketDef getTicketDef() {
		return ticketDef;
	}

	public void setTicketDef(TicketDef ticketDef) {
		this.ticketDef = ticketDef;
	}

	public TicketPublishBean() {
	}

	public TicketPublishBean(String id, String merchantId, Integer cardType, String cardNo) {
		this.id = id;
		this.merchantId = merchantId;
		this.cardType = cardType;
		this.cardNo = cardNo;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getCardType() {
		return this.cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExpriyTime() {
		return this.expriyTime;
	}

	public void setExpriyTime(Date expriyTime) {
		this.expriyTime = expriyTime;
	}

	public Boolean getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Boolean validFlag) {
		this.validFlag = validFlag;
	}

	public Date getCancelTime() {
		return this.cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getTicketDefId() {
		return this.ticketDefId;
	}

	public void setTicketDefId(String ticketDefId) {
		this.ticketDefId = ticketDefId;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTicketName() {
		return this.ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public Integer getBalance() {
		return this.balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Boolean getReusedFlag() {
		return this.reusedFlag;
	}

	public void setReusedFlag(Boolean reusedFlag) {
		this.reusedFlag = reusedFlag;
	}

	public Boolean getUsedFlag() {
		return this.usedFlag;
	}

	public void setUsedFlag(Boolean usedFlag) {
		this.usedFlag = usedFlag;
	}

	public Integer getValidPeriod() {
		return this.validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public Integer getActiveAmount() {
		return this.activeAmount;
	}

	public void setActiveAmount(Integer activeAmount) {
		this.activeAmount = activeAmount;
	}

	public Integer getUsingBalance() {
		return usingBalance;
	}

	public void setUsingBalance(Integer usingBalance) {
		this.usingBalance = usingBalance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public String getWxAdded() {
		return wxAdded;
	}

	public void setWxAdded(String wxAdded) {
		this.wxAdded = wxAdded;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
