package com.wizarpos.pay.recode.constants;

public interface IntentConstants {

    enum TradLogActivity {
        REFUND_DIALOG_OBJECT("REFUND_OBJECT");
        private String key;

        TradLogActivity(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    enum TradDetailActivty {
        DETAIL_OBJ("detail_data");
        private String key;

        TradDetailActivty(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
