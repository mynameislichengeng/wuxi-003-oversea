package com.wizarpos.pay.recode.zusao.bean.acti;

import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;

public class ZSSelectTypeActivityParam {



    private ZsPayChannelEnum payChannelEnum;

    private String realAmount;

    private String tipAmount;

    private String invoiceNo;


    public ZsPayChannelEnum getPayChannelEnum() {
        return payChannelEnum;
    }

    public void setPayChannelEnum(ZsPayChannelEnum payChannelEnum) {
        this.payChannelEnum = payChannelEnum;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
