package com.wizarpos.pay.recode.zusao.bean.resp;

public class ZSQueryOrderStatusResp {


    /**
     * amount : 1
     * cardNo :
     * cnyAmount : 0
     * convenAmount : 0
     * createTime : 1616331651000
     * disCount : 0
     * districtZone : EDT
     * exchangeRate : 5.21896
     * extraAmount : 0
     * fid : 100105100000023
     * id : P100100020000067202103210014
     * inputAmount : 1
     * isHook : 0
     * lastTime : 1616331651269
     * merchantDef : {"adminMobileNo":"6134494510","contacts":"Jianjiang Li ","createTime":1494296923000,"isOpenWxactive":"0","isVirtualMainStore":"0","lastTime":1616046661960,"logoImage":"http://wizarpos-1252884747.file.myqcloud.com/image/100100020000067_2021012203480698057.jpg","marketActivityFlag":"1","merchantAddr":"228 Hunt Club Rd, Unit 201, Ottawa, ON, K1V 1C1","merchantName":"Merchant Demo","merchantShortName":"MP DEMO","merchantTel":"6133196686","merchantType":"1","mid":"100100020000067","negaStockFlag":"1","payId":0,"usePayRouteConfig":"99","validFlag":"0","vcode":"26502322","virtualMainStoreMid":"100105100000023","wxactiveContent":""}
     * merchantTradeCode :
     * mixedFlag : 0
     * mnFlag : native
     * orderNo : 02100100020000067202103210006
     * paySource : 1
     * payTime : 2021-03-21 13:00:51
     * payType : W
     * refundAmount : 0
     * settlementAmount : 0
     * settlementCurrency : CAD
     * sn : 8200000551
     * state : 1
     * subTranCode : 814
     * ticketNums : 0
     * timeZone : Canada/Eastern
     * tipAmount : 0
     * tranCode : 814
     * transCurrency : CAD
     */

    private int amount;
    private String cardNo;
    private int cnyAmount;
    private int convenAmount;
    private long createTime;
    private int disCount;
    private String districtZone;
    private double exchangeRate;
    private int extraAmount;
    private String fid;
    private String id;
    private int inputAmount;
    private String isHook;
    private long lastTime;
    private MerchantDefBean merchantDef;
    private String merchantTradeCode;
    private String mixedFlag;
    private String mnFlag;
    private String orderNo;
    private String paySource;
    private String payTime;
    private String payType;
    private int refundAmount;
    private int settlementAmount;
    private String settlementCurrency;
    private String sn;
    private int state;
    private String subTranCode;
    private String ticketNums;
    private String timeZone;
    private int tipAmount;
    private String tranCode;
    private String transCurrency;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCnyAmount() {
        return cnyAmount;
    }

    public void setCnyAmount(int cnyAmount) {
        this.cnyAmount = cnyAmount;
    }

    public int getConvenAmount() {
        return convenAmount;
    }

