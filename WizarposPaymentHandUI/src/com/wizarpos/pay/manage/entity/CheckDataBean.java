package com.wizarpos.pay.manage.entity;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-4 下午5:54:26
 * @Description: 支付方式选择界面Bean 
 */
public class CheckDataBean {
	private String checkName;//checkbox名称
	private boolean isChecked = false;//Checkbox选择状态
	private boolean isVisible = true;//是否可见 默认可见
	private String configDef;//对应数据库的字段
	
	public CheckDataBean(String checkName)
	{
		this.checkName = checkName;
	}
	public CheckDataBean(String checkName,String configDef)
	{
		this.checkName = checkName;
		this.configDef = configDef;
	}
	
	public CheckDataBean(String checkName,boolean isChecked)
	{
		this.checkName = checkName;
		this.isChecked = isChecked;
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getConfigDef() {
		return configDef;
	}

	public void setConfigDef(String configDef) {
		this.configDef = configDef;
	}
	
	
}
