package com.wizarpos.pay.model;

import com.wizarpos.pay.common.Constants;

import java.io.Serializable;

/**
 * 100 登录接口 增加上送信息参数 terminalInfo
 * Created by Song on 2016/6/29.
 */
public class AppInfo implements Serializable{
    /**
     * 固件版本号
     */
    private String buildVersion;
    /**
     * api版本号
     */
    private String apiVersion;
    /**
     * 上送时间
     */
    private long uploadTime;
    /**
     * 应用信息
     */
    private String packageInfos;
    /**
     * 机器型号
     */
    private String model;
    /**
     * 内核版本
     */
    private String kernelVersion;
    /**
     * 安卓版本
     */
    private String androidVersion;
    /**
     * 安全模块固件版本
     */
    private String hsmVersion;

    public AppInfo() {
        uploadTime = System.currentTimeMillis();
        apiVersion = Constants.SERVER_VERSION;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getPackageInfos() {
        return packageInfos;
    }

    public void setPackageInfos(String packageInfos) {
        this.packageInfos = packageInfos;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getHsmVersion() {
        return hsmVersion;
    }

    public void setHsmVersion(String hsmVersion) {
        this.hsmVersion = hsmVersion;
    }
}
