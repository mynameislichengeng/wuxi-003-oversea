package com.wizarpos.pay.cashier.presenter.ticket.inf;

import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 券操作 Created by wu on 2015/4/11 0011.
 */
public interface TicketOperater {

	/**
	 * 核销券
	 * 
	 * @param ticket
	 *            券实体
	 * @param listener
	 */
	void pass(TicketInfo ticket, BasePresenter.ResultListener listener);

	/**
	 * 获取券的详细信息
	 * 
	 * @param code
	 *            券唯一标识
	 * @param listener
	 */
	void getTicketDetial(String code, BasePresenter.ResultListener listener);
}
