package com.wizarpos.pay.model;

/**
 * Created by blue_sky on 2016/11/5.
 */

public class TipsConfigInfo {
    private String collectTips;//是否启用小费  ON启用 OFF禁用
    private String tipsPercentageAllow;//是否允许设置小费百分比 T允许 F不允许
    private String tipsPercentage;
    private String tipsCustomAllow;//是否允许顾客输入小费金额  T允许  F不允许
    private String tipsTerminalAllow;//是否允许客户端输入小费金额  T允许  F不允许

    public String getCollectTips() {
        return collectTips;
    }

    public void setCollectTips(String collectTips) {
        this.collectTips = collectTips;
    }

    public String getTipsPercentageAllow() {
        return tipsPercentageAllow;
    }

    public void setTipsPercentageAllow(String tipsPercentageAllow) {
        this.tipsPercentageAllow = tipsPercentageAllow;
    }

    public String getTipsPercentage() {
        return tipsPercentage;
    }

    public void setTipsPercentage(String tipsPercentage) {
        this.tipsPercentage = tipsPercentage;
    }

    public String getTipsCustomAllow() {
        return tipsCustomAllow;
    }

    public void setTipsCustomAllow(String tipsCustomAllow) {
        this.tipsCustomAllow = tipsCustomAllow;
    }

    public String getTipsTerminalAllow() {
        return tipsTerminalAllow;
    }

    public void setTipsTerminalAllow(String tipsTerminalAllow) {
        this.tipsTerminalAllow = tipsTerminalAllow;
    }
}
