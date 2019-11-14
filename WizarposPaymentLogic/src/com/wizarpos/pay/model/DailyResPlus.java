package com.wizarpos.pay.model;

import java.io.Serializable;
import java.util.List;

/**
 * 日结单返回实体
 */
public class DailyResPlus implements Serializable {
    private List<TodayDetailBean> list;//日结单显示
    private int grossSalesNumber;//销售总笔数
    private int grossSalesAmount;//销售总金额，不包括小费
    private int fullRefundsNumber;//全额退款总笔数
    private int fullRefundsAmount;//全款退款总金额
    private int partialRefundsNumber;//部分退款总笔数
    private int partialRefundsAmount;//部分退款总金额
    private int netSalesNumber;//净销售总笔数=销售总笔数+全额退款总笔数+部分退款总笔数
    private int netSalesAmount;//净销售总金额=销售总金额+全款退款总金额+部分退款总金额
    private int tipsNumber;//小费总笔费
    private int tipsAmount;//小费总金额
    private int totalCollected;//总收款金额=净销售总金额+小费总金额

    private long currentDay;//当天

    public List<TodayDetailBean> getList() {
        return list;
    }

    public void setList(List<TodayDetailBean> list) {
        this.list = list;
    }

    public int getGrossSalesNumber() {
        return grossSalesNumber;
    }

    public void setGrossSalesNumber(int grossSalesNumber) {
        this.grossSalesNumber = grossSalesNumber;
    }

    public int getGrossSalesAmount() {
        return grossSalesAmount;
    }

    public void setGrossSalesAmount(int grossSalesAmount) {
        this.grossSalesAmount = grossSalesAmount;
    }

    public int getFullRefundsNumber() {
        return fullRefundsNumber;
    }

    public void setFullRefundsNumber(int fullRefundsNumber) {
        this.fullRefundsNumber = fullRefundsNumber;
    }

    public int getFullRefundsAmount() {
        return fullRefundsAmount;
    }

    public void setFullRefundsAmount(int fullRefundsAmount) {
        this.fullRefundsAmount = fullRefundsAmount;
    }

    public int getPartialRefundsNumber() {
        return partialRefundsNumber;
    }

    public void setPartialRefundsNumber(int partialRefundsNumber) {
        this.partialRefundsNumber = partialRefundsNumber;
    }

    public int getPartialRefundsAmount() {
        return partialRefundsAmount;
    }

    public void setPartialRefundsAmount(int partialRefundsAmount) {
        this.partialRefundsAmount = partialRefundsAmount;
    }

    public int getNetSalesNumber() {
        return netSalesNumber;
    }

    public void setNetSalesNumber(int netSalesNumber) {
        this.netSalesNumber = netSalesNumber;
    }

    public int getNetSalesAmount() {
        return netSalesAmount;
    }

    public void setNetSalesAmount(int netSalesAmount) {
        this.netSalesAmount = netSalesAmount;
    }

    public int getTipsNumber() {
        return tipsNumber;
    }

    public void setTipsNumber(int tipsNumber) {
        this.tipsNumber = tipsNumber;
    }

    public int getTipsAmount() {
        return tipsAmount;
    }

    public void setTipsAmount(int tipsAmount) {
        this.tipsAmount = tipsAmount;
    }

    public int getTotalCollected() {
        return totalCollected;
    }

    public void setTotalCollected(int totalCollected) {
        this.totalCollected = totalCollected;
    }

    public long getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(long currentDay) {
        this.currentDay = currentDay;
    }
}
