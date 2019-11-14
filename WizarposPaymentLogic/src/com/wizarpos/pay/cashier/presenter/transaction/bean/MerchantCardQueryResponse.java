package com.wizarpos.pay.cashier.presenter.transaction.bean;

import java.io.Serializable;


/**
 * 
 * @Author: Huangweicai
 * @date 2016-1-26 上午10:42:23
 * @Description:341请求接口返回参数对象
 */
public class MerchantCardQueryResponse implements Serializable{

    private long activeTime;
    private int balance;
    private boolean canceled;
    private String cardMediaType;
    private String cardNo;
    private String cardType;
    private String cardTypeDesc;
    private long createTime;
    private int dealCount;
    private long expriyTime;
    private double fbalance;
    private boolean freeze;
    private String id;
    private String labelIds;
    private String mobileNo;
    private String openId;
    private int points;
    private int totalPoints;
    private String username;
    private String wxCallbackTicketno;
    
    private boolean isChecked = false;

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setCardMediaType(String cardMediaType) {
        this.cardMediaType = cardMediaType;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setDealCount(int dealCount) {
        this.dealCount = dealCount;
    }

    public void setExpriyTime(long expriyTime) {
        this.expriyTime = expriyTime;
    }

    public void setFbalance(double fbalance) {
        this.fbalance = fbalance;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }


    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWxCallbackTicketno(String wxCallbackTicketno) {
        this.wxCallbackTicketno = wxCallbackTicketno;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public String getCardMediaType() {
        return cardMediaType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getDealCount() {
        return dealCount;
    }

    public long getExpriyTime() {
        return expriyTime;
    }

    public double getFbalance() {
        return fbalance;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public String getId() {
        return id;
    }

    public String getLabelIds() {
        return labelIds;
    }


    public String getMobileNo() {
        return mobileNo;
    }

    public String getOpenId() {
        return openId;
    }

    public int getPoints() {
        return points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getUsername() {
        return username;
    }

    public String getWxCallbackTicketno() {
        return wxCallbackTicketno;
    }

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCardTypeDesc() {
		return cardTypeDesc;
	}

	public void setCardTypeDesc(String cardTypeDesc) {
		this.cardTypeDesc = cardTypeDesc;
	}

}
