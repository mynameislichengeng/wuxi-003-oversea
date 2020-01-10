package com.wizarpos.pay.cashier.presenter.ticket.impl;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;

/**
 * 普通券(非会员券)管理
 * 
 * @author wu
 */
public class CommonTicketManagerImpl extends TicketManagerImpl implements CommonTicketManager {

	public CommonTicketManagerImpl(Context context) {
		super(context);
	}

	public String getCommonTicketPrintInfo() {
		List<TicketInfo> addedTickets = getAddedTickets();
		int commonNum = 0;
		for (TicketInfo ticket : addedTickets) {
			if (!(("1".equals(ticket.getTicketDef().getWxFlag()) || 
					"3".equals(ticket.getTicketDef().getWxFlag())))) {// 判读是不是微信券或朋友券
				commonNum ++;
			}
		}
		if (addedTickets == null || addedTickets.isEmpty() || commonNum <= 0) { return ""; }
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = builder.center(builder.bold("非会员券使用")) + builder.br() + builder.branch();
		printString += "时间:" + DateUtil.format(System.currentTimeMillis(), DateUtil.P2) + builder.br() + builder.branch();//券使用 打印时间 @yaosong [20151026]
		for (TicketInfo ticket : addedTickets) {
			printString += "名称:" + ticket.getTicketDef().getTicketName() + builder.br();
			printString += "金额:" + Tools.formatFen(ticket.getTicketDef().getBalance()) + builder.br();
			if(PaymentApplication.getInstance().isWemengMerchant()) {//微盟券使用打印的券号bugfix
//				printStringPayFor += builder.center("券号:" + builder.normal(ticket.getDisplayCode()));
			}else {
				printString += builder.center("券号:" + builder.normal(ticket.getTicketNo()));
			}
			printString += builder.branch();
		}
		return printString;
	}

}
