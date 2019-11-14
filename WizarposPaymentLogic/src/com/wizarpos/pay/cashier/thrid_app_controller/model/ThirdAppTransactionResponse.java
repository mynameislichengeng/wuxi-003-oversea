package com.wizarpos.pay.cashier.thrid_app_controller.model;

import java.io.Serializable;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdResponse;

/**
 * 第三方应用transact交易返回参数
 * 
 * @author wu
 * 
 */
public class ThirdAppTransactionResponse extends ThirdResponse implements
		Serializable {

	private ThirdAppTransactionEXJsonResponse exJson;

	public ThirdAppTransactionEXJsonResponse getExJson() {
		return exJson;
	}

	public void setExJson(ThirdAppTransactionEXJsonResponse exJson) {
		this.exJson = exJson;
	}
}
