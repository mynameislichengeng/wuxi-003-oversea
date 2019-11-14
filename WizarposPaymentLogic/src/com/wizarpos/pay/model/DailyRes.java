package com.wizarpos.pay.model;

import java.io.Serializable;
import java.util.List;

/**
 * 日结单返回实体
 */
public class DailyRes implements Serializable {
    private List<TodayDetailBean> list;//日结单显示
    private String totalAmount;//当天消费总金额
    private String consumptionAmount;//消费金额
    private String transTotalAmount;//交易总金额
    private String transTotalAcount;//交易总笔数
    private String revokeAmount;//撤销金额
    private String refundAmount;//撤销交易总金额
    private String refundCount;//撤销交易总笔数
    private long currentDay;//当天


    public long getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(long currentDay) {
        this.currentDay = currentDay;
    }

    public List<TodayDetailBean> getList() {
        return list;
    }

    public void setList(List<TodayDetailBean> list) {
        this.list = list;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getConsumptionAmount() {
        return consumptionAmount;
    }

    public void setConsumptionAmount(String consumptionAmount) {
        this.consumptionAmount = consumptionAmount;
    }

    public String getRevokeAmount() {
        return revokeAmount;
    }

    public void setRevokeAmount(String revokeAmount) {
        this.revokeAmount = revokeAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(String refundCount) {
        this.refundCount = refundCount;
    }

    public String getTransTotalAmount() {
        return transTotalAmount;
    }

    public void setTransTotalAmount(String transTotalAmount) {
        this.transTotalAmount = transTotalAmount;
    }

    public String getTransTotalAcount() {
        return transTotalAcount;
    }

    public void setTransTotalAcount(String transTotalAcount) {
        this.transTotalAcount = transTotalAcount;
    }
}
