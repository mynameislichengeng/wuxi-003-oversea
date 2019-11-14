package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

/**
 * 微信被扫支付
 * 
 */
public interface WepayAgentMicroTransaction extends WepayTransaction {
	/**
	 * 设置获取的被扫二维码
	 * 
	 * @param authCode
	 *            二维码
	 */
	void setAuthCode(String authCode, ResultListener listener);
}
