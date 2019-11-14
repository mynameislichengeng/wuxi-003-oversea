package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 终端信息实体
 * 
 * @author wu
 * 
 */
public class TerminalInfo implements Serializable {
	private String createTime;
	private String fid;
	private String lastTime;
	private String operatorType;
	private String sn;
	private String state;
	private String terminalNo;
	private String terminalOrderNo;
	private String validFlag;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getTerminalOrderNo() {
		return terminalOrderNo;
	}

	public void setTerminalOrderNo(String terminalOrderNo) {
		this.terminalOrderNo = terminalOrderNo;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

}
