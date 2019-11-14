package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 线上支付被扫支付 Created by wu on 2015/4/15 0015.
 */
public interface OnlinePaymentMicroTransaction extends OnlinePaymentTransaction {

	/**
	 * 支付
	 * 
	 * @param resultListener
	 */
	void pay(String authCode, BasePresenter.ResultListener resultListener);

}
