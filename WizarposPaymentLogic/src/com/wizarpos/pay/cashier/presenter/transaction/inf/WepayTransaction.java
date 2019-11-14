package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 微信支付 Created by wu on 2015/4/13 0013.
 */
public interface WepayTransaction extends Transaction {

	public interface WepayTransactionListener {
		void onInit(Response response);
	}

	/**
	 * 设置撤销订单号
	 * 
	 * @param revokeTransOrderNum
	 *            订单号
	 */
	public void setRevokeTransNum(String revokeTransOrderNum);

	/**
	 * 设置撤销金额
	 * 
	 * @param revokeTransAmount
	 *            订单号
	 */
	void setRevokeTransAmount(String revokeTransAmount);

	void print();

	/**
	 * 检查订单(界面调用)
	 * 
	 * @param listener
	 *            回调
	 */
	void checkOrder(final BasePresenter.ResultListener listener);
}
