package com.wizarpos.pay.cashier.presenter.transaction.inf;

import android.content.Intent;

import com.wizarpos.pay.cashier.model.TransactionInfo2;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 交易定义 Created by wu on 2015/4/13 0013.
 */
public interface Transaction2 {

	TransactionInfo2 getTransactionInfo();
	
	/**
	 * 根据流水号查询交易明细
	 */
	void getTransDetial(String tranLogId, final BasePresenter.ResultListener listener);

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
