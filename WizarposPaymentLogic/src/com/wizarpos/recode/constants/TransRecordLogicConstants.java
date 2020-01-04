package com.wizarpos.recode.constants;

public interface TransRecordLogicConstants {


    //货币结算类型
    enum TRANSCURRENCY {
        CAD("CAD", "$","$"),
        USD("USD", "$","$"),
        CNY("CNY", "¥","\uFFE5");
        private String type;
        private String symbol;
        private String printStr;

        TRANSCURRENCY(String type, String symbol,String printStr) {
            this.type = type;
            this.symbol = symbol;
            this.printStr = printStr;
        }

        public String getType() {
            return type;
        }


        private String getSymbol() {
            return symbol;
        }

        public String getPrintStr() {
            return printStr;
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

        public static String getPrintStr(String type) {
            TRANSCURRENCY[] imageFormatTypes = values();
            for (TRANSCURRENCY imageFormatType : imageFormatTypes) {
                if (imageFormatType.getType().equals(type)) {
                    return imageFormatType.getPrintStr();
                }
            }
            return CAD.getPrintStr();
        }
    }


}
