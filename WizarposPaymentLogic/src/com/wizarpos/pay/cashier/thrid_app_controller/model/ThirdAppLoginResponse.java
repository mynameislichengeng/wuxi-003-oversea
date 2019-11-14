package com.wizarpos.pay.cashier.thrid_app_controller.model;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdResponse;

public class ThirdAppLoginResponse extends ThirdResponse {

	private ThirdAppLoginEXJsonResponse exJson;

	public ThirdAppLoginEXJsonResponse getExJson() {
		return exJson;
	}

	public void setExJson(ThirdAppLoginEXJsonResponse exJson) {
		this.exJson = exJson;
	}

}
