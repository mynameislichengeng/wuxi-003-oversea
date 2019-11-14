package com.wizarpos.pay.statistics.model;

import java.io.Serializable;

/**
 * 将交易汇总查询返回的记录封装为对象
 * 
 * @author wu
 */
public class TransRecord implements Serializable {
	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private int tranCode;// 交易类型
	private int tcount;// 交易数量
	private int total;// 总计金额
	private String tranLogId;// 交易订单号

	public int getTranCode() {
		return tranCode;
	}

	public void setTranCode(int tranCode) {
		this.tranCode = tranCode;
	}

	public int getTcount() {
		return tcount;
	}

	public void setTcount(int tcount) {
		this.tcount = tcount;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getTranLogId() {
		return tranLogId;
	}

	public void setTranLogId(String tranLogId) {
		this.tranLogId = tranLogId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
