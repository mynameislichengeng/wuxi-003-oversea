package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

public interface TenpayNativeTransaction extends Transaction {

	public void createCode(ResultListener listener);

	/**
	 * 单笔订单查询
	 * 
	 * @param listener
	 */
	public void query(String tranLogId, ResultListener listener);

}