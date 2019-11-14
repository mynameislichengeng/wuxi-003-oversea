package com.wizarpos.pay.model;

/**
 * @Author: yaosong
 * @date 2016-2-17 上午10:04:59
 * @Description:子标签实体
 */
public class MerchantLabelChildEntity {

	private String id;

	private String labelName;

	private String labelType;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelName() {
		return this.labelName;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public String getLabelType() {
		return this.labelType;
	}
}
