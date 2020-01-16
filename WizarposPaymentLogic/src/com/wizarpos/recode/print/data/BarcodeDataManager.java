package com.wizarpos.recode.print.data;

import com.wizarpos.recode.print.constants.BarFormat;

public class BarcodeDataManager {

    private static BarFormat CURRENT_FORMAT = BarFormat.CODE128;

    public static void settingCurrentFormat(BarFormat bar) {
        CURRENT_FORMAT = bar;
    }

    public static BarFormat getCurrentFormat() {
        return CURRENT_FORMAT;
    }

}
