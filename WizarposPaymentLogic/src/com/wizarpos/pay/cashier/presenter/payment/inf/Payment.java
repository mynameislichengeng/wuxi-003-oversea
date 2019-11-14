package com.wizarpos.pay.cashier.presenter.payment.inf;

import com.wizarpos.pay.common.base.BasePresenter;

/**
 * Created by wu on 2015/4/8 0008.
 */
public interface Payment {

	/**
	 * 支付
	 * 
	 * @param amount
	 *            交易金额
	 * @param listener
	 *            回调
	 * @return
	 */
	public boolean pay(String amount, BasePresenter.ResultListener listener);

	/**
	 * 撤销
	 * 
	 * @param amount
	 *            交易金额
	 * @param listener
	 *            回调
	 * @return
	 */
	public boolean revoke(String amount, BasePresenter.ResultListener listener);
}
