package com.wizarpos.pay.cashier.presenter.payment.impl;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.MemberCardInfoBean;
import com.wizarpos.pay.common.utils.Calculater;

public class MemberPayment {

	private MemberCardInfoBean cardInfo;

	public MemberCardInfoBean getCardInfo() {
		return cardInfo;
	}
	
	public void setMemberCardInfo(MemberCardInfoBean cardInfo) throws Exception {
		if (this.cardInfo != null) { throw new Exception("已有会员卡信息"); }
		this.cardInfo = cardInfo;
	}
	
	public String getBalance() {
		if (this.cardInfo == null) {return "";}
		return cardInfo.getBalance();
	}

	public Response pay(String amount) {
		Response response = new Response();
		if (cardInfo == null) {
			response.code = 1;
			response.msg = "未设置会员卡信息";
			return response;
		} else if (Calculater.compare(amount, cardInfo.getBalance()) == 1) {
			response.code = 1;
			response.msg = "会员卡余额不足";
			return response;
		} else {
			cardInfo.setBalance(Calculater.subtract(cardInfo.getBalance(), amount));
			response.code = 0;
			response.msg = "金额扣减成功";
			return response;
		}

	}

	public boolean revoke(String amount) {
		return false;
	}
}
