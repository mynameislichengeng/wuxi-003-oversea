package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 商户信息实体
 *
 * @author wu
 */
public class MerchantInfo implements Serializable {
    private String bannerImage;
    private String cardStartNo;
    private String cityLoct;
    private String collectNotifyMark;
    private String contacts;
    private String createTime;
    private String lastTime;
    private String logoImage;
    private String merchantAddr;
    private String merchantId;
    private String merchantName;
    private String merchantSummary;
    private String merchantTel;
    private String mgtLoct;
    private String mid;
    private String payId;
    private String saleNotifyMark;
    private String sequenceNo;
    private String shopDesc;
    private String startNo;
    private String storesTemplates;
    private String usePayRouteConfig;
    private String useWizarposAlipayConfig;
    private String useWizarposWeixinPayConfig;
    private String validFlag;
    private String weixinAppId;
    private String weixinAppSecret;
    private String weixinMchId;
    private String weixinPartnerKey;
    private String wxAccessToken;
    private String wxAccessTokenTimestamp;
    private String saleDeductType;//销售单提成
    private String isOpenMemberDeduct;//会员充值是否开启员工提成 "0"为关闭 "1"为开启
    private String handLogoImage;//手持机商户 logo
    private String appLogoUrl;//pos 机商户 logo

    public String getHandLogoUrl() {
        return handLogoImage;
    }

    public String getHandLogoImage() {
        return handLogoImage;
    }

    public void setHandLogoImage(String handLogoImage) {
        this.handLogoImage = handLogoImage;
    }

    public String getAppLogoUrl() {
        return appLogoUrl;
    }

    public void setAppLogoUrl(String appLogoUrl) {
        this.appLogoUrl = appLogoUrl;
    }

    public void setSaleDeductType(String saleDeductType) {
        this.saleDeductType = saleDeductType;
    }

    public String getSaleDeductType() {
        return saleDeductType;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getCardStartNo() {
        return cardStartNo;
    }

    public void setCardStartNo(String cardStartNo) {
        this.cardStartNo = cardStartNo;
    }

    public String getCityLoct() {
        return cityLoct;
    }

    public void setCityLoct(String cityLoct) {
        this.cityLoct = cityLoct;
    }

    public String getCollectNotifyMark() {
        return collectNotifyMark;
    }

    public void setCollectNotifyMark(String collectNotifyMark) {
        this.collectNotifyMark = collectNotifyMark;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getMerchantAddr() {
        return merchantAddr;
    }

    public void setMerchantAddr(String merchantAddr) {
        this.merchantAddr = merchantAddr;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantSummary() {
        return merchantSummary;
    }

    public void setMerchantSummary(String merchantSummary) {
        this.merchantSummary = merchantSummary;
    }

    public String getMerchantTel() {
        return merchantTel;
    }

    public void setMerchantTel(String merchantTel) {
        this.merchantTel = merchantTel;
    }

    public String getMgtLoct() {
        return mgtLoct;
    }

    public void setMgtLoct(String mgtLoct) {
        this.mgtLoct = mgtLoct;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getSaleNotifyMark() {
        return saleNotifyMark;
    }

    public void setSaleNotifyMark(String saleNotifyMark) {
        this.saleNotifyMark = saleNotifyMark;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getStartNo() {
        return startNo;
    }

    public void setStartNo(String startNo) {
        this.startNo = startNo;
    }

    public String getStoresTemplates() {
        return storesTemplates;
    }

    public void setStoresTemplates(String storesTemplates) {
        this.storesTemplates = storesTemplates;
    }

    public String getUsePayRouteConfig() {
        return usePayRouteConfig;
    }

    public void setUsePayRouteConfig(String usePayRouteConfig) {
        this.usePayRouteConfig = usePayRouteConfig;
    }

    public String getUseWizarposAlipayConfig() {
        return useWizarposAlipayConfig;
    }

    public void setUseWizarposAlipayConfig(String useWizarposAlipayConfig) {
        this.useWizarposAlipayConfig = useWizarposAlipayConfig;
    }

    public String getUseWizarposWeixinPayConfig() {
        return useWizarposWeixinPayConfig;
    }

    public void setUseWizarposWeixinPayConfig(String useWizarposWeixinPayConfig) {
        this.useWizarposWeixinPayConfig = useWizarposWeixinPayConfig;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getWeixinAppId() {
        return weixinAppId;
    }

    public void setWeixinAppId(String weixinAppId) {
        this.weixinAppId = weixinAppId;
    }

    public String getWeixinAppSecret() {
        return weixinAppSecret;
    }

    public void setWeixinAppSecret(String weixinAppSecret) {
        this.weixinAppSecret = weixinAppSecret;
    }

    public String getWeixinMchId() {
        return weixinMchId;
    }

    public void setWeixinMchId(String weixinMchId) {
        this.weixinMchId = weixinMchId;
    }

    public String getWeixinPartnerKey() {
        return weixinPartnerKey;
    }

    public void setWeixinPartnerKey(String weixinPartnerKey) {
        this.weixinPartnerKey = weixinPartnerKey;
    }

    public String getWxAccessToken() {
        return wxAccessToken;
    }

    public void setWxAccessToken(String wxAccessToken) {
        this.wxAccessToken = wxAccessToken;
    }

    public String getWxAccessTokenTimestamp() {
        return wxAccessTokenTimestamp;
    }

    public void setWxAccessTokenTimestamp(String wxAccessTokenTimestamp) {
        this.wxAccessTokenTimestamp = wxAccessTokenTimestamp;
    }

    public String getIsOpenMemberDeduct() {
        return isOpenMemberDeduct;
    }

    public void setIsOpenMemberDeduct(String isOpenMemberDeduct) {
        this.isOpenMemberDeduct = isOpenMemberDeduct;
    }

}
