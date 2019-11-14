package com.wizarpos.pay.model;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author hong
 * 营销活动相应
 *
 */
public class MarketPayRes {
	private String reducAmount;//可用券的张数
	private JSONArray ticketIds;//可用券集合
	public String getReducAmount() {
		return reducAmount;
	}
	public void setReducAmount(String reducAmount) {
		this.reducAmount = reducAmount;
	}
	public JSONArray getTicketIds() {
		return ticketIds;
	}
	public void setTicketIds(JSONArray ticketIds) {
		this.ticketIds = ticketIds;
	}
}
