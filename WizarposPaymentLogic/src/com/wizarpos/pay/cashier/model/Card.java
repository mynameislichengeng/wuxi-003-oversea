package com.wizarpos.pay.cashier.model;

import java.io.Serializable;

public class Card implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3541821601892154566L;
	private String id ;
	/** 注销*/
	private boolean canceled;
	private String username;
	private String cardName;
	private String mobileNo;
	private int balance;
	private String cardType = "";
	private String cardNo;
	/** 赠送积分*/
	private String tranPoints;
	/** 时间*/
	private String tranTime;

	private String payAmount;
	//应付金额
	private String tranAmount;
	//折扣金额
	private String disAmount;
	//折扣率
	private String disCount;
	//冻结
	private boolean freeze;
	//积分
	private String points;
	
	private String payModeDesc;
	
	//发卡时间
	private long createTime;
	/** 处理交易笔数*/
	private String dealCount;
	/** 余额 元*/
	private String fbalance;
	/** 会员标签*/
	private String labelNames;
	/** 会员卡中券的数量*/
	private String ticketCount;
	/** 会员卡密码*/
	private String passwd;
	/** 会员卡类型*/
	private String cardMediaType;
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getTranPoints() {
		return tranPoints;
	}
	public void setTranPoints(String tranPoints) {
		this.tranPoints = tranPoints;
	}
	public String getTranTime() {
		return tranTime;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	public String getTranAmount() {
		return tranAmount;
	}
	public void setTranAmount(String tranAmount) {
		this.tranAmount = tranAmount;
	}
	public String getDisAmount() {
		return disAmount;
	}
	public void setDisAmount(String disAmount) {
		this.disAmount = disAmount;
	}
	public String getDisCount() {
		return disCount;
	}
	public void setDisCount(String disCount) {
		this.disCount = disCount;
	}
	public boolean isFreeze() {
		return freeze;
	}
	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getPayModeDesc() {
		return payModeDesc;
	}
	public void setPayModeDesc(String payModeDesc) {
		this.payModeDesc = payModeDesc;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getDealCount() {
		return dealCount;
	}
	public void setDealCount(String dealCount) {
		this.dealCount = dealCount;
	}
	public String getFbalance() {
		return fbalance;
	}
	public void setFbalance(String fbalance) {
		this.fbalance = fbalance;
	}
	public String getLabelNames() {
		return labelNames;
	}
	public void setLabelNames(String labelNames) {
		this.labelNames = labelNames;
	}
	public String getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(String ticketCount) {
		this.ticketCount = ticketCount;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getCardMediaType() {
		return cardMediaType;
	}
	public void setCardMediaType(String cardMediaType) {
		this.cardMediaType = cardMediaType;
	}
	
	
}
