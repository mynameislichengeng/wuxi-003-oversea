package com.wizarpos.pay.recode.zusao.sp;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.recode.zusao.constants.ZsSettingEnum;
import com.wizarpos.recode.print.constants.SettingPrintModeEnum;

public class ZSSettingManager {
    private final static String KEY_CODE_PAY_TYPE = "zs_pay_type";

    private final static String KEY_CODE_CONNECT_REQUEST = "zs_connect_request";

    public static void clearScanCodePayType() {
        AppConfigHelper.setConfig(KEY_CODE_PAY_TYPE, "");
    }
    public static String getScanCodePayType() {
        return AppConfigHelper.getConfig(KEY_CODE_PAY_TYPE, "");
    }
    public static void setScanCodePayType(ZsSettingEnum zsSettingEnum) {
        AppConfigHelper.setConfig(KEY_CODE_PAY_TYPE, zsSettingEnum.getType());
    }

    public static void clearConnectJson() {
        setConnectJson("");
    }
    public static String getConnectJson() {
        return AppConfigHelper.getConfig(KEY_CODE_PAY_TYPE, "");
    }
    public static void setConnectJson(String json) {
        AppConfigHelper.setConfig(KEY_CODE_CONNECT_REQUEST, json);
    }

}
