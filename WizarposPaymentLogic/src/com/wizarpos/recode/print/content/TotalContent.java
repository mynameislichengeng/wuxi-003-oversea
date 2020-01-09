package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class TotalContent extends PrintBase {

    public static HTMLPrintModel.LeftAndRightLine printHtml(Context context, String totalAmount) {
        String total = context.getResources().getString(R.string.print_total);
        return new HTMLPrintModel.LeftAndRightLine(total, "$" + totalAmount);
    }

    public static String printString(Context context, String totalAmount) {
        String total = context.getResources().getString(R.string.print_total);
        return total + multipleSpaces(25 - totalAmount.length()) + "$" + totalAmount;
    }
}
