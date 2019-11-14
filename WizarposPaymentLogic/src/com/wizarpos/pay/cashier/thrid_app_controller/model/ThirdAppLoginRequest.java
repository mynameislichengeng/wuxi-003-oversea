package com.wizarpos.pay.cashier.thrid_app_controller.model;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdRequest;

public class ThirdAppLoginRequest extends ThirdRequest {

	private String appName;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
