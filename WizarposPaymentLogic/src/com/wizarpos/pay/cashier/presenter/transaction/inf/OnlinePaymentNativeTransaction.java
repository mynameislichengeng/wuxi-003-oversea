package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

/**
 * 线上支付扫码支付 Created by wu on 2015/6/29.
 */
public interface OnlinePaymentNativeTransaction extends
		OnlinePaymentTransaction {

	/**
	 * 获取二维码
	 * 
	 * @param listener
	 */
	void getBarCode(BasePresenter.ResultListener listener);

	/**
	 * 监听结果
	 * 
	 * @param listener
	 */
	void listenResult(ResultListener listener);

}
