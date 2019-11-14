package com.wizarpos.pay.view;

import java.io.Serializable;

public class TransTypeItem implements Serializable{
	private int realValue;
	private String showValue;
	private int icon;

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public TransTypeItem() {}

	public TransTypeItem(int realValue, String showValue, int icon) {
		this.realValue = realValue;
		this.showValue = showValue;
		this.icon = icon;
	}

	public int getRealValue() {
		return realValue;
	}

	public void setRealValue(int realValue) {
		this.realValue = realValue;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	@Override
	public String toString() {
		return showValue;
	}
}
