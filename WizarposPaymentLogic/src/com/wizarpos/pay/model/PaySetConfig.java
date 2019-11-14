package com.wizarpos.pay.model;

/**
 * 支付易配置参数
 */
public class PaySetConfig {
	// 支付宝参数 0为慧银 1为其他支付参数
	private int alipay = 0;
	// 微信参数 0为慧银 1为其他微信参数
	private int weixin = 0;
	// 银行卡支付撤销密码需要 0为不需要 1为需要
	private int bankPayPasNeed = 0;
	// 其他支付撤销密码需要 0为不需要 1为需要
	private int otherPayNeed = 0;

	public int getAlipay() {
		return alipay;
	}

	public void setAlipay(int alipay) {
		this.alipay = alipay;
	}

	public int getWeixin() {
		return weixin;
	}

	public void setWeixin(int weixin) {
		this.weixin = weixin;
	}

	public int getBankPayPasNeed() {
		return bankPayPasNeed;
	}

	public void setBankPayPasNeed(int bankPayPasNeed) {
		this.bankPayPasNeed = bankPayPasNeed;
	}

	public int getOtherPayNeed() {
		return otherPayNeed;
	}

	public void setOtherPayNeed(int otherPayNeed) {
		this.otherPayNeed = otherPayNeed;
	}

}