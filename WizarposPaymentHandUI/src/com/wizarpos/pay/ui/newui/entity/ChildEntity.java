package com.wizarpos.pay.ui.newui.entity;

/**
 * Created by vienan on 2015/9/17.
 */
public class ChildEntity {
    private String childTitle;
    private String tranType;
    private String transName;
    private String singleAmount;
    public ChildEntity(String childTitle) {
        this.childTitle = childTitle;
    }

    public String getChildTitle() {
        return childTitle;
    }

    public void setChildTitle(String childTitle) {
        this.childTitle = childTitle;
    }

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
}
