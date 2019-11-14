package com.wizarpos.pay.common.utils;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wu on 15/11/25.
 */
public class TokenGenerater {

    public static String newToken() {
        return AppConfigHelper.getConfig(AppConfigDef.mid) + (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + getRandom();
    }

    /**
     * 产生1000到9999的随机数
     *
     * @return
     */
    private static long getRandom() {
        return Math.round(Math.random() * 8999 + 1000);
    }


}
