package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class CADContent extends PrintBase {

    public static String printStringActivity(Context context, DailyDetailResp resp) throws UnsupportedEncodingException {
        String result = "";
        String exchangeRate = resp.getExchangeRate();
        if (TextUtils.isEmpty(exchangeRate)) {
            exchangeRate = "1";
        }
        String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
        String printFx = context.getString(R.string.print_fx_rate);
        result += printFx + multipleSpaces(getCADSpaceCount() - printFx.getBytes("GBK").length - showCNY.length()) + showCNY + formatForBr();
        result += formatForBr() + formatForNBr();
        return result;
    }


    private static int getCADSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 32 + 5;
        } else {
            return 32;
        }
    }
}
