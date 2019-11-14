package com.wizarpos.pay.cashier.pay_tems.alipay.entity;

import java.io.Serializable;

public class AlipayMicroReq implements Serializable {
	private String service = "alipay.acquire.createandpay";
	private String partner;
	private String inputCharset = "UTF-8";
	private String signType = "MD5";
	private String sign;
	private String outTradeNo;
	private String subject;
	private String productCode = "BARCODE_PAY_OFFLINE";
	private String totalFee;
	private String notifyUrl;
	private String dynamic_id_type = "qrcode";
	private String dynamic_id;
	private String authCode;
	private String extend_params;// 公用业务扩展信息
	private String AGENT_ID;// 代理人号//extend_params
	private String STORE_ID;// 门店类型 //extend_params
	private String STORE_NAME;
	private String TERMINAL_ID;// 终端设备号extend_params
	private String channel_parameters;// 渠道参数
	private String payeeTermId;// 收款方终端号 channel_parameters
	public String getSTORE_NAME() {
		return STORE_NAME;
	}

	public void setSTORE_NAME(String sTORE_NAME) {
		STORE_NAME = sTORE_NAME;
	}
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getDynamic_id_type() {
		return dynamic_id_type;
	}

	public void setDynamic_id_type(String dynamic_id_type) {
		this.dynamic_id_type = dynamic_id_type;
	}

	public String getDynamic_id() {
		return dynamic_id;
	}

	public void setDynamic_id(String dynamic_id) {
		this.dynamic_id = dynamic_id;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getExtend_params() {
		return extend_params;
	}

	public void setExtend_params(String extend_params) {
		this.extend_params = extend_params;
	}

	public String getAGENT_ID() {
		return AGENT_ID;
	}

	public void setAGENT_ID(String aGENT_ID) {
		AGENT_ID = aGENT_ID;
	}

	public String getSTORE_ID() {
		return STORE_ID;
	}

	public void setSTORE_ID(String sTORE_ID) {
		STORE_ID = sTORE_ID;
	}

	public String getTERMINAL_ID() {
		return TERMINAL_ID;
	}

	public void setTERMINAL_ID(String tERMINAL_ID) {
		TERMINAL_ID = tERMINAL_ID;
	}

	public String getChannel_parameters() {
		return channel_parameters;
	}

	public void setChannel_parameters(String channel_parameters) {
		this.channel_parameters = channel_parameters;
	}

	public String getPayeeTermId() {
		return payeeTermId;
	}

	public void setPayeeTermId(String payeeTermId) {
		this.payeeTermId = payeeTermId;
	}

}
