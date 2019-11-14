package com.wizarpos.pay.cashier.presenter.ticket;

import android.content.Context;

import com.wizarpos.pay.cashier.presenter.ticket.impl.CommonTicketManagerImpl;
import com.wizarpos.pay.cashier.presenter.ticket.impl.MemberTicketManagerImpl;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.cashier.presenter.ticket.inf.MemberTicketManager;

/**
 * 统一维护券管理类 Created by wu on 2015/4/11 0011.
 */
public class TicketManagerFactory {
	private TicketManagerFactory() {
	}

	/**
	 * 生成普通券(非会员券)管理者
	 * 
	 * @param context
	 * @return
	 */
	public static CommonTicketManager createCommonTicketManager(Context context) {
		return new CommonTicketManagerImpl(context);
	}

	/**
	 * 生成会员券管理者
	 * 
	 * @param context
	 * @return
	 */
	public static MemberTicketManager createMemberTicketManager(Context context) {
		return new MemberTicketManagerImpl(context);
	}

}
