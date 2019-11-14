package com.wizarpos.base.net;

import com.alibaba.fastjson.annotation.JSONField;

public class MsgRequest {

	@JSONField(name = "service_code")
	private String serviceCode;
	private Object param;
	private String signature;
	private Object suffix;
	private String pem;

	public MsgRequest() {}

	public MsgRequest(String serviceCode, Object param) {
		this.serviceCode = serviceCode;
		this.param = param;
	}

	public MsgRequest(String serviceCode, Object param, String signature) {
		this.serviceCode = serviceCode;
		this.param = param;
		this.signature = signature;
	}

	public MsgRequest(String serviceCode, Object param, String signature, Object suffix) {
		this.serviceCode = serviceCode;
		this.param = param;
		this.signature = signature;
		this.suffix = suffix;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setMsgRequest(String serviceCode, Object param) {
		this.serviceCode = serviceCode;
		this.param = param;
	}

	public String getPem() {
		return pem;
	}

	public void setPem(String pem) {
		this.pem = pem;
	}

	public Object getSuffix() {
		return suffix;
	}

	public void setSuffix(Object suffix) {
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		return "MsgRequest{" +
				"suffix=" + suffix +
				", serviceCode='" + serviceCode + '\'' +
				", param=" + param +
				'}';
	}
}
