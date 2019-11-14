package com.wizarpos.pay.cashier.model;

import java.io.Serializable;

import android.R.integer;

/** 
 * @Author:Huangweicai
 * @Date:2015-8-5 下午2:38:13
 * @Reason:卡包子元素详情
 */
public class TicketPacketInfo implements Serializable{
	private String packetId;
	private String ticketId;
	private Integer ticketNums;
	private String createTime;
	private Integer balance;
	private String ticketDefId;
	private String ticketName;
	private Integer validPeriod;
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public String getTicketDefId() {
		return ticketDefId;
	}
	public void setTicketDefId(String ticketDefId) {
		this.ticketDefId = ticketDefId;
	}
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public Integer getValidPeriod() {
		return validPeriod;
	}
	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}
	public String getPacketId() {
		return packetId;
	}
	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public Integer getTicketNums() {
		return ticketNums;
	}
	public void setTicketNums(Integer ticketNums) {
		this.ticketNums = ticketNums;
	}
	
}
