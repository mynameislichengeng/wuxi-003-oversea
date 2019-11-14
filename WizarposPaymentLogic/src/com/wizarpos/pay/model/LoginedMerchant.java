package com.wizarpos.pay.model;

import java.util.List;

/**
 * 用于存储登陆过的商户信息实体
 * Created by Song on 2016/2/18.
 */
public class LoginedMerchant {
    private String merchantName;
    private String mid;
    private List<String> administrators;
    private long lastOpreatorUpateTime;
    private long lastTerminalInfoUploadTime = 0L;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public List<String> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<String> administrators) {
        this.administrators = administrators;
    }

    public long getLastOpreatorUpateTime() {
        return lastOpreatorUpateTime;
    }

    public void setLastOpreatorUpateTime(long lastOpreatorUpateTime) {
        this.lastOpreatorUpateTime = lastOpreatorUpateTime;
    }

    public long getLastTerminalInfoUploadTime() {
        return lastTerminalInfoUploadTime;
    }

    public void setLastTerminalInfoUploadTime(long lastTerminalInfoUploadTime) {
        this.lastTerminalInfoUploadTime = lastTerminalInfoUploadTime;
    }
}
