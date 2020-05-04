package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.print.base.PrintBase;

public class SettlementContent extends PrintBase {

    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlementPayFor(TransactionInfo transactionInfo) {

        String amount = divide100(transactionInfo.getSettlementAmount());
        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", transactionInfo.getSettlementCurrency() + " " + amount);
        return leftAndRightLine;
    }


    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlementRefund(RefundDetailResp resp) {

        String tranCurrency = resp.getTransCurrency();

        String tempCurrency = resp.getSettlementCurrency();

        String tempAmount;
        if (TransRecordLogicConstants.TRANSCURRENCY.CNY.getType().equals(tranCurrency)) {
            tempAmount = divide100(resp.getSettlementAmount()).trim();
        } else {
            tempAmount = divide100(resp.getRefundAmount()).trim();
        }
        tempAmount = removeFuhao(tempAmount);
        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", tempCurrency + " " + tempAmount);
        return leftAndRightLine;
    }


    public static HTMLPrintModel.LeftAndRightLine printHtmlActivity(DailyDetailResp resp) {

        String settlementCurrency = resp.getSettlementCurrency();
        String settlementAmount = divide100(resp.getSettlementAmount());
        settlementAmount = removeFuhao(settlementAmount);
        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", settlementCurrency + " " + settlementAmount);
        return leftAndRightLine;
    }

    public static String printStringSettlementPayFor(TransactionInfo transactionInfo) {

        String amount = divide100(transactionInfo.getSettlementAmount());
        return printStringBase(transactionInfo.getSettlementCurrency(), amount);
    }


    public static String printStringSettlementRefund(RefundDetailResp resp) {

        String tranCurrency = resp.getTransCurrency();

        String tempCurrency = resp.getSettlementCurrency();
        String tempAmount;
        if (TransRecordLogicConstants.TRANSCURRENCY.CNY.getType().equals(tranCurrency)) {
            tempAmount = divide100(resp.getSettlementAmount()).trim();
        } else {
            tempAmount = divide100(resp.getRefundAmount()).trim();
        }
        tempAmount = removeFuhao(tempAmount);
//        String s = multipleSpaces(getSettlementSpaceCount() - tempAmount.length()) + tempCurrency + " " + tempAmount;
//        return s;

        return printStringBase(tempCurrency, tempAmount);
    }


    public static String printStringActivity(DailyDetailResp resp) {
        String settlementCurrency = resp.getSettlementCurrency();
        String settlementAmount = divide100(resp.getSettlementAmount());
        settlementAmount = removeFuhao(settlementAmount);
//        String str = multipleSpaces(getSettlementSpaceCount() - settlementAmount.length()) + settlementCurrency + " " + settlementAmount;
//        return str;

        return printStringBase(settlementCurrency, settlementAmount);
    }


    private static String printStringBase(String settlementCurrency, String settlementAmount) {
        StringBuffer sb = new StringBuffer();

        if (isComputerSpaceForLeftRight()) {
            sb.append(formartForRight(settlementCurrency + " " + settlementAmount));
        } else {
            String space = multipleSpaces(getSettlementSpaceCount(settlementCurrency + settlementAmount) - settlementCurrency.length() - settlementAmount.length());
            sb.append(space);
            sb.append(settlementCurrency);
            sb.append(" ");
            sb.append(settlementAmount);
        }


        return sb.toString();
    }


    private static int getSettlementSpaceCount(String content) {
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
                    countTr = 0;
                    break;
                case 6:
                    countTr = 0;
                    break;
                case 7:
                    countTr = 0;
                    break;
                case 8:
                    countTr = -1;
                    break;
                case 9:
                    countTr = -2;
                    break;
                case 10:
                    countTr = -3;
                    break;
                case 11:
                    countTr = -5;
                    break;
                case 12:
                    countTr = -6;
                    break;
                case 13:
                    countTr = -7;
                    break;
                case 14:
                    countTr = -9;
                    break;
                case 15:
                    countTr = -10;
                    break;
                case 16:
                    countTr = -11;
                    break;
            }

            return 53 + countTr;
        } else {
            return 28;
        }
    }
}
