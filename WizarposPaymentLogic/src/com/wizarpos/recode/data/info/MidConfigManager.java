package com.wizarpos.recode.data.info;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class MidConfigManager {


    public static void setMid(String str) {
        AppConfigHelper.setConfig(AppConfigDef.mid, str);// 慧商户号
    }

    public static String getMid() {
        return AppConfigHelper.getConfig(AppConfigDef.mid);
    }
}
