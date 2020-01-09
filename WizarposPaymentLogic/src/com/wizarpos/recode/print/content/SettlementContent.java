package com.wizarpos.recode.print.content;

import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;

public class SettlementContent extends PrintBase {

    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlement(TransactionInfo transactionInfo) {

//        String tranCurrency = transactionInfo.getTransCurrency();
//        String settlementCurrency = transactionInfo.getSettlementCurrency();
//        String tempAmount;
//        String tempCurrency;
//        if (tranCurrency.equals(settlementCurrency)) {
//            String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
//            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
//                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
//            }
//            tempAmount = cnyAmount;
//            tempCurrency = "CNY";
//        } else {
//        String settlementAmount = Calculater.formotFen(transactionInfo.getSettlementAmount()).trim();
//        if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
//            settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
//        }
//        tempAmount = settlementAmount;
//        tempCurrency = settlementCurrency;
//        }
        String amount = divide100(transactionInfo.getSettlementAmount());
        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", transactionInfo.getSettlementCurrency() + " " + amount);
        return leftAndRightLine;
    }

    public static String printStringSettlement(TransactionInfo transactionInfo) {

//        String tranCurrency = transactionInfo.getTransCurrency();
//        String settlementCurrency = transactionInfo.getSettlementCurrency();
//        String tempAmount;
//        String tempCurrency;
//        if (tranCurrency.equals(settlementCurrency)) {
//            String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
//            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
//                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
//            }
//            tempAmount = cnyAmount;
//            tempCurrency = "CNY";
//        } else {
//            String settlementAmount = Calculater.formotFen(transactionInfo.getSettlementAmount()).trim();
//            if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
//                settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
//            }
//            tempAmount = settlementAmount;
//            tempCurrency = settlementCurrency;
//        }
        String amount = divide100(transactionInfo.getSettlementAmount());
        String str = multipleSpaces(28 - amount.length()) + transactionInfo.getSettlementCurrency() + " " + amount;
        return str;
    }
}
