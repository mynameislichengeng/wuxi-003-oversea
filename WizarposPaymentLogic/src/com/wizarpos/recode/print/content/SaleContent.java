package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class SaleContent extends PrintBase {


    public static String printStringActivity(Context context) {
        return printStringBase(context);
    }

    public static String printStringPayFor(Context context) {
        return printStringBase(context);
    }

    private static String printStringBase(Context context) {
        String title = context.getString(R.string.print_sale);
        String result = "";
        result = formartForBold(title);
        result = formatForC(result);
        if (isComputerSpaceForLeftRight()) {

        } else {
            result += formatForBr();
        }
        return result;
    }
}
