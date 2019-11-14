package com.wizarpos.pay.cashier.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.model.TransactionInfo;

/**
 * 交易描述 Created by wu on 2015/8/2ß.
 */
public class TransactionInfo2 implements Serializable {
	protected int transactionType;// 交易类型
	protected String tranId;// 订单号 (线上支付订单号)
	protected String transTime;// 付款时间
	protected String giftPoints;// 赠送积分
	protected boolean isOffline;// 是否是离线模式
	protected String tranLogId = "";// 流水号
									// 流水号和订单号的区别在于,订单号未支付就已经生成.而流水号需要等到交易结束之后才生成.订单号不是每次交易都会生成.
	protected String body;// 商品信息

	protected boolean isNeedPrint = true;// 是否需要打印
	protected boolean isNeedTicket;// 是否需要发券

	// 金额精确到分
	protected int initAmount;// 初始金额 (传入金额)
	protected int shouldAmount;// 应付金额
	protected int realAmount;// 实付金额
	protected int reduceAmount;// 扣减金额
	protected int changeAmount;// 找零金额
	protected int discountAmount;// 折扣扣减金额

	protected String authCode;// 交易特征码(线上支付,被扫支付)
	protected String thirdTradeNo;// 第三方交易号

	protected String cardType;
	protected String cardNo;
	protected String rechargeOn;
	protected String isPayComingForm;

	public static final String FLAG_MIX = "1";
	protected String mixFlag;// 是否是混合支付
	protected String mixTranLogId;// 混合支付主单号
	protected int mixInitAmount;// 混合支付总金额

	private int discountPrecent = 100;// 折扣扣减比例

	private List<TransactionInfo2> payedTransactionInfo = new ArrayList<TransactionInfo2>();// 已经支付的信息列表

	public List<TransactionInfo2> getPayedTransactionInfo() {
		return payedTransactionInfo;
	}

	public void setPayedTransactionInfo(
			List<TransactionInfo2> payedTransactionInfo) {
		this.payedTransactionInfo = payedTransactionInfo;
	}

	public void addRealAmount(int realAmount) {
		this.realAmount = this.realAmount + realAmount;
		this.shouldAmount = this.shouldAmount - realAmount;
	}

	public void addReduceAmount(int reduceAmount) {
		this.reduceAmount = this.reduceAmount + reduceAmount;
		this.shouldAmount = this.shouldAmount - reduceAmount;
	}

	public void addDiscountAmount(int discountAmount) {
		this.discountAmount = this.discountAmount + discountAmount;
		this.shouldAmount = this.shouldAmount - discountAmount;
	}
	
	public int getDiscountPrecent() {
		return discountPrecent;
	}

	public void setDiscountPrecent(int discountPrecent) {
		if (discountPrecent < 0) {
			discountPrecent = 0;
		} else if (discountPrecent > 100) {
			discountPrecent = 100;
		}
		this.discountPrecent = discountPrecent;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getGiftPoints() {
		return giftPoints;
	}

	public void setGiftPoints(String giftPoints) {
		this.giftPoints = giftPoints;
	}

	public boolean isOffline() {
		return isOffline;
	}

	public void setOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}

	public String getTranLogId() {
		return tranLogId;
	}

	public void setTranLogId(String tranLogId) {
		this.tranLogId = tranLogId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isNeedPrint() {
		return isNeedPrint;
	}

	public void setNeedPrint(boolean isNeedPrint) {
		this.isNeedPrint = isNeedPrint;
	}

	public boolean isNeedTicket() {
		return isNeedTicket;
	}

	public void setNeedTicket(boolean isNeedTicket) {
		this.isNeedTicket = isNeedTicket;
	}

	public int getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(int initAmount) {
		this.initAmount = initAmount;
	}

	public int getShouldAmount() {
		return shouldAmount;
	}

	public void setShouldAmount(int shouldAmount) {
		this.shouldAmount = shouldAmount;
	}

	public int getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(int realAmount) {
		this.realAmount = realAmount;
	}

	public int getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(int reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public int getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(int changeAmount) {
		this.changeAmount = changeAmount;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
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

	public String getRechargeOn() {
		return rechargeOn;
	}

	public void setRechargeOn(String rechargeOn) {
		this.rechargeOn = rechargeOn;
	}

	public String getIsPayComingForm() {
		return isPayComingForm;
	}

	public void setIsPayComingForm(String isPayComingForm) {
		this.isPayComingForm = isPayComingForm;
	}

	public String getMixFlag() {
		return mixFlag;
	}

	public void setMixFlag(String mixFlag) {
		this.mixFlag = mixFlag;
	}

	public String getMixTranLogId() {
		return mixTranLogId;
	}

	public void setMixTranLogId(String mixTranLogId) {
		this.mixTranLogId = mixTranLogId;
	}

	public int getMixInitAmount() {
		return mixInitAmount;
	}

	public void setMixInitAmount(int mixInitAmount) {
		this.mixInitAmount = mixInitAmount;
	}

}
