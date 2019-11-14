package com.wizarpos.pay.cashier.thrid_app_controller.model.base;

import java.io.Serializable;

public class ThirdRequest implements Serializable {

	private String transType;

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

}
