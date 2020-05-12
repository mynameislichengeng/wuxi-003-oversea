package com.wizarpos.recode.print.service;

import com.wizarpos.recode.print.constants.HtmlRemarkConstans;
import com.wizarpos.recode.print.constants.PrintTypeEnum;

public abstract class PrintHandleService {

    public abstract void contentTrigger(String str);

    public abstract void keywordTrigger(String keyword);


    protected PrintTypeEnum printTypeEnum = PrintTypeEnum.TEXT;

    public void setPrintTypeEnum(PrintTypeEnum printTypeEnum) {
        this.printTypeEnum = printTypeEnum;
    }

    public boolean isLeftStartKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LEFT_START.getValue());
    }

    public boolean isLeftEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LEFT_END.getValue());
    }


    public boolean isCenterStartKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.CENTER_START.getValue());
    }

    public boolean isCenterEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.CENTER_END.getValue());
    }

    public boolean isRightStartKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.RIGHT_START.getValue());
    }

    public boolean isRightEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.RIGHT_END.getValue());
    }

    public boolean isLeftAndRightStartKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LEFT_RIGHT_START.getValue());
    }

    public boolean isLeftAndRightEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LEFT_RIGHT_END.getValue());
    }

    public boolean isBoldStartKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LEFT_RIGHT_START.getValue());
    }

    public boolean isBoldEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LEFT_RIGHT_END.getValue());
    }


    public boolean isBCStartKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.BARCODE_START.getValue());
    }

    public boolean isBCEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.BARCODE_END.getValue());
    }


    public boolean isLineKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LINE.getValue());
    }

    public boolean isNLineKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LINE_N.getValue());
    }

//    public boolean isNewLineKeyWords(String keyword) {
//        return keyword.equals(HtmlRemarkConstans.LINE_TAG_NEW.getValue());
//    }

    public boolean isNewLineSpaceKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.LINE_SPACE_NEW.getValue());
    }

    public boolean isEndKeyWords(String keyword) {
        return keyword.equals(HtmlRemarkConstans.END.getValue());
    }
}