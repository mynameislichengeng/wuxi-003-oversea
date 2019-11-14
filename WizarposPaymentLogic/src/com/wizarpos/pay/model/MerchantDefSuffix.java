package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * Created by blue_sky on 2016/11/4.
 */

public class MerchantDefSuffix implements Serializable{
    private String businessFlag;
    private String convertCurrency;
    private String feizhuFlag;
    private String mid;
    private String sortBy;
    private String tipsCustomAllow;
    private String tipsPercentageAllow;
    private String tipsTerminalAllow;
    private String collectTips;
    private String tipsPercentage;

    public String getBusinessFlag() {
        return businessFlag;
    }

    public void setBusinessFlag(String businessFlag) {
        this.businessFlag = businessFlag;
    }

    public String getConvertCurrency() {
        return convertCurrency;
    }

    public void setConvertCurrency(String convertCurrency) {
        this.convertCurrency = convertCurrency;
    }

    public String getFeizhuFlag() {
        return feizhuFlag;
    }

    public void setFeizhuFlag(String feizhuFlag) {
        this.feizhuFlag = feizhuFlag;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getTipsCustomAllow() {
        return tipsCustomAllow;
    }

    public void setTipsCustomAllow(String tipsCustomAllow) {
        this.tipsCustomAllow = tipsCustomAllow;
    }

    public String getTipsPercentageAllow() {
        return tipsPercentageAllow;
    }

    public void setTipsPercentageAllow(String tipsPercentageAllow) {
        this.tipsPercentageAllow = tipsPercentageAllow;
    }

    public String getTipsTerminalAllow() {
        return tipsTerminalAllow;
    }

    public void setTipsTerminalAllow(String tipsTerminalAllow) {
        this.tipsTerminalAllow = tipsTerminalAllow;
    }

    public String getCollectTips() {
        return collectTips;
    }

    public void setCollectTips(String collectTips) {
        this.collectTips = collectTips;
    }

    public String getTipsPercentage() {
        return tipsPercentage;
    }

    public void setTipsPercentage(String tipsPercentage) {
        this.tipsPercentage = tipsPercentage;
    }
}
