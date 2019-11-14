package com.wizarpos.pay.model;

/**
 * 便民通配置参数
 */
public class PeopSetConfig {
	private String merchantId;
	private String terminalId;
	private String ip;
	private String port;
	private String kmsPort;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getKmsPort() {
		return kmsPort;
	}

	public void setKmsPort(String kmsPort) {
		this.kmsPort = kmsPort;
	}

}