package com.wizarpos.recode.print.content;

import android.text.TextUtils;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.recode.print.base.PrintBase;

public class MerchantAddrContent extends PrintBase {


    public static String getPrintContent() {
        String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
        StringBuffer sb = new StringBuffer();
        try {
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    sb.append(formatForC(address));
                } else if (address.getBytes("GBK").length <= 64) {
                    sb.append(formatForC(address.substring(0, 32)));
                    sb.append(formatForC(address.substring(32)));
                } else if (address.getBytes("GBK").length <= 96) {
                    sb.append(formatForC(address.substring(0, 32)));
                    sb.append(formatForC(address.substring(32, 64)));
                    sb.append(formatForC(address.substring(64)));

                }
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }
}
