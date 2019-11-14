package com.wizarpos.pay.common;

import android.text.TextUtils;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * Created by wu on 16/1/20.
 */
public class MerchantLogoHelper {

    public static String getMerchantLogoUrl(){
        String imgUrl = AppConfigHelper.getConfig(AppConfigDef.agentHandLogo);
        if(TextUtils.isEmpty(imgUrl)){
            imgUrl = AppConfigHelper.getConfig(AppConfigDef.merchantHandLogo);
        }
        return imgUrl;
    }
}
