package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.payment.impl.CardPayment;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * Created by wu on 2015/4/13 0013.
 */
public interface CardTransaction extends Transaction {

	/**
	 * 获取普通券的信息
	 * 
	 * @param ticketNum
	 *            券唯一标识
	 * @param listener
	 */
	void getCommonTicketInfo(String ticketNum, final BasePresenter.ResultListener listener);
	void getCommonTicketInfo(String ticketNum, String shouldPayAmount,final BasePresenter.ResultListener listener);
	/**
	 * 添加普通券
	 */
	Response addCommonTicket(TicketInfo ticket, boolean isFromScan);

	void setCardPaymetProgressListener(CardPayment.CardPaymetProgressListener cardPaymetProgressListener);

	void continueTrans();

	void endTrans();
}
