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

    public static String printStrPurchasePayFor(Context context, String tipsAmount, String totalAmount, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        String pur = null;
        String printPurchase = context.getString(R.string.print_purchase);
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            String purchaseAmount = Calculater.formotFen(Calculater.subtract(transactionInfo.getRealAmount(), tipsAmount));
            pur = printPurchase + multipleSpaces(getPurchaseSpaceCount() - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount;
        } else {
            pur = printPurchase + multipleSpaces(getPurchaseSpaceCount() - printPurchase.getBytes("GBK").length - totalAmount.length()) + "$" + totalAmount;
        }
        return pur;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) throws UnsupportedEncodingException {
        //
        String printPurchase = context.getString(R.string.print_purchase);

        String transCurrency = resp.getTransCurrency();
        String transCurrencyPrint = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(transCurrency);

        String tranAmount = resp.getTransAmount();
        String tipsAmount = resp.getTipAmount();
        int numSpace = tranZhSpaceNums(getPurchaseSpaceCount(), 1, transCurrency);
        String purchaseAmount;
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            purchaseAmount = divide100(Calculater.subtract(tranAmount, tipsAmount));
        } else {
            purchaseAmount = divide100(tranAmount);
        }
        return printPurchase + multipleSpaces(numSpace - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + transCurrencyPrint + purchaseAmount;
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


    private static int getPurchaseSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 31 + 15;
        } else {
            return 31;
        }
    }

}
