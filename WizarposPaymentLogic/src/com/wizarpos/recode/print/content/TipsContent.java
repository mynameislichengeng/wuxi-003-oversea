package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class TipsContent extends PrintBase {


    public static HTMLPrintModel.LeftAndRightLine printHtmlPayFor(Context context, String tipsAmount) {
        String printTip = context.getString(R.string.print_tip);
        return new HTMLPrintModel.LeftAndRightLine(printTip, "$" + Calculater.formotFen(tipsAmount));
    }


    public static HTMLPrintModel.LeftAndRightLine printHtmlActivity(Context context, DailyDetailResp resp) {
        if (!TextUtils.isEmpty(resp.getTipAmount()) && !resp.getTipAmount().equals("0")) {
            String printTip = context.getString(R.string.print_tip);
            String tips = divide100(resp.getTipAmount());
            String printTrancurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
            return new HTMLPrintModel.LeftAndRightLine(printTip, printTrancurrency + tips);
        }
        return null;
    }


    public static String printStringPayFor(Context context, String tipsAmount, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        return printStringBase(context, tipsAmount, TransRecordLogicConstants.TRANSCURRENCY.CAD.getType());
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) throws UnsupportedEncodingException {
        if (!TextUtils.isEmpty(resp.getTipAmount()) && !resp.getTipAmount().equals("0")) {

            return printStringBase(context, resp.getTipAmount(), resp.getTransCurrency());
        }
        return null;
    }


    private static String printStringBase(Context context, String tipAmount, String transCurrency) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String printTip = context.getString(R.string.print_tip);

        String tips = divide100(tipAmount);
        String printTrancurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(transCurrency);

        if (isComputerSpaceForLeftRight()) {
            String right = printTrancurrency + tips;
            sb.append(createTextLineForLeftAndRight(printTip, right));
        } else {
            sb.append(printTip);
            int numSpace = tranZhSpaceNums(getTipsSpaceCount(tips), 1, transCurrency);
            String space = multipleSpaces(numSpace - printTip.getBytes("GBK").length - tips.length());
            sb.append(space);
            //

            sb.append(printTrancurrency);
            //
            sb.append(tips);
            sb.append(formatForBr());
        }

        return sb.toString();

    }


    private static int getTipsSpaceCount(String value) {

        return 31;
    }
}
