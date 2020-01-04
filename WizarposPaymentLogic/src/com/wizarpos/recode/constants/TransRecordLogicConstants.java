package com.wizarpos.recode.constants;

public interface TransRecordLogicConstants {


    //货币结算类型
    enum TRANSCURRENCY {
        CAD("CAD", "$"),
        USD("USD", "$"),
        CNY("CNY", "¥");
        private String type;
        private String symbol;

        TRANSCURRENCY(String type, String symbol) {
            this.type = type;
            this.symbol = symbol;
        }

        public String getType() {
            return type;
        }


        private String getSymbol() {
            return symbol;
        }


        public static String getSymbol(String type) {
            TRANSCURRENCY[] imageFormatTypes = values();
            for (TRANSCURRENCY imageFormatType : imageFormatTypes) {
                if (imageFormatType.getType().equals(type)) {
                    return imageFormatType.getSymbol();
                }
            }
            return CAD.getSymbol();
        }


    }


}
