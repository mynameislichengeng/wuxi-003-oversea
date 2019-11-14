package com.wizarpos.pay.statistics.model;

import java.io.Serializable;

/**
 * 用来展示的交易汇总记录
 * 
 * @author wu
 */
public class GroupQueryBean implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private String transKind; // 交易类型
	private int transAmount; // 交易金额
	private int transCount;// 交易笔数

	public GroupQueryBean() {
	}

	public GroupQueryBean(String transKind, int transAmount, int transCount) {
		super();
		this.transKind = transKind;
		this.transAmount = transAmount;
		this.transCount = transCount;
	}

	public String getTransKind() {
		return transKind;
	}

	public void setTransKind(String transKind) {
		this.transKind = transKind;
	}

	public int getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(int transAmount) {
		this.transAmount = transAmount;
	}

	public int getTransCount() {
		return transCount;
	}

	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 增加交易金额
	 * 
	 * @param transAmount
	 */
	public void addTransAmount(int transAmount) {
		this.transAmount = this.transAmount + transAmount;
	}

	/**
	 * 增加交易笔数
	 * 
	 * @param transCount
	 */
	public void addTransCount(int transCount) {
		this.transCount = this.transCount + transCount;
	}

}
