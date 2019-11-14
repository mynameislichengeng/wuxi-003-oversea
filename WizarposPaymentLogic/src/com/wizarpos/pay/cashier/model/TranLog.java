package com.wizarpos.pay.cashier.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易记录
 * 
 * @author wu
 */
public class TranLog implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private String id;
	private String sn;
	private String mid;// 商户号
	private String pid;// 发行商户号
	private Date tran_time;
	/*
	 * transCode 700 银行卡支付 302 会员卡充值 304 会员卡消费 300 会员卡发卡 831 会员卡冻结/会员卡解冻 830
	 * 会员卡换卡 310 会员卡消费撤销/会员卡充值撤销 400 现金支付 814 微信支付(含微店订单微信支付) 813 支付宝支付 820
	 * 第三方支付撤销(支付宝/微信) 306 会员卡注销 315 积分赠送 316 积分消费 402
	 * 现金支付/离线现金交易(根据offlineTranLogId有无来判定) 401 其它支付（新增） 405 现金交易撤销/离线现金交易撤销
	 */
	private String tran_code;// 交易码
	private String cash_type;// 其他收银类型码
	private int tran_amount;// 交易金额
	private int extra_amount;// 附加金额．充值，送的金额．消费，折扣扣减金额
	private int input_amount;// 输入金额
	private String card_no;// 卡号
	private String order_no;// 订单号(微店订单,支付宝支付/微信支付订单)
	private String ticket_info_id;// 卡包卡券实例id
	private String relate_tran_log_id;// 关联交易撤销流水号
	private String canceled_flag;// 是否有效
	private String operator_id;// 操作员号
	private String tran_mark;// 简单标记
	private String tran_desc;// 交易描述
	private String offline_tran_log_id;// 离线交易ID(POS上传)
	private String ticket_tran_log_id;// 卡券关联会员卡交易流水
	private String master_tran_log_id;// 查询关联交易流水号
	private String ticket_no;// 券号
	private String balance;// 卡内余额
	private String revoc_no;//
	private String dis_count;// 折扣率
	private String service_id;// 业务类型代码
	private String thirdTradeNo;
	
	
	private String merchantId;
	private Date tranTime;
	private String tranCode;
	private String cashType;
	private Integer tranAmount;
	private String cardType;
	private String cardNo;
	private String ticketInfoId;
	private String ticketCode;
	private String relateTranLogId;
	private Boolean canceledFlag;
	private String operatorId;
	private String posCode;

	private String merchantCardInfoId;
	
	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Date getTran_time() {
		return tran_time;
	}

	public void setTran_time(Date tran_time) {
		this.tran_time = tran_time;
	}

	public String getTran_code() {
		return tran_code;
	}

	public void setTran_code(String tran_code) {
		this.tran_code = tran_code;
	}

	public String getCash_type() {
		return cash_type;
	}

	public void setCash_type(String cash_type) {
		this.cash_type = cash_type;
	}

	public int getTran_amount() {
		return tran_amount;
	}

	public void setTran_amount(int tran_amount) {
		this.tran_amount = tran_amount;
	}

	public int getExtra_amount() {
		return extra_amount;
	}

	public void setExtra_amount(int extra_amount) {
		this.extra_amount = extra_amount;
	}

	public int getInput_amount() {
		return input_amount;
	}

	public void setInput_amount(int input_amount) {
		this.input_amount = input_amount;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getTicket_info_id() {
		return ticket_info_id;
	}

	public void setTicket_info_id(String ticket_info_id) {
		this.ticket_info_id = ticket_info_id;
	}

	public String getRelate_tran_log_id() {
		return relate_tran_log_id;
	}

	public void setRelate_tran_log_id(String relate_tran_log_id) {
		this.relate_tran_log_id = relate_tran_log_id;
	}

	public String getCanceled_flag() {
		return canceled_flag;
	}

	public void setCanceled_flag(String canceled_flag) {
		this.canceled_flag = canceled_flag;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public String getTran_mark() {
		return tran_mark;
	}

	public void setTran_mark(String tran_mark) {
		this.tran_mark = tran_mark;
	}

	public String getTran_desc() {
		return tran_desc;
	}

	public void setTran_desc(String tran_desc) {
		this.tran_desc = tran_desc;
	}

	public String getOffline_tran_log_id() {
		return offline_tran_log_id;
	}

	public void setOffline_tran_log_id(String offline_tran_log_id) {
		this.offline_tran_log_id = offline_tran_log_id;
	}

	public String getTicket_tran_log_id() {
		return ticket_tran_log_id;
	}

	public void setTicket_tran_log_id(String ticket_tran_log_id) {
		this.ticket_tran_log_id = ticket_tran_log_id;
	}

	public String getMaster_tran_log_id() {
		return master_tran_log_id;
	}

	public void setMaster_tran_log_id(String master_tran_log_id) {
		this.master_tran_log_id = master_tran_log_id;
	}

	public String getTicket_no() {
		return ticket_no;
	}

	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getRevoc_no() {
		return revoc_no;
	}

	public void setRevoc_no(String revoc_no) {
		this.revoc_no = revoc_no;
	}

	public String getDis_count() {
		return dis_count;
	}

	public void setDis_count(String dis_count) {
		this.dis_count = dis_count;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Date getTranTime() {
		return tranTime;
	}

	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}

	public String getTranCode() {
		return tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public Integer getTranAmount() {
		return tranAmount;
	}

	public void setTranAmount(Integer tranAmount) {
		this.tranAmount = tranAmount;
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

	public String getTicketInfoId() {
		return ticketInfoId;
	}

	public void setTicketInfoId(String ticketInfoId) {
		this.ticketInfoId = ticketInfoId;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getRelateTranLogId() {
		return relateTranLogId;
	}

	public void setRelateTranLogId(String relateTranLogId) {
		this.relateTranLogId = relateTranLogId;
	}

	public Boolean getCanceledFlag() {
		return canceledFlag;
	}

	public void setCanceledFlag(Boolean canceledFlag) {
		this.canceledFlag = canceledFlag;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public String getMerchantCardInfoId() {
		return merchantCardInfoId;
	}

	public void setMerchantCardInfoId(String merchantCardInfoId) {
		this.merchantCardInfoId = merchantCardInfoId;
	}

	

}
