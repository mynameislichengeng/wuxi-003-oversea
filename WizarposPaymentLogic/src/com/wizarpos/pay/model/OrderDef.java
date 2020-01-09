package com.wizarpos.pay.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the pay_order_def database table.
 */
public class OrderDef implements Serializable {

	public final static int STATE_NOT_PAY = 1;// 未支付
	public final static int STATE_PAYED = 2;// 已支付
	public final static int STATE_REVOKED = 3;// 已撤销
	public final static int STATE_CLOSED = 4;// 已关闭
	public final static int STATE_TIME_OUT = -1;//交易超时
	
	public final static String TIME_OUT = "O";

	public static String getOrderStateDes(int state) {
		switch (state) {
		case STATE_NOT_PAY:
			return "未支付";
		case STATE_PAYED:
			return "已支付";
		case STATE_REVOKED:
			return "已撤销";
		case STATE_CLOSED:
			return "已关闭";
		case STATE_TIME_OUT:
			return "交易已关闭，请重新刷新二维码支付";
		default:
			return "订单状态未知";
		}
	}

	/**
	 * 流水号 (与支付交易流水号取号方式相同)
	 */
	private String id;

	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 金额
	 */
	private int amount;
	/**
	 * 订单内容
	 */
	private String body;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 身份标志(0操作员/1管理员)
	 */
	private String operatorId;

	/**
	 * 订单状态
	 */
	private int state;
	/**
	 * 订单概述
	 */
	private String subject;
	/**
	 * 扣减金额(折扣)
	 */
	private Integer extraAmount;
	/**
	 * 应付金额
	 */
	private Integer inputAmount;
	/**
	 * 折扣率
	 */
	private Integer disCount;

	/**
	 * 会员卡号
	 */
	private String cardNo;
	/**
	 * 发券模式
	 */
	private String issueTicketMode;
	/**
	 * 是否为会员卡充值(0否 1是)
	 */
	private String rechargeOn;
	/**
	 * 订单类型
	 */
	private String payType;

	private Integer tranPoints;
	/**
	 * 是否混合支付(0否 1是)
	 */
	private String mixedFlag;

	/**
	 * 混合支付主流水单标识
	 */
	private String masterTranLogId;
	/**
	 * 第三方交易号
	 */
	private String thirdTradeNo;

	private String ticketNums;
	private Integer pointsGive;
	private Integer marketAmount;
	/**
	 * 支付宝用户名等参数
	 */
	private String thirdExtId;
	private String thirdExtName;
	private String payTime;
	/**
	 * 密钥
	 */
	private String key;
	private String ticketInfo;
	
	private String timeOutFlag;//是否交易超时 后续需自动撤销 "O"(大写字母o) 为超时

	private String settlementCurrency;
	private String settlementAmount;

	public String getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(String ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getExtraAmount() {
		return extraAmount;
	}

	public void setExtraAmount(Integer extraAmount) {
		this.extraAmount = extraAmount;
	}

	public Integer getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(Integer inputAmount) {
		this.inputAmount = inputAmount;
	}

	public Integer getDisCount() {
		return disCount;
	}

	public void setDisCount(Integer disCount) {
		this.disCount = disCount;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIssueTicketMode() {
		return issueTicketMode;
	}

	public void setIssueTicketMode(String issueTicketMode) {
		this.issueTicketMode = issueTicketMode;
	}

	public String getRechargeOn() {
		return rechargeOn;
	}

	public void setRechargeOn(String rechargeOn) {
		this.rechargeOn = rechargeOn;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getTranPoints() {
		return tranPoints;
	}

	public void setTranPoints(Integer tranPoints) {
		this.tranPoints = tranPoints;
	}

	public String getMixedFlag() {
		return mixedFlag;
	}

	public void setMixedFlag(String mixedFlag) {
		this.mixedFlag = mixedFlag;
	}

	public String getMasterTranLogId() {
		return masterTranLogId;
	}

	public void setMasterTranLogId(String masterTranLogId) {
		this.masterTranLogId = masterTranLogId;
	}

	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}

	public String getTicketNums() {
		return ticketNums;
	}

	public void setTicketNums(String ticketNums) {
		this.ticketNums = ticketNums;
	}

	public Integer getPointsGive() {
		return pointsGive;
	}

	public void setPointsGive(Integer pointsGive) {
		this.pointsGive = pointsGive;
	}

	public Integer getMarketAmount() {
		return marketAmount;
	}

	public void setMarketAmount(Integer marketAmount) {
		this.marketAmount = marketAmount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTimeOutFlag() {
		return timeOutFlag;
	}

	public void setTimeOutFlag(String timeOutFlag) {
		this.timeOutFlag = timeOutFlag;
	}

	public String getThirdExtId() {
		return thirdExtId;
	}

	public void setThirdExtId(String thirdExtId) {
		this.thirdExtId = thirdExtId;
	}

	public String getThirdExtName() {
		return thirdExtName;
	}

	public void setThirdExtName(String thirdExtName) {
		this.thirdExtName = thirdExtName;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getSettlementCurrency() {
		return settlementCurrency;
	}

	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}

	public String getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(String settlementAmount) {
		this.settlementAmount = settlementAmount;
	}
}