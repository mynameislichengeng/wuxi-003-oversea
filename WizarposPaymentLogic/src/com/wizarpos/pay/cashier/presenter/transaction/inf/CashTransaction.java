package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 现金交易 Created by wu on 2015/4/13 0013.
 */
public interface CashTransaction extends Transaction {

	/**
	 * 添加券
	 * 
	 * @param ticket
	 *            券实体
	 * @param isFromScan
	 *            是否是扫码添加
	 */
	Response addTicket(TicketInfo ticket, boolean isFromScan);

	/**
	 * 获取券的信息
	 * 
	 * @param ticketNum
	 *            券唯一标识
	 * @param amount 交易金额           
	 * @param listener
	 *            回调
	 */
	void getTicketInfo(String ticketNum, final BasePresenter.ResultListener listener);
	
	void getTicketInfo(String ticketNum, String shouldPayAmount,final BasePresenter.ResultListener listener);
	/**
	 * 获取减免金额
	 * @param listener
	 */
	void derate(final BasePresenter.ResultListener listener);
	
	/**
	 * 设置四舍五入金额
	 * @param round
	 */
	void setRound(double round);
}
