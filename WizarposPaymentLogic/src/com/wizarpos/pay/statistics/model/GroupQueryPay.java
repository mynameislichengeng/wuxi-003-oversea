package com.wizarpos.pay.statistics.model;

import java.io.Serializable;

public class GroupQueryPay implements Serializable {
	private String tcount;
	private long total;
	private int tranCode;
	private String tranLogId;

	public String getTcount() {
		return tcount;
	}

	public void setTcount(String tcount) {
		this.tcount = tcount;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getTranCode() {
		return tranCode;
	}

	public void setTranCode(int tranCode) {
		this.tranCode = tranCode;
	}

	public String getTranLogId() {
		return tranLogId;
	}

	public void setTranLogId(String tranLogId) {
		this.tranLogId = tranLogId;
	}

}
