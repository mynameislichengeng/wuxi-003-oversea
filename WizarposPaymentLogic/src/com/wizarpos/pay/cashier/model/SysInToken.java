package com.wizarpos.pay.cashier.model;

public class SysInToken implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String tokenId;
	private String info;
	private String subInfo;
	private String mid;
	private Long expiryTime;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSubInfo() {
		return subInfo;
	}

	public void setSubInfo(String subInfo) {
		this.subInfo = subInfo;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public Long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}
}
