package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class FxRateContent extends PrintBase {


    public static String printStringPayfor(Context context) {

        String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
        String result = printStringBase(context, exchangeRate);
        return result;
    }


    public static String printStringActivity(Context context, DailyDetailResp resp) {

        String exchangeRate = resp.getExchangeRate();

        String result = printStringBase(context, exchangeRate);
        return result;
    }


    public static String printStringBase(Context context, String exchangeRate) {
        String result = "";
        if (TextUtils.isEmpty(exchangeRate)) {
            exchangeRate = "1";
        }

        String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
        String printFx = context.getString(R.string.print_fx_rate);
        result += printFx;
        result += formatForBr();
        result += multipleSpaces(getCADSpaceCount() - showCNY.length()) + showCNY + formatForBr();
        result += formatForBr() + formatForNBr();
        return result;
    }


    private static int getCADSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 32 + 3;
        } else {
            return 32;
        }
    }
}
