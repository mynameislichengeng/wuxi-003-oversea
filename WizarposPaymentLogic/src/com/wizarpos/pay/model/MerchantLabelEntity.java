package com.wizarpos.pay.model;

import java.util.List;

/**
 * @Author: yaosong
 * @date 2016-2-17 上午9:59:48
 * @Description:商户标签实体
 */
public class MerchantLabelEntity {

	private List<MerchantLabelChildEntity> child;

	private String id;

	private String labelName;

	private String labelType;

	public void setChild(List<MerchantLabelChildEntity> child) {
		this.child = child;
	}

	public List<MerchantLabelChildEntity> getChild() {
		return this.child;
	}

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
