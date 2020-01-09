package com.wizarpos.recode.constants;

public interface HttpConstants {

    String FIELD_INVOICENO = "invoiceNo";//字段invoiceNo
    String FIELD_SN = "sn";
    String FIELD_OPTNAME = "optName";
    String FIELD_SETTLEMENTCURRENCY = "settlementCurrency";
    String FIELD_SETTLEMENTAMOUNT = "settlementAmount";


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
        ;

        private String key;

        public String getKey() {
            return key;
        }

        API_955_RESPONSE(String key) {
            this.key = key;
        }
    }
}
