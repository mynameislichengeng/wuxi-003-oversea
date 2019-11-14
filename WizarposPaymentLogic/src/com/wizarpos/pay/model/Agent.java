package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 代理商实体
 *
 * @author wu
 */
public class Agent implements Serializable {
    private String handLogoUrl;
    private String appLogoUrl;

    public String getHandLogoUrl() {
        return handLogoUrl;
    }

    public void setHandLogoUrl(String handLogoUrl) {
        this.handLogoUrl = handLogoUrl;
    }

    public String getAppLogoUrl() {
        return appLogoUrl;
    }

    public void setAppLogoUrl(String appLogoUrl) {
        this.appLogoUrl = appLogoUrl;
    }
}
