package com.wizarpos.pay.cashier.presenter.ticket.inf;

import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 会员卡管理 Created by wu on 2015/4/11 0011.
 */
public interface MemberTicketManager extends TicketManager {

	/**
	 * 查找会员号下的会员券
	 * 
	 * @param cardNo
	 *            会员卡号
	 * @param cardType
	 *            卡类型
	 * @param listener
	 *            回调
	 */
	void getMemberTickets(String cardNo, String cardType,
			final BasePresenter.ResultListener listener);

	/**
	 * 获取非会员券的打印信息
	 * 
	 * @return
	 */
	String getMemberPrintInfo();
}
