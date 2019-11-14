package com.wizarpos.pay.cashier.presenter.payment.impl;

import android.content.Context;

import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

public class OtherPaymentImpl extends PaymentImpl {
	public OtherPaymentImpl(Context context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean pay(String amount, ResultListener listener) {
		return false;
	}

	@Override
	public boolean revoke(String amount, ResultListener listener) {
		return false;
	}

}
