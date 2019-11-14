package com.wizarpos.pay.model;

/**
 * 离线实例表
 */
public class OfflineSave {
	private String mid;
	private String fid;
	// private String mechantId;// 这三个字段是支付路由的getPosInfo方法返回的,建议加上,方便离线登陆时验证
	// private String terminalId;
	// private String operatorNo;
	private String jsonbject_1;
	private String jsonbject_2;
	private String jsonbject_3;
	private String jsonbject_4;

	public String getJsonbject_1() {
		return jsonbject_1;
	}

	public void setJsonbject_1(String jsonbject_1) {
		this.jsonbject_1 = jsonbject_1;
	}

	public String getJsonbject_2() {
		return jsonbject_2;
	}

	public void setJsonbject_2(String jsonbject_2) {
		this.jsonbject_2 = jsonbject_2;
	}

	public String getJsonbject_3() {
		return jsonbject_3;
	}

	public void setJsonbject_3(String jsonbject_3) {
		this.jsonbject_3 = jsonbject_3;
	}

	public String getJsonbject_4() {
		return jsonbject_4;
	}

	public void setJsonbject_4(String jsonbject_4) {
		this.jsonbject_4 = jsonbject_4;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

}