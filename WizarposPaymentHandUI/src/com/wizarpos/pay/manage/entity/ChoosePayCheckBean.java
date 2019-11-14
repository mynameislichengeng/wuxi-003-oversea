package com.wizarpos.pay.manage.entity;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.manage.activity.PaymentItemSettingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-4 下午5:54:26
 * @Description: {@link CheckDataBean} {@link PaymentItemSettingActivity}
 */
public class ChoosePayCheckBean {
	
	private List<CheckDataBean> checkList;

	public List<CheckDataBean> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<CheckDataBean> checkList) {
		this.checkList = checkList;
	}
	
	
	
	public CheckDataBean getUnionPay()
	{
		if(checkList == null)
			return null;
		for(CheckDataBean bean:checkList)
		{
			if(bean.getConfigDef().equals(AppConfigDef.isSupportUnionPay))
			{
				return bean;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-17 上午11:38:59 
	 * @return 
	 * @Description:
	 */
	public List<CheckDataBean> getThirdPayList()
	{
		List<CheckDataBean> dataList = new ArrayList<CheckDataBean>();
		for(CheckDataBean bean:checkList)
		{
			if(bean.getConfigDef().equals(AppConfigDef.isSupportWepay)
					|| bean.getConfigDef().equals(AppConfigDef.isSupportAlipay)
					|| bean.getConfigDef().equals(AppConfigDef.isSupportTenpay)
					|| bean.getConfigDef().equals(AppConfigDef.isSupportBaiduPay)
					|| bean.getConfigDef().equals(AppConfigDef.isSupportUnionPay))
			{
				dataList.add(bean);
			}
		}
		return dataList;
	}
}
