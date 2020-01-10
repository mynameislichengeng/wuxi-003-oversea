package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.print.PrintManager;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class TotalContent extends PrintBase {

    public static HTMLPrintModel.LeftAndRightLine printHtmlPayFor(Context context, String totalAmount) {
        String total = context.getResources().getString(R.string.print_total);
        return new HTMLPrintModel.LeftAndRightLine(total, "$" + totalAmount);
    }

    public static String printStringPayFor(Context context, String totalAmount) {
        String total = context.getResources().getString(R.string.print_total);
        return total + multipleSpaces(25 - totalAmount.length()) + "$" + totalAmount;
    }


    public static HTMLPrintModel.LeftAndRightLine printHtmlRefund(Context context, RefundDetailResp resp) {
        String printTotal = context.getString(R.string.print_total);
        String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
        String tranAmount = divide100(resp.getTranAmount());
        tranAmount = removeFuhao(tranAmount);
        return new HTMLPrintModel.LeftAndRightLine(printTotal, transCurrency + tranAmount);
    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
        int numTotalSpace = tranZhSpaceNums(25, 1, resp.getTransCurrency());
        String printTotal = context.getString(R.string.print_total);
        String tranAmount = divide100(resp.getTranAmount());
        tranAmount = removeFuhao(tranAmount);
        return printTotal + multipleSpaces(numTotalSpace - tranAmount.length()) + transCurrency + tranAmount;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String printTotal = context.getString(R.string.print_total);
        String tranCurrency = resp.getTransCurrency();
        String printTranCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(tranCurrency);
        String tranAmount = divide100(resp.getTrasnAmount());
        tranAmount = removeFuhao(tranAmount);

        int numSpaceTotal = tranZhSpaceNums(25, 1, tranCurrency);
        return printTotal + multipleSpaces(numSpaceTotal - tranAmount.length()) + printTranCurrency + tranAmount;

    }

    public static HTMLPrintModel.LeftAndRightLine printHtmlActivity(Context context, DailyDetailResp resp) {
        String printTotal = context.getString(R.string.print_total);
        String tranCurrency = resp.getTransCurrency();
        String printTranCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(tranCurrency);
        String tranAmount = divide100(resp.getTrasnAmount());
        tranAmount = removeFuhao(tranAmount);
        return new HTMLPrintModel.LeftAndRightLine(printTotal, printTranCurrency + tranAmount);
    }


}
