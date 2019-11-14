package com.wizarpos.pay.cashier.presenter.payment.impl;

import com.wizarpos.pay.cashier.presenter.payment.inf.UDIDPayment;

/**
 * 特征码支付
 * 
 * @author wu
 */
public abstract class UDIDPaymentImpl extends PaymentImpl implements
		UDIDPayment {

	protected String UDID;// 特征码

	public void setUDID(String uDID) {
		UDID = uDID;
	}

	public String getUDID() {
		return UDID;
	}

}
