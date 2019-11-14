package com.wizarpos.pay.cashier.pay_tems.bat.inf;

import com.wizarpos.pay.cashier.presenter.transaction.inf.OnlinePaymentNativeTransaction;
import com.wizarpos.pay.common.base.BasePresenter;

public interface NativeTransaction extends OnlinePaymentNativeTransaction {
	void batPay(String payBatType, BasePresenter.ResultListener resultListener);
	
	/**
	 * 获得券信息(目前用于微盟 2015年12月15日11:51:57 hwc)
	 * 
	 * @param resultListener
	 */
	void getTicketInfo(String ticketNum, BasePresenter.ResultListener resultListener);
}
