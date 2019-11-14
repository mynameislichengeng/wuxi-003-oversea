package com.wizarpos.pay.cashier.presenter.ticket.inf;

import com.wizarpos.pay.common.base.BasePresenter;


/**
 * 普通券(非会员券)管理 Created by wu on 2015/4/11 0011.
 */
public interface CommonTicketManager extends TicketManager {

	/**
	 * 获取非会员券的打印信息
	 */
	String getCommonTicketPrintInfo();
	

}
