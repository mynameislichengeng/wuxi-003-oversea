package com.wizarpos.base.net;

import java.io.Serializable;

public class LoginResp implements Serializable {

    private long currentTime;
    private MerchantInfo merchant;
    private boolean merchant_has_weixin;
    private long symTime;
    private long sysPosTimeStamp;
    private String merchantType; //商户类型 wu@[20150824]
    private String alipay;//支付宝是否开启@hong
    private String baidu;//百付宝是否开启@hong
    private String qq;//QQ钱包是否开启@hong
    private String weixin;//微信是否开启@hong

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public MerchantInfo getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantInfo merchant) {
        this.merchant = merchant;
    }

    public boolean isMerchant_has_weixin() {
        return merchant_has_weixin;
    }

    public void setMerchant_has_weixin(boolean merchant_has_weixin) {
        this.merchant_has_weixin = merchant_has_weixin;
    }

    public long getSymTime() {
        return symTime;
    }

    public void setSymTime(long symTime) {
        this.symTime = symTime;
    }

    public long getSysPosTimeStamp() {
        return sysPosTimeStamp;
    }

    public void setSysPosTimeStamp(long sysPosTimeStamp) {
        this.sysPosTimeStamp = sysPosTimeStamp;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getBaidu() {
        return baidu;
    }

    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
