package com.wizarpos.pay.cashier.presenter.ticket;

import com.wizarpos.pay.cashier.presenter.ticket.impl.WepayTicketOperaterImpl;
import com.wizarpos.pay.cashier.presenter.ticket.inf.WepayTicketOperater;

/**
 * 管理者工厂 Created by wu on 2015/4/13 0013.
 */
public class TicketOperaterFactory {

	private TicketOperaterFactory() {
	}

	;

	/**
	 * 生成微信券管理者
	 */
	public static WepayTicketOperater createWepayTicketOperater() {
		return new WepayTicketOperaterImpl();
	}

}
