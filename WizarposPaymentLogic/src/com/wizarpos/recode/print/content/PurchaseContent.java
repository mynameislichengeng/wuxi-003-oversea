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

public class PurchaseContent extends PrintBase {


    public static HTMLPrintModel.LeftAndRightLine printHtmlPurchasePayFor(Context context, String tipsAmount, String totalAmount) {
        HTMLPrintModel.LeftAndRightLine purchase = null;
        String printPurchase = context.getString(R.string.print_purchase);
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            String purchaseAmount = Calculater.formotFen(Calculater.subtract(totalAmount, tipsAmount));
            purchase = new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + purchaseAmount);
        } else {
            purchase = new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + totalAmount);
        }
        return purchase;
    }

    public static HTMLPrintModel.LeftAndRightLine printHtmlActivity(Context context, DailyDetailResp resp) {
        //
        try {
            String printPurchase = context.getString(R.string.print_purchase);

            String transCurrency = resp.getTransCurrency();
            String transCurrencyPrint = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(transCurrency);

            String tranAmount = resp.getTransAmount();
            String tipsAmount = resp.getTipAmount();
            String purchaseAmount;
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                purchaseAmount = divide100(Calculater.subtract(tranAmount, tipsAmount));
            } else {
                purchaseAmount = divide100(tranAmount);
            }
            HTMLPrintModel.LeftAndRightLine printLine = new HTMLPrintModel.LeftAndRightLine(printPurchase, transCurrencyPrint + purchaseAmount);
            return printLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printStrPurchasePayFor(Context context, String tipsAmount, String totalAmount, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        String pur = null;
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            String purchaseAmount = Calculater.formotFen(Calculater.subtract(transactionInfo.getRealAmount(), tipsAmount));
            pur = printStringBase(context, purchaseAmount, "$", getPurchaseSpaceCount(purchaseAmount));
//            pur = printPurchase + multipleSpaces( - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount;
        } else {
//            pur = printPurchase + multipleSpaces(getPurchaseSpaceCount() - printPurchase.getBytes("GBK").length - totalAmount.length()) + "$" + totalAmount;
            pur = printStringBase(context, totalAmount, "$", getPurchaseSpaceCount(totalAmount));
        }
        return pur;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) throws UnsupportedEncodingException {
        //

        String transCurrency = resp.getTransCurrency();
        String transCurrencyPrint = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(transCurrency);

        String tranAmount = resp.getTransAmount();
        String tipsAmount = resp.getTipAmount();

        String purchaseAmount;
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            purchaseAmount = divide100(Calculater.subtract(tranAmount, tipsAmount));
        } else {
            purchaseAmount = divide100(tranAmount);
        }
        int numSpace = tranZhSpaceNums(getPurchaseSpaceCount(purchaseAmount), 1, transCurrency);
        return printStringBase(context, purchaseAmount, transCurrencyPrint, numSpace);
//        return printPurchase + multipleSpaces(numSpace - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + transCurrencyPrint + purchaseAmount;
    }

    private static String printStringBase(Context context, String value, String transCurrencyPrint, int numSpace) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String printPurchase = context.getString(R.string.print_purchase);

        if (isComputerSpaceForLeftRight()) {
            String temp = createTextLineForLeftAndRight(printPurchase, transCurrencyPrint + value);
            sb.append(temp);
        } else {
            //
            sb.append(printPurchase);
            //
            String space = multipleSpaces(numSpace - printPurchase.getBytes("GBK").length - value.length());
            sb.append(space);
            //
            sb.append(transCurrencyPrint);
            //
            sb.append(value);
        }
        return sb.toString();
    }

    private static int getPurchaseSpaceCount(String content) {
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
            return 47 + countTr + getCOUNTSPACE();
        } else {
            return 31;
        }
    }

}
