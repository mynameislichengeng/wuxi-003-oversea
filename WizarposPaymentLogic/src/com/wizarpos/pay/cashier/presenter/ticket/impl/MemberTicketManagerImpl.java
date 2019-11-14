package com.wizarpos.pay.cashier.presenter.ticket.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.inf.MemberTicketManager;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;

public class MemberTicketManagerImpl extends TicketManagerImpl implements MemberTicketManager {

	public MemberTicketManagerImpl(Context context) {
		super(context);
	}

	public void getMemberTickets(String cardNo, String cardType, final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardType", cardType);
		params.put("cardNo", cardNo);
		NetRequest.getInstance().addRequest(Constants.SC_303_TICKET_LIST, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				if (TextUtils.isEmpty(response.result.toString())) {
					listener.onFaild(new Response(1, "没有会员券"));
					return;
				}
				List<TicketInfo> tickets = JSONArray.parseArray(response.result.toString(), TicketInfo.class);
				if (tickets == null || tickets.isEmpty()) {
					listener.onFaild(new Response(1, "没有会员券"));
				} else {
					listener.onSuccess(new Response(0, "success", tickets));
				}
			}

			@Override
			public void onFaild(Response response) {
				response.code = 1;
				listener.onSuccess(response);
			}
		});
	}

	public String getMemberPrintInfo() {
		List<TicketInfo> addedTickets = getAddedTickets();
		if (addedTickets == null || addedTickets.isEmpty()) { return ""; }
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		// 统计数量
		// HashMap<TicketDef, Integer> ticketGroup = new HashMap<TicketDef,
		// Integer>();
		// for (TicketInfo ticket : addedTickets) {
		// TicketDef ticketDef = ticket.getTicketDef();
		// if (ticketGroup.containsKey(ticketDef)) {
		// ticketGroup.put(ticketDef, ticketGroup.get(ticketDef) + 1);
		// } else {
		// ticketGroup.put(ticketDef, 1);
		// }
		// }
		// Iterator iter = ticketGroup.entrySet().iterator();
		// while (iter.hasNext()) {
		// // if ("1".equals(ticket.getTicketDef().getWxFlag()) ||
		// // "2".equals(ticket.getTicketDef().getWxFlag())) {
		// // continue;
		// // }
		//
		// Map.Entry<TicketDef, Integer> entry = (Map.Entry<TicketDef, Integer>)
		// iter.next();
		// TicketDef ticketDef = entry.getKey();
		// int count= entry.getValue();
		// if (TextUtils.isEmpty(printString)) {
		// printString = builder.center(builder.bold("会员券使用"))
		// + builder.br() + builder.branch();
		// }
		// printString += "名称:" + ticketDef.getTicketName()
		// + builder.br();
		// printString += "金额:"
		// + Tools.formatFen(ticketDef.getBalance())
		// + builder.br();
		// printString += "数量:"+ count + builder.br();
		// printString += builder.branch();
		// }

		for (int i = 0; i < addedTickets.size(); i++) {
			if (TextUtils.isEmpty(printString)) {
				printString = builder.center(builder.bold("会员券使用")) + builder.br();
			}
			if (i == 0) {
				printString += "时间:" + DateUtil.format(System.currentTimeMillis(), DateUtil.P2) + builder.br() + builder.branch();//券使用 打印时间 @yaosong [20151026]
			}
			printString += "名称:" + addedTickets.get(i).getTicketDef().getTicketName() + builder.br();
			printString += "金额:" + Tools.formatFen(addedTickets.get(i).getTicketDef().getBalance()) + builder.br();
			printString += builder.branch();
		}
		return printString;
	}

}
