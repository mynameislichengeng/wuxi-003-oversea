package com.wizarpos.pay.cashier.presenter.payment.inf;

/**
 * 特征码支付 Created by wu on 2015/4/8 0008.
 */
public interface UDIDPayment extends Payment {

	/**
	 * 设置交易过程中的特征码
	 * 
	 * @param uDID
	 */
	public void setUDID(String uDID);

	/**
	 * 获取特征码
	 * 
	 * @return
	 */
	public String getUDID();
}
