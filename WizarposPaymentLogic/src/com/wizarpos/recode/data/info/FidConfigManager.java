package com.wizarpos.recode.data.info;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class FidConfigManager {

    public static void setFid(String str) {
        AppConfigHelper.setConfig(AppConfigDef.fid, str);// 慧商户号
    }

    public static String getFid() {
        return AppConfigHelper.getConfig(AppConfigDef.fid);
    }
}
