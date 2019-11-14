package com.wizarpos.pay.cashier.bean;

import java.io.Serializable;

/** 
 * @Author:Huangweicai
 * @Date:2015-7-29 上午10:18:12
 * @Reason:普通item对象
 */
public class BaseListDataBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int drableSouce;
	private String title;
	private String value;
	private Double intValue = 0.00;
	
	public int getDrableSouce() {
		return drableSouce;
	}
	public void setDrableSouce(int drableSouce) {
		this.drableSouce = drableSouce;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getIntValue() {
		return intValue;
	}
	public void setIntValue(Double intValue) {
		this.intValue = intValue;
	}
	
}
