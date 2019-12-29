package com.wizarpos.recode.payprocess.entity.http;

import com.wizarpos.recode.entity.base.ResponseBaseHttpParam;

public class ResponsePayProcessResultParam extends ResponseBaseHttpParam {


    /**
     * result : {"err_code":999,"orderDef":{"amount":36159,"cardNo":"","createTime":1577358136900,"disCount":0,"districtZone":"EST","exchangeRate":5.3305,"extraAmount":0,"fid":"100100020000013","id":"P100100020000013201912270013","inputAmount":36159,"lastTime":1577358136900,"merchantDef":{"adminMobileNo":"","contacts":"Ye Wang","createTime":1491171781000,"isOpenWxactive":"0","isVirtualMainStore":"0","lastTime":1547493368327,"marketActivityFlag":"1","marketModel":"","merchantAddr":" 5365 Yonge St. North York, ON, M2N 5R6","merchantName":"L'amour Beauty Trading Ltd North York","merchantShortName":"L'amour Beauty","merchantTel":"6479368986","merchantType":"1","mid":"100100020000013","negaStockFlag":"1","payId":0,"sessionFlag":"T","usePayRouteConfig":"99","validFlag":"0","vcode":"10704678","wxactiveContent":""},"mixedFlag":"0","mnFlag":"micro","orderNo":"02100100020000013201912270009","paySource":"1","payTime":"2019-12-26 19:02:16","payType":"W","sn":"WP18331Q20200568","state":1,"subTranCode":"814","timeZone":"Canada/Eastern","tipAmount":0,"tranCode":"814","transCurrency":"CAD"}}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * err_code : 999
         * orderDef : {"amount":36159,"cardNo":"","createTime":1577358136900,"disCount":0,"districtZone":"EST","exchangeRate":5.3305,"extraAmount":0,"fid":"100100020000013","id":"P100100020000013201912270013","inputAmount":36159,"lastTime":1577358136900,"merchantDef":{"adminMobileNo":"","contacts":"Ye Wang","createTime":1491171781000,"isOpenWxactive":"0","isVirtualMainStore":"0","lastTime":1547493368327,"marketActivityFlag":"1","marketModel":"","merchantAddr":" 5365 Yonge St. North York, ON, M2N 5R6","merchantName":"L'amour Beauty Trading Ltd North York","merchantShortName":"L'amour Beauty","merchantTel":"6479368986","merchantType":"1","mid":"100100020000013","negaStockFlag":"1","payId":0,"sessionFlag":"T","usePayRouteConfig":"99","validFlag":"0","vcode":"10704678","wxactiveContent":""},"mixedFlag":"0","mnFlag":"micro","orderNo":"02100100020000013201912270009","paySource":"1","payTime":"2019-12-26 19:02:16","payType":"W","sn":"WP18331Q20200568","state":1,"subTranCode":"814","timeZone":"Canada/Eastern","tipAmount":0,"tranCode":"814","transCurrency":"CAD"}
         */

        private int err_code;
        private OrderDefBean orderDef;

        public int getErr_code() {
            return err_code;
        }

        public void setErr_code(int err_code) {
            this.err_code = err_code;
        }

        public OrderDefBean getOrderDef() {
            return orderDef;
        }

        public void setOrderDef(OrderDefBean orderDef) {
            this.orderDef = orderDef;
        }

        public static class OrderDefBean {
            /**
             * amount : 36159
             * cardNo :
             * createTime : 1577358136900
             * disCount : 0
             * districtZone : EST
             * exchangeRate : 5.3305
             * extraAmount : 0
             * fid : 100100020000013
             * id : P100100020000013201912270013
             * inputAmount : 36159
             * lastTime : 1577358136900
             * merchantDef : {"adminMobileNo":"","contacts":"Ye Wang","createTime":1491171781000,"isOpenWxactive":"0","isVirtualMainStore":"0","lastTime":1547493368327,"marketActivityFlag":"1","marketModel":"","merchantAddr":" 5365 Yonge St. North York, ON, M2N 5R6","merchantName":"L'amour Beauty Trading Ltd North York","merchantShortName":"L'amour Beauty","merchantTel":"6479368986","merchantType":"1","mid":"100100020000013","negaStockFlag":"1","payId":0,"sessionFlag":"T","usePayRouteConfig":"99","validFlag":"0","vcode":"10704678","wxactiveContent":""}
             * mixedFlag : 0
             * mnFlag : micro
             * orderNo : 02100100020000013201912270009
             * paySource : 1
             * payTime : 2019-12-26 19:02:16
             * payType : W
             * sn : WP18331Q20200568
             * state : 1
             * subTranCode : 814
             * timeZone : Canada/Eastern
             * tipAmount : 0
             * tranCode : 814
             * transCurrency : CAD
             */

            private int amount;
            private String cardNo;
            private long createTime;
            private int disCount;
            private String districtZone;
            private double exchangeRate;
            private int extraAmount;
            private String fid;
            private String id;
            private int inputAmount;
            private long lastTime;
            private MerchantDefBean merchantDef;
            private String mixedFlag;
            private String mnFlag;
            private String orderNo;
            private String paySource;
            private String payTime;
            private String payType;
            private String sn;
            private int state;
            private String subTranCode;
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
                 * adminMobileNo :
                 * contacts : Ye Wang
                 * createTime : 1491171781000
                 * isOpenWxactive : 0
                 * isVirtualMainStore : 0
                 * lastTime : 1547493368327
                 * marketActivityFlag : 1
                 * marketModel :
                 * merchantAddr :  5365 Yonge St. North York, ON, M2N 5R6
                 * merchantName : L'amour Beauty Trading Ltd North York
                 * merchantShortName : L'amour Beauty
                 * merchantTel : 6479368986
                 * merchantType : 1
                 * mid : 100100020000013
                 * negaStockFlag : 1
                 * payId : 0
                 * sessionFlag : T
                 * usePayRouteConfig : 99
                 * validFlag : 0
                 * vcode : 10704678
                 * wxactiveContent :
                 */

                private String adminMobileNo;
                private String contacts;
                private long createTime;
                private String isOpenWxactive;
                private String isVirtualMainStore;
                private long lastTime;
                private String marketActivityFlag;
                private String marketModel;
                private String merchantAddr;
                private String merchantName;
                private String merchantShortName;
                private String merchantTel;
                private String merchantType;
                private String mid;
                private String negaStockFlag;
                private int payId;
                private String sessionFlag;
                private String usePayRouteConfig;
                private String validFlag;
                private String vcode;
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

                public String getMarketActivityFlag() {
                    return marketActivityFlag;
                }

                public void setMarketActivityFlag(String marketActivityFlag) {
                    this.marketActivityFlag = marketActivityFlag;
                }

                public String getMarketModel() {
                    return marketModel;
                }

                public void setMarketModel(String marketModel) {
                    this.marketModel = marketModel;
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

                public String getSessionFlag() {
                    return sessionFlag;
                }

                public void setSessionFlag(String sessionFlag) {
                    this.sessionFlag = sessionFlag;
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

                public String getWxactiveContent() {
                    return wxactiveContent;
                }

                public void setWxactiveContent(String wxactiveContent) {
                    this.wxactiveContent = wxactiveContent;
                }
            }
        }
    }
}
