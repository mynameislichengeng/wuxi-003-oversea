package com.wizarpos.pay.cashier.bean;
/** 
 * @Author:Huangweicai
 * @Date:2015-7-31 下午4:58:56
 * @Reason:首页数据对象
 */
public class HomeNavBean {
	/** 昨日数据*/
	private BaseListDataBean yesterDayData = new BaseListDataBean();
	/** 今日数据*/
	private BaseListDataBean todayDayData = new BaseListDataBean();
	/** 本月（累计）数据*/
	private BaseListDataBean totalDayData = new BaseListDataBean();
	
	
	public BaseListDataBean getYesterDayData() {
		return yesterDayData;
	}
	public void setYesterDayData(BaseListDataBean yesterDayData) {
		this.yesterDayData = yesterDayData;
	}
	public BaseListDataBean getTodayDayData() {
		return todayDayData;
	}
	public void setTodayDayData(BaseListDataBean todayDayData) {
		this.todayDayData = todayDayData;
	}
	public BaseListDataBean getTotalDayData() {
		return totalDayData;
	}
	public void setTotalDayData(BaseListDataBean totalDayData) {
		this.totalDayData = totalDayData;
	}
	
	
}
