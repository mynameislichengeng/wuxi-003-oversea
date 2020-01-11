package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * Created by 苏震 on 2016/11/18.
 */

public class RefundDetailResp implements Serializable {

    private String masterTranLogId;
    private String tranLogId;
    private String tran_time;
    private String transKind;
    private String refundAmount;
    private String thirdTradeNo;
    private String thirdExtId;
    private String thirdExtName;
    private String exchangeRate;
    private String payTime;//撤销时间
    private String cnyAmount;//服务端返回交易的人民币金额
    private String transCurrency;
    private String sn;
    private String optName;
    private String settlementCurrency;
    private String settlementAmount;
    private String merchantTradeCode;

    private String tranAmount;

    public String getThirdExtName() {
        return thirdExtName;
    }

    public void setThirdExtName(String thirdExtName) {
        this.thirdExtName = thirdExtName;
    }

    public String getMasterTranLogId() {
        return masterTranLogId;
    }

    public void setMasterTranLogId(String masterTranLogId) {
        this.masterTranLogId = masterTranLogId;
    }

    public String getTranLogId() {
        return tranLogId;
    }

    public void setTranLogId(String tranLogId) {
        this.tranLogId = tranLogId;
    }

    public String getTran_time() {
        return tran_time;
    }

    public void setTran_time(String tran_time) {
        this.tran_time = tran_time;
    }

    public String getTransKind() {
        return transKind;
    }

    public void setTransKind(String transKind) {
        this.transKind = transKind;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getThirdExtId() {
        return thirdExtId;
    }

    public void setThirdExtId(String thirdExtId) {
        this.thirdExtId = thirdExtId;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getMerchantTradeCode() {
        return merchantTradeCode;
    }

    public void setMerchantTradeCode(String merchantTradeCode) {
        this.merchantTradeCode = merchantTradeCode;
    }
}
