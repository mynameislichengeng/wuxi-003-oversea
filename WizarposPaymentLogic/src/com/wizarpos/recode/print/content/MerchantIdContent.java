package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class MerchantIdContent extends PrintBase {

    public static String printStringPayfor(Context context) {
        return printBase(context);
    }

    public static String printStringActivity(Context context) {
        return printBase(context);
    }

    public static String printStringRefund(Context context) {
        return printBase(context);
    }

    private static String printBase(Context context) {
        String title = context.getString(R.string.print_merchant_id);
        String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
        String result;
        if (isComputerSpaceForLeftRight()) {
            result = createTextLineForLeft(title, merchantId);
        } else {
            result = title + merchantId;
            result += formatForBr();
        }
        return result;
    }
}
