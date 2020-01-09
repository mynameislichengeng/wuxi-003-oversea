package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class PurchaseContent extends PrintBase {

    public static HTMLPrintModel.LeftAndRightLine printHtmlPurchase(Context context, String tipsAmount, String totalAmount) {
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

    public static String printStrPurchase(Context context, String tipsAmount, String totalAmount, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        String pur = null;
        String printPurchase = context.getString(R.string.print_purchase);
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            String purchaseAmount = Calculater.formotFen(Calculater.subtract(transactionInfo.getRealAmount(), tipsAmount));
            pur = printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount;
        } else {
            pur = printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - totalAmount.length()) + "$" + totalAmount;
        }
        return pur;
    }
}
