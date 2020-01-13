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

    public static String printStringPayFor(Context context, String tipsAmount, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        String printTip = context.getString(R.string.print_tip);
        return printTip + multipleSpaces(31 - printTip.getBytes("GBK").length - Calculater.formotFen(transactionInfo.getRealAmount()).length()) + "$" + Calculater.formotFen(tipsAmount);

    }

    public static String printStringActivity(Context context, DailyDetailResp resp) throws UnsupportedEncodingException {
        if (!TextUtils.isEmpty(resp.getTipAmount()) && !resp.getTipAmount().equals("0")) {
            String printTip = context.getString(R.string.print_tip);
            String tips = divide100(resp.getTipAmount());
            int numSpace = tranZhSpaceNums(31, 1, resp.getTransCurrency());
            String printTrancurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
            return printTip + multipleSpaces(numSpace - printTip.getBytes("GBK").length - tips.length()) + printTrancurrency + tips;
        }
        return null;
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
}
