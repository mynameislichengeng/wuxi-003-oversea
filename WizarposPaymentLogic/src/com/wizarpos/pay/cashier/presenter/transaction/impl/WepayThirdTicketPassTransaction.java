package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;

/**
 * 微信卡券核销
 * 
 * @author wu
 * 
 */
public class WepayThirdTicketPassTransaction extends TransactionImpl {

	public WepayThirdTicketPassTransaction(Context context) {
		super(context);
	}

	@Override
	public void trans(ResultListener listener) {

	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

}
