package com.wizarpos.pay.cardlink.model;
/** 
 * @author 李敏 E-mail:min.li@wizarpos.com 
 * @version 创建时间：2016年2月24日 上午11:31:48 
 * 类说明 
 */

public class TransInfo {
	private int transType = -1;			// 交易类型
	private int transAmount = -1;		// 交易金额
	private int trace = -1;				// 交易流水号
	private int batchNumber = -1;		// 批次号
	
	private String pan = "";			// 卡号
	private String authCode = "";		// 授权码
	private String rrn = "";			// 主机参考号
	private String transDate = "";		// 交易日期
	private String transTime = "";		// 交易时间
	private String tid = "";			// 终端号
	private String mid = "";			// 商户号
	private String merchantName = "";	// 商户名称
	private String issuerCode = "";		// 发卡行代码
	private String acquirerCode = "";	// 收单行代码
	private String expiryDate = "";		// 卡有效期

	private String oldTransDate = "";	// 原交易日期
	private String oldTID = "";			// 原交易终端号
	private String oldAuthCode = "";	// 原交易授权码
	private String oldRRN = "";			// 原交易参考号
	private int    oldBatch = -1;		// 原交易批次号
	private int    oldTrace = -1;		// 原交易流水号
	
	private String respCode = "";		// 主机应答码
	private String respDesc = "";    	// 交易结果说明
	
	public int getTransType() {
		return transType;
	}
	public void setTransType(int transType) {
		this.transType = transType;
	}
	public int getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(int transAmount) {
		this.transAmount = transAmount;
	}
	public int getTrace() {
		return trace;
	}
	public void setTrace(int trace) {
		this.trace = trace;
	}
	public int getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getIssuerCode() {
		return issuerCode;
	}
	public void setIssuerCode(String issuerCode) {
		this.issuerCode = issuerCode;
	}
	public String getAcquirerCode() {
		return acquirerCode;
	}
	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getOldTransDate() {
		return oldTransDate;
	}
	public void setOldTransDate(String oldTransDate) {
		this.oldTransDate = oldTransDate;
	}
	public String getOldTID() {
		return oldTID;
	}
	public void setOldTID(String oldTID) {
		this.oldTID = oldTID;
	}
	public String getOldAuthCode() {
		return oldAuthCode;
	}
	public void setOldAuthCode(String oldAuthCode) {
		this.oldAuthCode = oldAuthCode;
	}
	public String getOldRRN() {
		return oldRRN;
	}
	public void setOldRRN(String oldRRN) {
		this.oldRRN = oldRRN;
	}
	public int getOldBatch() {
		return oldBatch;
	}
	public void setOldBatch(int oldBatch) {
		this.oldBatch = oldBatch;
	}
	public int getOldTrace() {
		return oldTrace;
	}
	public void setOldTrace(int oldTrace) {
		this.oldTrace = oldTrace;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
}
