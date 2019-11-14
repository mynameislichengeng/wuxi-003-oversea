package com.wizarpos.pay.cashier.model;

import java.util.List;

/**
 * @Author:Huangweicai
 * @Date:2015-8-5 下午2:37:49
 * @Reason:券包对象
 */
public class TicketPacket extends TicketDef {
	private String packetId;
	private String packetName;
	protected List<TicketPacketInfo> packInfo;

	public List<TicketPacketInfo> getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(List<TicketPacketInfo> packInfo) {
		this.packInfo = packInfo;
	}

	public TicketPacket() {
		isPacket = true;
	}

	public boolean isPacket() {
		return isPacket;
	}

	public void setPacket(boolean isPacket) {
		this.isPacket = isPacket;
	}

	public String getPacketId() {
		return packetId;
	}

	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}

	public String getPacketName() {
		return packetName;
	}

	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}

}
