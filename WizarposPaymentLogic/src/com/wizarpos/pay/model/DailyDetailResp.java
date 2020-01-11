package com.wizarpos.pay.model;

import java.io.Serializable;

public class DailyDetailResp implements Serializable {
    private String discountAmount;
    private String masterTranLogId;
    private String tranLogId;
    //    private String tran_time;
    private String transKind;
    private String transType;
    private String transAmount;
    private String refundAmount;
    private String thirdTradeNo;
    private String transCurrency;// 结算类型
    private String settlementAmount;
    private String settlementCurrency;
    private String merchantTradeCode;

    private String tipAmount;
    private String thirdExtId;
    private String thirdExtName;
    private String exchangeRate;
    private String cnyAmount;//服务端返回交易的人民币金额

    public boolean isExpand = false;
    private String tranType;
    private String transName;
    private String singleAmount;
    private String tranTime;
    private String tranlogId;
    private String payTime;
    private String cancelKind;
    private String sn;


    private String optName;
    private String bank_info;

    public String getCancelKind() {
        return cancelKind;
    }

    public void setCancelKind(String cancelKind) {
        this.cancelKind = cancelKind;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }


    public String getThirdExtId() {
        return thirdExtId;
    }

    public void setThirdExtId(String thirdExtId) {
        this.thirdExtId = thirdExtId;
    }

    public String getThirdExtName() {
        return thirdExtName;
    }

    public void setThirdExtName(String thirdExtName) {
        this.thirdExtName = thirdExtName;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }


    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getTranLogId() {
        return tranLogId;
    }

    public void setTranLogId(String tranLogId) {
        this.tranLogId = tranLogId;
    }


    public String getTransKind() {
        return transKind;
    }

    public void setTransKind(String transKind) {
        this.transKind = transKind;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

//    public boolean isExpand() {
//        return isExpand;
//    }
//
//    public void setExpand(boolean expand) {
//        isExpand = expand;
//    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(String singleAmount) {
        this.singleAmount = singleAmount;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranlogId() {
        return tranlogId;
    }

    public void setTranlogId(String tranlogId) {
        this.tranlogId = tranlogId;
    }

    public String getBank_info() {
        return bank_info;
    }

    public void setBank_info(String bank_info) {
        this.bank_info = bank_info;
    }

    public String getMasterTranLogId() {
        return masterTranLogId;
    }

    public void setMasterTranLogId(String masterTranLogId) {
        this.masterTranLogId = masterTranLogId;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCnyAmount() {
        return cnyAmount;
    }

    public void setCnyAmount(String cnyAmount) {
        this.cnyAmount = cnyAmount;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMerchantTradeCode() {
        return merchantTradeCode;
    }

    public void setMerchantTradeCode(String merchantTradeCode) {
        this.merchantTradeCode = merchantTradeCode;
    }
}