    public void setConvenAmount(int convenAmount) {
        this.convenAmount = convenAmount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDisCount() {
        return disCount;
    }

    public void setDisCount(int disCount) {
        this.disCount = disCount;
    }

    public String getDistrictZone() {
        return districtZone;
    }

    public void setDistrictZone(String districtZone) {
        this.districtZone = districtZone;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public int getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(int extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInputAmount() {
        return inputAmount;
    }

    public void setInputAmount(int inputAmount) {
        this.inputAmount = inputAmount;
    }

    public String getIsHook() {
        return isHook;
    }

    public void setIsHook(String isHook) {
        this.isHook = isHook;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public MerchantDefBean getMerchantDef() {
        return merchantDef;
    }

    public void setMerchantDef(MerchantDefBean merchantDef) {
        this.merchantDef = merchantDef;
    }

    public String getMerchantTradeCode() {
        return merchantTradeCode;
    }

    public void setMerchantTradeCode(String merchantTradeCode) {
        this.merchantTradeCode = merchantTradeCode;
    }

    public String getMixedFlag() {
        return mixedFlag;
    }

    public void setMixedFlag(String mixedFlag) {
        this.mixedFlag = mixedFlag;
    }

    public String getMnFlag() {
        return mnFlag;
    }

    public void setMnFlag(String mnFlag) {
        this.mnFlag = mnFlag;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(int settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSubTranCode() {
        return subTranCode;
    }

    public void setSubTranCode(String subTranCode) {
        this.subTranCode = subTranCode;
    }

    public String getTicketNums() {
        return ticketNums;
    }

    public void setTicketNums(String ticketNums) {
        this.ticketNums = ticketNums;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(int tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public static class MerchantDefBean {
        /**
         * adminMobileNo : 6134494510
         * contacts : Jianjiang Li
         * createTime : 1494296923000
         * isOpenWxactive : 0
         * isVirtualMainStore : 0
         * lastTime : 1616046661960
         * logoImage : http://wizarpos-1252884747.file.myqcloud.com/image/100100020000067_2021012203480698057.jpg
         * marketActivityFlag : 1
         * merchantAddr : 228 Hunt Club Rd, Unit 201, Ottawa, ON, K1V 1C1
         * merchantName : Merchant Demo
         * merchantShortName : MP DEMO
         * merchantTel : 6133196686
         * merchantType : 1
         * mid : 100100020000067
         * negaStockFlag : 1
         * payId : 0
         * usePayRouteConfig : 99
         * validFlag : 0
         * vcode : 26502322
         * virtualMainStoreMid : 100105100000023
         * wxactiveContent :
         */

        private String adminMobileNo;
        private String contacts;
        private long createTime;
        private String isOpenWxactive;
        private String isVirtualMainStore;
        private long lastTime;
        private String logoImage;
        private String marketActivityFlag;
        private String merchantAddr;
        private String merchantName;
        private String merchantShortName;
        private String merchantTel;
        private String merchantType;
        private String mid;
        private String negaStockFlag;
        private int payId;
        private String usePayRouteConfig;
        private String validFlag;
        private String vcode;
        private String virtualMainStoreMid;
        private String wxactiveContent;

        public String getAdminMobileNo() {
            return adminMobileNo;
        }

        public void setAdminMobileNo(String adminMobileNo) {
            this.adminMobileNo = adminMobileNo;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getIsOpenWxactive() {
            return isOpenWxactive;
        }

        public void setIsOpenWxactive(String isOpenWxactive) {
            this.isOpenWxactive = isOpenWxactive;
        }

        public String getIsVirtualMainStore() {
            return isVirtualMainStore;
        }

        public void setIsVirtualMainStore(String isVirtualMainStore) {
            this.isVirtualMainStore = isVirtualMainStore;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public String getLogoImage() {
            return logoImage;
        }

        public void setLogoImage(String logoImage) {
            this.logoImage = logoImage;
        }

        public String getMarketActivityFlag() {
            return marketActivityFlag;
        }

        public void setMarketActivityFlag(String marketActivityFlag) {
            this.marketActivityFlag = marketActivityFlag;
        }

        public String getMerchantAddr() {
            return merchantAddr;
        }

        public void setMerchantAddr(String merchantAddr) {
            this.merchantAddr = merchantAddr;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantShortName() {
            return merchantShortName;
        }

        public void setMerchantShortName(String merchantShortName) {
            this.merchantShortName = merchantShortName;
        }

        public String getMerchantTel() {
            return merchantTel;
        }

        public void setMerchantTel(String merchantTel) {
            this.merchantTel = merchantTel;
        }

        public String getMerchantType() {
            return merchantType;
        }

        public void setMerchantType(String merchantType) {
            this.merchantType = merchantType;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getNegaStockFlag() {
            return negaStockFlag;
        }

        public void setNegaStockFlag(String negaStockFlag) {
            this.negaStockFlag = negaStockFlag;
        }

        public int getPayId() {
            return payId;
        }

        public void setPayId(int payId) {
            this.payId = payId;
        }

        public String getUsePayRouteConfig() {
            return usePayRouteConfig;
        }

        public void setUsePayRouteConfig(String usePayRouteConfig) {
            this.usePayRouteConfig = usePayRouteConfig;
        }

        public String getValidFlag() {
            return validFlag;
        }

        public void setValidFlag(String validFlag) {
            this.validFlag = validFlag;
        }

        public String getVcode() {
            return vcode;
        }

        public void setVcode(String vcode) {
            this.vcode = vcode;
        }

        public String getVirtualMainStoreMid() {
            return virtualMainStoreMid;
        }

        public void setVirtualMainStoreMid(String virtualMainStoreMid) {
            this.virtualMainStoreMid = virtualMainStoreMid;
        }

        public String getWxactiveContent() {
            return wxactiveContent;
        }

        public void setWxactiveContent(String wxactiveContent) {
            this.wxactiveContent = wxactiveContent;
        }
    }
}
