package com.wizarpos.pay.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.wizarpos.log.util.LogEx;

/**
 * 
 * @ClassName: MixWepayTicketBean 
 * @author Huangweicai
 * @date 2015-9-6 下午4:19:37 
 * @Description: 网络请求 微信核销实体
 */
public class MixWepayTicketBean implements Serializable{
	private String masterTranLogId;
	private String mixFlag;
	/** 应收 例 组合支付50元 现金已经支付了20元 再进行一笔的时候应该传50元(5000)*/
	private long inputAmount;//应收
	private String isPayComingForm;
	private long wxTicketAmount;//券金额
	/** 已收 例 组合支付50元 现金已经支付了20元 再进行一笔10元交易的时候应该传10元(1000)*/
	private long masterPayAmount;//已收
	/** 当券的金额大于应付金额的时候,应赋予应付金额 如 券金额5元,应付为1元,则该值为100 @hwc*/
	private long wxTrueAmount;
	private String wxCode;
	
	public long getMasterPayAmount() {
		return masterPayAmount;
	}
	public void setMasterPayAmount(long masterPayAmount) {
		this.masterPayAmount = masterPayAmount;
	}
	public String getWxCode() {
		return wxCode;
	}
	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}
	public String getMasterTranLogId() {
		return masterTranLogId;
	}
	public void setMasterTranLogId(String masterTranLogId) {
		this.masterTranLogId = masterTranLogId;
	}
	public String getMixFlag() {
		return mixFlag;
	}
	public void setMixFlag(String mixFlag) {
		this.mixFlag = mixFlag;
	}
	public String getIsPayComingForm() {
		return isPayComingForm;
	}
	public void setIsPayComingForm(String isPayComingForm) {
		this.isPayComingForm = isPayComingForm;
	}
	public long getInputAmount() {
		return inputAmount;
	}
	public void setInputAmount(long inputAmount) {
		this.inputAmount = inputAmount;
	}
	public long getWxTicketAmount() {
		return wxTicketAmount;
	}
	public void setWxTicketAmount(long wxTicketAmount) {
		this.wxTicketAmount = wxTicketAmount;
	}
	public long getWxTrueAmount() {
		return wxTrueAmount;
	}
	public void setWxTrueAmount(long wxTrueAmount) {
		this.wxTrueAmount = wxTrueAmount;
	}
	
}
