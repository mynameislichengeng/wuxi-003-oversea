package com.wizarpos.recode.constants;

public interface HttpConstants {

    String FIELD_INVOICENO = "invoiceNo";//字段invoiceNo
    String FIELD_SN = "sn";
    String FIELD_OPTNAME = "optName";
    String FIELD_SETTLEMENTCURRENCY = "settlementCurrency";
    String FIELD_SETTLEMENTAMOUNT = "settlementAmount";
    String FIELD_TRANAMOUNT = "tranAmount";
    String FIELD_TERMINALVERSION = "terminalVersion";//版本号
    String FIELD_MERCHANTTRADECODE = "merchantTradeCode";//流水票据
    String FIELD_TRANSCURRENCY = "transCurrency";//交易货币
    String FIELD_DIFFCODE = "diffCode";



    /**
     * 登陆
     */
    enum API_100_PARAM {
        TERMINALVERSION(FIELD_TERMINALVERSION);
        private String key;

        public String getKey() {
            return key;
        }

        API_100_PARAM(String key) {
            this.key = key;
        }
    }

    /**
     * 支付
     */
    enum API_953_PARAM {
        INVOICENO(FIELD_INVOICENO),
        ;
        private String key;

        public String getKey() {
            return key;
        }

        API_953_PARAM(String key) {
            this.key = key;
        }
    }

    enum API_953_RESPONSE {
        SN(FIELD_SN),
        OPTNAME(FIELD_OPTNAME),
        SETTLEMENTCURRENCY(FIELD_SETTLEMENTCURRENCY),
        SETTLEMENTAMOUNT(FIELD_SETTLEMENTAMOUNT),
        MERCHANTTRADECODE(FIELD_MERCHANTTRADECODE),
        TRANSCURRENCY(FIELD_TRANSCURRENCY),
        ;
        private String key;

        public String getKey() {
            return key;
        }

        API_953_RESPONSE(String key) {
            this.key = key;
        }
    }


    /**
     * refund
     */
    enum API_955_RESPONSE {
        SN(FIELD_SN),
        OPTNAME(FIELD_OPTNAME),
        SETTLEMENTCURRENCY(FIELD_SETTLEMENTCURRENCY),
        SETTLEMENTAMOUNT(FIELD_SETTLEMENTAMOUNT),
        TRANAMOUNT(FIELD_TRANAMOUNT),
        MERCHANTTRADECODE(FIELD_MERCHANTTRADECODE),
        TRANSCURRENCY(FIELD_TRANSCURRENCY),
        DIFF_CODE(FIELD_DIFFCODE),
        ;

        private String key;

        public String getKey() {
            return key;
        }

        API_955_RESPONSE(String key) {
            this.key = key;
        }
    }

    /**
     * activity历史记录查询
     */
    enum API_964_PARAM{
        INVOICENO(FIELD_INVOICENO),
        ;

        private String key;

        public String getKey() {
            return key;
        }

        API_964_PARAM(String key) {
            this.key = key;
        }
    }

    /**
     * activity历史记录查询
     */
    enum API_965_PARAM{
        INVOICENO(FIELD_INVOICENO),
        ;

        private String key;

        public String getKey() {
            return key;
        }

        API_965_PARAM(String key) {
            this.key = key;
        }
    }
}
