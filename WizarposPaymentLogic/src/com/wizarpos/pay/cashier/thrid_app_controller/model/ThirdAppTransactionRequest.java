package com.wizarpos.pay.cashier.thrid_app_controller.model;

import java.io.Serializable;

import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdRequest;

/**
 * 第三方应用transact交易请求参数
 * 
 * @author wu
 * {@link #choosePayMode}类型(根据类型支付) 根据{@link TransactionTemsController}来判断 Hwc
 */
public class ThirdAppTransactionRequest extends ThirdRequest implements
		Serializable {
	private String amount;
	private String saleInputAmount;
	private String button_control;
	private String memberCard;
	private String noPrint;
	private String noTicket;
	private String mid;
	private ThirdAppTransactionBtnAvailable btnAvailable;
	/**根据类型直接跳转支付 int类型参考{@link TransactionTemsController} Hwc 2015年11月23日15:50:08*/
	private int choosePayMode;

	public void setSaleInputAmount(String saleInputAmount) {
		this.saleInputAmount = saleInputAmount;
	}
	
	public String getSaleInputAmount() {
		return saleInputAmount;
	}
	
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

	public int getChoosePayMode() {
		return choosePayMode;
	}

	public void setChoosePayMode(int choosePayMode) {
		this.choosePayMode = choosePayMode;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

}
