package com.wizarpos.pay.recode.zusao.bean.connect;

public class ZsConnectIntentBeanReq {


    /**
     * cmdType : Purchase
     * ReqPayload : {"BaseAmount":"1.23","UserDefinedEchoData":"testData","CardEntryMethod":"AUTO","AutoPrint":"true"}
     */

    private String cmdType;
    private ReqPayloadBean ReqPayload;

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public ReqPayloadBean getReqPayload() {
        return ReqPayload;
    }

    public void setReqPayload(ReqPayloadBean ReqPayload) {
        this.ReqPayload = ReqPayload;
    }

    public static class ReqPayloadBean {
        /**
         * BaseAmount : 1.23
         * UserDefinedEchoData : testData
         * CardEntryMethod : AUTO
         * AutoPrint : true
         */

        private String BaseAmount;
        private String UserDefinedEchoData;
        private String CardEntryMethod;
        private String AutoPrint;
        private String TipAmount;

        public String getBaseAmount() {
            return BaseAmount;
        }

        public void setBaseAmount(String BaseAmount) {
            this.BaseAmount = BaseAmount;
        }

        public String getUserDefinedEchoData() {
            return UserDefinedEchoData;
        }

        public void setUserDefinedEchoData(String UserDefinedEchoData) {
            this.UserDefinedEchoData = UserDefinedEchoData;
        }

        public String getCardEntryMethod() {
            return CardEntryMethod;
        }

        public void setCardEntryMethod(String CardEntryMethod) {
            this.CardEntryMethod = CardEntryMethod;
        }

        public String getAutoPrint() {
            return AutoPrint;
        }

        public void setAutoPrint(String AutoPrint) {
            this.AutoPrint = AutoPrint;
        }

        public String getTipAmount() {
            return TipAmount;
        }

        public void setTipAmount(String tipAmount) {
            TipAmount = tipAmount;
        }
    }
}
