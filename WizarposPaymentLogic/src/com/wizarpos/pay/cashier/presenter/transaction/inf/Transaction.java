package com.wizarpos.pay.cashier.presenter.transaction.inf;

import android.content.Intent;

import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 交易定义 Created by wu on 2015/4/13 0013.
 */
public interface Transaction {

	/**
	 * 设置实付金额
	 * 
	 * @param realAmount
	 */
	void setRealAmount(String realAmount);

	/**
	 * 获取初始金额
	 * 
	 * @return
	 */
	String getInitAmount();

	/**
	 * 获取应收金额
	 * 
	 * @return
	 */
	String getShouldAmount();

	/**
	 * 获取实收金额
	 * 
	 * @return
	 */
	String getRealAmount();

	/**
	 * 获取扣减金额
	 * 
	 * @return
	 */
	String getReduceAmount();

	/**
	 * 获取找零金额
	 * 
	 * @return
	 */
	String getChangeAmount();

	/**
	 * 获取折扣扣减金额
	 * 
	 * @return
	 */
	String getDiscountAmount();

	/**
	 * 获取订单号
	 */
	String getTranId();

	/**
	 * 获取流水号
	 * 
	 * @return
	 */
	String getTranLogId();

	/**
	 * 根据流水号查询交易明细
	 */
	void getTransDetial(String tranLogId,boolean bankcardpay, final BasePresenter.ResultListener listener);

	/**
	 * 初始化intent
	 * 
	 * @param intent
	 */
	void handleIntent(Intent intent);

	/**
	 * 交易
	 * 
	 * @return
	 */
	void trans(BasePresenter.ResultListener listener);

	/**
	 * 撤销交易
	 * 
	 * @return
	 */
	boolean revokeTrans(BasePresenter.ResultListener listener);

	/**
	 * 获取交易类型
	 * @return
	 */
	int getTransactionType();
	
	boolean isMixTransaction();
	
	void onCreate();

	void onResume();

	void onPause();

	void onStop();

	void onDestory();
}
