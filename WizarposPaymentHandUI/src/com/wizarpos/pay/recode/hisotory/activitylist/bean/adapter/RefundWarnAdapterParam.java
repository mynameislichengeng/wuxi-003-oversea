package com.wizarpos.pay.recode.hisotory.activitylist.bean.adapter;

public class RefundWarnAdapterParam {

    private String saleAmount;
    private String paymenType;
    private String receipt;
    private String refundAmount;
    private String time;

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getPaymenType() {
        return paymenType;
    }

    public void setPaymenType(String paymenType) {
        this.paymenType = paymenType;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
