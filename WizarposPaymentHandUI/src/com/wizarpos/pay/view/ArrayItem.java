package com.wizarpos.pay.view;

public class ArrayItem {
	private int realValue;
	private String showValue;
	
	public ArrayItem() {}

	public ArrayItem(int realValue, String showValue) {
		this.realValue = realValue;
		this.showValue = showValue;
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
