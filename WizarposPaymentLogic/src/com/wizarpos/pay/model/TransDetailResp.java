package com.wizarpos.pay.model;

import java.io.Serializable;
import java.util.List;

public class TransDetailResp implements Serializable {
    private String totalAmount;
    private String transTime;
    private String payTime;
    private List<DailyDetailResp> transDetail;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public List<DailyDetailResp> getTransDetail() {
        return transDetail;
    }

    public void setTransDetail(List<DailyDetailResp> transDetail) {
        this.transDetail = transDetail;
    }
}
