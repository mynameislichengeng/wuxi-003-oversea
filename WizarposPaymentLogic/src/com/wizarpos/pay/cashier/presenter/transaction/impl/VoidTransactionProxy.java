package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;

import com.wizarpos.pay.cashier.presenter.transaction.inf.Transaction;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

/**
 * 撤销交易代理
 * 
 * @author wu
 */
public class VoidTransactionProxy {

	private Transaction transaction;

	public VoidTransactionProxy(Context context) {
		transaction = new TransactionImpl(context) {
			@Override
			public void trans(ResultListener listener) {

			}

			@Override
			public boolean revokeTrans(ResultListener listener) {
				return false;
			}
		};
	}

	public void getTransDetial(String transNum,boolean bankcardpay,ResultListener resultListener) {
		transaction.getTransDetial(transNum,bankcardpay,resultListener);
	}

}
