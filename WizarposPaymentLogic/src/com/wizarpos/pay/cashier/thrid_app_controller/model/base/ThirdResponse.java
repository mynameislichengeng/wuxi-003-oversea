package com.wizarpos.pay.cashier.thrid_app_controller.model.base;

import java.io.Serializable;

import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionEXJsonResponse;

public class ThirdResponse implements Serializable {
	private int code;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
