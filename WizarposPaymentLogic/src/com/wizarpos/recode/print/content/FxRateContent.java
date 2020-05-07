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


    /**
     * FX rate:<br/>         CAD 1.00=CNY 5.05290000<br/><br/><nbr/>
     *
     * @param context
     * @param exchangeRate
     * @return
     */
    public static String printStringBase(Context context, String exchangeRate) {
        String result = "";

        String printFx = context.getString(R.string.print_fx_rate);

        if (TextUtils.isEmpty(exchangeRate)) {
            exchangeRate = "1";
        }
        String exchangeRateString = Calculater.multiply("1", exchangeRate);
        String showCNY = "CAD 1.00=CNY " + exchangeRateString;


        if (isComputerSpaceForLeftRight()) {
            StringBuffer sb = new StringBuffer();
            sb.append(createTextLineForLeftAndRight(printFx, showCNY));
            sb.append(formartForLineSpace());
            result = sb.toString();
        } else {
            result += printFx;
            result += formatForBr();
            result += multipleSpaces(getCADSpaceCount(exchangeRateString) - showCNY.length()) + showCNY + formatForBr();
            result += formatForBr() + formatForNBr();
        }


        return result;
    }


    private static int getCADSpaceCount(String content) {

        return 32;
    }
}
