package com.wizarpos.pay.ui.newui.entity;

import java.io.Serializable;

/**
 * Created by blue_sky on 2016/3/28.
 */
public class MyItem implements Serializable{
    public boolean isExpand = false;
    private String tranType;
    private String transName;
    private String singleAmount;
    private String transTime;
    private String tranlogId;
    private String optName;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
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

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTranlogId() {
        return tranlogId;
    }

    public void setTranlogId(String tranlogId) {
        this.tranlogId = tranlogId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }
}
