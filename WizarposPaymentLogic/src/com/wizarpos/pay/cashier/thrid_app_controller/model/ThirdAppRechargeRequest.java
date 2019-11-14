package com.wizarpos.pay.cashier.thrid_app_controller.model;

import java.io.Serializable;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdRequest;

/**
 * 第三方应用transact交易请求参数
 * 
 * @author wu
 * 
 */
public class ThirdAppRechargeRequest extends ThirdRequest implements
		Serializable {
	private String amount;
	private String button_control;
	private String memberCard;
	private String noPrint;
	private String noTicket;
	private ThirdAppTransactionBtnAvailable btnAvailable;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getButton_control() {
		return button_control;
	}

	public void setButton_control(String button_control) {
		this.button_control = button_control;
	}

	public String getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(String memberCard) {
		this.memberCard = memberCard;
	}

	public String getNoPrint() {
		return noPrint;
	}

	public void setNoPrint(String noPrint) {
		this.noPrint = noPrint;
	}

	public String getNoTicket() {
		return noTicket;
	}

	public void setNoTicket(String noTicket) {
		this.noTicket = noTicket;
	}

	public void setBtnAvailable(ThirdAppTransactionBtnAvailable btnAvailable) {
		this.btnAvailable = btnAvailable;
	}

	public ThirdAppTransactionBtnAvailable getBtnAvailable() {
		return btnAvailable;
	}

}
