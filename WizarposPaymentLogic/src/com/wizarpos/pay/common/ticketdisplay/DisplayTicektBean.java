package com.wizarpos.pay.common.ticketdisplay;

import java.io.Serializable;

/**
 * 显示券列表
 * @author wu
 *
 */
public class DisplayTicektBean implements Serializable {
	
	private String title; //标题 (居中)
	private String subTitle; //子标题 (靠左)
	private String bitmapPath; //图片路径
	private String endString; //图片后String (靠左)
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getBitmapPath() {
		return bitmapPath;
	}
	public void setBitmapPath(String bitmapPath) {
		this.bitmapPath = bitmapPath;
	}
	public String getEndString() {
		return endString;
	}
	public void setEndString(String endString) {
		this.endString = endString;
	}
	
	

}
