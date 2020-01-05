package com.wizarpos.recode.constants;

public interface HttpConstants {

    String FIELD_INVOICENO = "invoiceNo";//字段invoiceNo

    enum API_953_PARAM {
        INVOICENO(FIELD_INVOICENO);
        private String key;

        public String getKey() {
            return key;
        }

        API_953_PARAM(String key) {
            this.key = key;
        }
    }


}
