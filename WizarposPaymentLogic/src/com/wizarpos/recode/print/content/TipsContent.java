package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class TipsContent extends PrintBase {


    public static HTMLPrintModel.LeftAndRightLine printHtml(Context context, String tipsAmount) {
        String printTip = context.getString(R.string.print_tip);
        return new HTMLPrintModel.LeftAndRightLine(printTip, "$" + Calculater.formotFen(tipsAmount));
    }

    public static String printString(Context context, String tipsAmount, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        String printTip = context.getString(R.string.print_tip);
        return printTip + multipleSpaces(31 - printTip.getBytes("GBK").length - Calculater.formotFen(transactionInfo.getRealAmount()).length()) + "$" + Calculater.formotFen(tipsAmount);

    }
}
