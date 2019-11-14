package com.wizarpos.pay.cashier.presenter.payment.impl;

import com.wizarpos.pay.cashier.presenter.payment.inf.Payment;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 支付类
 * 
 * @author wu
 */
public abstract class PaymentImpl implements Payment {
	protected String amount;

	protected String getAmount() {
		return amount;
	}

	protected void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public abstract boolean pay(String amount,
			BasePresenter.ResultListener listener);

	@Override
	public abstract boolean revoke(String amount,
			BasePresenter.ResultListener listener);
}
