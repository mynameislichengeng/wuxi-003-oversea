package com.wizarpos.pay.cashier.thrid_app_controller.model;

import java.io.Serializable;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdRequest;

public class ThirdAppTransactionBtnAvailable implements Serializable { // 根据旧有逻辑,出现什么禁用什么
	private String btnAliPay;
	private String aliPayFlag; // 禁用 1主扫 2被扫
	private String btnWxPay;
	private String wxPayFlag; // 禁用 1主扫 2被扫
	private String btnBaiduPay;
	private String baiduPayFlag; // 禁用 1主扫 2被扫
	private String btnQQPay;
	private String qqPayFlag; // 禁用 1主扫 2被扫
	private String btnBankPay;
	private String btnCashPay;
	private String btnMemberPay;
	private String btnMixPay;
	private String btnOtherPay;
	private String btnTicketPay;

	public String getBtnAliPay() {
		return btnAliPay;
	}

	public void setBtnAliPay(String btnAliPay) {
		this.btnAliPay = btnAliPay;
	}

	public String getBtnBankPay() {
		return btnBankPay;
	}

	public void setBtnBankPay(String btnBankPay) {
		this.btnBankPay = btnBankPay;
	}

	public String getBtnCashPay() {
		return btnCashPay;
	}

	public void setBtnCashPay(String btnCashPay) {
		this.btnCashPay = btnCashPay;
	}

	public String getBtnMemberPay() {
		return btnMemberPay;
	}

	public void setBtnMemberPay(String btnMemberPay) {
		this.btnMemberPay = btnMemberPay;
	}

	public String getBtnMixPay() {
		return btnMixPay;
	}

	public void setBtnMixPay(String btnMixPay) {
		this.btnMixPay = btnMixPay;
	}

	public String getBtnOtherPay() {
		return btnOtherPay;
	}

	public void setBtnOtherPay(String btnOtherPay) {
		this.btnOtherPay = btnOtherPay;
	}

	public String getBtnTicketPay() {
		return btnTicketPay;
	}

	public void setBtnTicketPay(String btnTicketPay) {
		this.btnTicketPay = btnTicketPay;
	}

	public String getBtnWxPay() {
		return btnWxPay;
	}

	public void setBtnWxPay(String btnWxPay) {
		this.btnWxPay = btnWxPay;
	}

	public void setWxPayFlag(String wxPayFlag) {
		this.wxPayFlag = wxPayFlag;
	}

	public String getWxPayFlag() {
		return wxPayFlag;
	}

	public void setAliPayFlag(String aliPayFlag) {
		this.aliPayFlag = aliPayFlag;
	}

	public String getAliPayFlag() {
		return aliPayFlag;
	}

	public String getBtnBaiduPay() {
		return btnBaiduPay;
	}

	public void setBtnBaiduPay(String btnBaiduPay) {
		this.btnBaiduPay = btnBaiduPay;
	}

	public String getBtnQQPay() {
		return btnQQPay;
	}

	public void setBtnQQPay(String btnQQPay) {
		this.btnQQPay = btnQQPay;
	}

	public String getBaiduPayFlag() {
		return baiduPayFlag;
	}

	public void setBaiduPayFlag(String baiduPayFlag) {
		this.baiduPayFlag = baiduPayFlag;
	}

	public String getQqPayFlag() {
		return qqPayFlag;
	}

	public void setQqPayFlag(String qqPayFlag) {
		this.qqPayFlag = qqPayFlag;
	}
}
