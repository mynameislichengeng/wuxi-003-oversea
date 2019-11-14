package com.wizarpos.pay.model;

import java.io.Serializable;

import com.wizarpos.pay.cashier.model.TicketInfo;

/**
 * 获取券信息返回  503接口
 * @author wu
 *
 */
public class GetCommonTicketInfoResp implements Serializable {

	private  TicketInfo ticketInfo; //查询的券信息
	
	private TicketInfo wemengTicket;//微盟券信息

	public TicketInfo getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(TicketInfo ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public TicketInfo getWemengTicket() {
		return wemengTicket;
	}

	public void setWemengTicket(TicketInfo wemengTicket) {
		this.wemengTicket = wemengTicket;
	}
	
	
	
	
}
