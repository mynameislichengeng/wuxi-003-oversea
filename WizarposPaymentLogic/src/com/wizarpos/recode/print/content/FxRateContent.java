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
        int length = content.length();
        if (getDeviceTypeForN3N5()) {
            int countTr = 0;
            switch (length) {
                case 1:
                    countTr = 0;
                    break;
                case 2:
                    countTr = -2;
                    break;
                case 3:
                    countTr = -3;
                    break;
                case 4:
                    countTr = -4;
                    break;
                case 5:
                    countTr = -6;
                    break;
                case 6:
                    countTr = -7;
                    break;
                case 7:
                    countTr = -8;
                    break;
                case 8:
                    countTr = -9;
                    break;
                case 9:
                    countTr = -11;
                    break;
                case 10:
                    countTr = -12;
                    break;
                case 11:
                    countTr = -14;
                    break;
                case 12:
                    countTr = -15;
                    break;
                case 13:
                    countTr = -16;
                    break;
            }
            return 47 + countTr;
        } else {
            return 32;
        }
    }
}
