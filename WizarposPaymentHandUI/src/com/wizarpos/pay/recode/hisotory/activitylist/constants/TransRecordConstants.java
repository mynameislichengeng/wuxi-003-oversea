package com.wizarpos.pay.recode.hisotory.activitylist.constants;

import com.wizarpos.recode.history.constants.TransRecordLogicConstants;

public interface TransRecordConstants extends TransRecordLogicConstants {

    int PAGENO_DEFAULT = 0;//默认启始页
    int PAGE_SIZE = 20;//每1页数目

    int ALL_PAGE_SIZE = 50;// 这是用在打印发票的时候

    boolean IS_NEXT_REFRESH = true;

    //
    String UNRECHARGEON = "0";


    enum TransType {
        ALL("1"),//全部
        UNDO("2"),//撤销
        SALE("3");//消费
        private String type;

        TransType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    enum TimeRange {
        TODAY("0"),
        YESTERDAY("1"),
        THIS_WEEK("2"),
        LAST_WEEK("3"),
        THIS_MONTH("4"),
        LAST_MONTH("5"),
        CUSTOM_TIME("6"),
        ;
        private String type;

        TimeRange(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }




}
