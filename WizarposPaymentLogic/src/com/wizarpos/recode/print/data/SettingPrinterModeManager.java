package com.wizarpos.recode.print.data;

import android.text.TextUtils;

import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.recode.print.constants.SettingPrintModeEnum;

import java.util.List;

public class SettingPrinterModeManager {

    private static String DEFAULT_MODE = SettingPrintModeEnum.MODE_0.getMode();

    /**
     * 在ui中显示
     *
     * @return
     */
    public static String getLayoutShowPrintMode() {
        String cache = getCachePrintMode();
        SettingPrintModeEnum enumMode = SettingPrintModeEnum.getEnumFromMode(cache);
        return enumMode.getShow();
    }


    public static String[][] getLayoutAdapterShowPrintMode() {
        String[][] arrays = new String[3][];
        arrays[0] = new String[2];
        arrays[0][0] = SettingPrintModeEnum.MODE_0.getMode();
        arrays[0][1] = SettingPrintModeEnum.MODE_0.getShow();
        arrays[1] = new String[2];
        arrays[1][0] = SettingPrintModeEnum.MODE_1.getMode();
        arrays[1][1] = SettingPrintModeEnum.MODE_1.getShow();
        arrays[2] = new String[2];
        arrays[2][0] = SettingPrintModeEnum.MODE_2.getMode();
        arrays[2][1] = SettingPrintModeEnum.MODE_2.getShow();
        return arrays;
    }


    /**
     * 获得缓存
     *
     * @return
     */
    public static String getCachePrintMode() {
        return AppConfigHelper.getConfig(AppConfigDef.print_number, DEFAULT_MODE);
    }

    public static void settingCachePrintModeFromShowValue(String show) {
        SettingPrintModeEnum modeEnum = SettingPrintModeEnum.getEnumFromShow(show);
        AppConfigHelper.setConfig(AppConfigDef.print_number, modeEnum.getMode());
    }
}
