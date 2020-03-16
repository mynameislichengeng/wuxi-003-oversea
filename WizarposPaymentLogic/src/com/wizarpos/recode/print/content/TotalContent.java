package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class TotalContent extends PrintBase {

    public static HTMLPrintModel.LeftAndRightLine printHtmlPayFor(Context context, String totalAmount) {
        String total = context.getResources().getString(R.string.print_total);
        return new HTMLPrintModel.LeftAndRightLine(total, "$" + totalAmount);
    }


    public static HTMLPrintModel.LeftAndRightLine printHtmlRefund(Context context, RefundDetailResp resp) {
        String printTotal = context.getString(R.string.print_total);
        String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
        String tranAmount = divide100(resp.getTranAmount());
        tranAmount = removeFuhao(tranAmount);
        return new HTMLPrintModel.LeftAndRightLine(printTotal, transCurrency + tranAmount);
    }


    public static HTMLPrintModel.LeftAndRightLine printHtmlActivity(Context context, DailyDetailResp resp) {
        String printTotal = context.getString(R.string.print_total);
        String tranCurrency = resp.getTransCurrency();
        String printTranCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(tranCurrency);
        String tranAmount = divide100(resp.getTransAmount());
        tranAmount = removeFuhao(tranAmount);
        return new HTMLPrintModel.LeftAndRightLine(printTotal, printTranCurrency + tranAmount);
    }


    public static String printStringPayFor(Context context, String totalAmount) {
//        String total = context.getResources().getString(R.string.print_total);
//        return total + multipleSpaces(getTotalSpaceCount() - totalAmount.length()) + "$" + totalAmount;

        return printStringBase(context, totalAmount, TransRecordLogicConstants.TRANSCURRENCY.CAD.getType());
    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
//        String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
//        int numTotalSpace = tranZhSpaceNums(getTotalSpaceCount(), 1, resp.getTransCurrency());
//        String printTotal = context.getString(R.string.print_total);
        String tranAmount = divide100(resp.getTranAmount());
        tranAmount = removeFuhao(tranAmount);
//        return printTotal + multipleSpaces(numTotalSpace - tranAmount.length()) + transCurrency + tranAmount;

        return printStringBase(context, tranAmount, resp.getTransCurrency());
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
//        String printTotal = context.getString(R.string.print_total);
//        String tranCurrency = resp.getTransCurrency();
//        String printTranCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(tranCurrency);
        String tranAmount = divide100(resp.getTransAmount());
        tranAmount = removeFuhao(tranAmount);

//        int numSpaceTotal = tranZhSpaceNums(getTotalSpaceCount(), 1, tranCurrency);
//        return printTotal + multipleSpaces(numSpaceTotal - tranAmount.length()) + printTranCurrency + tranAmount;

        return printStringBase(context, tranAmount, resp.getTransCurrency());

    }

    private static String printStringBase(Context context, String totalAmount, String tranCurrency) {
        StringBuffer sb = new StringBuffer();
        String printTotal = context.getString(R.string.print_total);
        sb.append(printTotal);
        //
        int numSpaceTotal = tranZhSpaceNums(getTotalSpaceCount(totalAmount), 1, tranCurrency);
        String space = multipleSpaces(numSpaceTotal - totalAmount.length());
        sb.append(space);
        //
        String printTranCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(tranCurrency);
        sb.append(printTranCurrency);
        //
        sb.append(totalAmount);
        return sb.toString();
    }


    private static int getTotalSpaceCount(String content) {
        int length = content.length();
        if (getDeviceTypeForN3N5()) {
            int countTr = 0;
            switch (length) {
                case 1:
                    countTr = 0;
                    break;
                case 2:
                    countTr = 0;
                    break;
                case 3:
                    countTr = 0;
                    break;
                case 4:
                    countTr = 0;
                    break;
                case 5:
                    countTr = -1;
                    break;
                case 6:
                    countTr = -2;
                    break;
                case 7:
                    countTr = -3;
                    break;
                case 8:
                    countTr = -5;
                    break;
                case 9:
                    countTr = -6;
                    break;
                case 10:
                    countTr = -7;
                    break;
                case 11:
                    countTr = -9;
                    break;
                case 12:
                    countTr = -10;
                    break;
                case 13:
                    countTr = -11;
                    break;
                case 14:
                    countTr = -13;
                    break;
                case 15:
                    countTr = -14;
                    break;
                case 16:
                    countTr = -15;
                    break;
                case 17:
                    countTr = -17;
                    break;
            }

            return 46 + countTr;
        } else {
            return 25;
        }
    }

}
