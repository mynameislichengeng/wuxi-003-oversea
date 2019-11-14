package com.wizarpos.pay.cashier.presenter.transaction.inf;

import android.content.Intent;

import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.model.TransactionInfo;

/**
 * 线上支付 Created by wu on 2015/6/26 0026.
 */
public interface OnlinePaymentTransaction {

	void handleIntent(Intent intent);

	/**
	 * 获取支付状态描述实体
	 * 
	 * @return
	 */
	TransactionInfo getTransactionInfo();

	/**
	 * 检查订单
	 * 
	 * @param tranLogId
	 *            交易订单号
	 * @param listener
	 */
	void checkOrder(String tranId, BasePresenter.ResultListener listener);

	/**
	 * 撤销交易
	 * 
	 * @param tranLogId
	 *            流水号
	 * @param listener
	 */
	void revokeTrans(String code, String tranLogId,
			BasePresenter.ResultListener listener);

	/**
	 * 打印交易
	 */
	void printTransInfo();

	/**
	 * 打印撤销二维码
	 */
	void printRevokeCode();

	void onDestory();

	Intent bundleResult();
	
	/**
	 * 是否是组合支付
	 * @return
	 */
	boolean isMixTransaction();
}
