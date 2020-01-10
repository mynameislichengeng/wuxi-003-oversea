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

    public static String printStringSettlementPayFor(TransactionInfo transactionInfo) {

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


    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlementRefund(RefundDetailResp resp) {

        String tranCurrency = resp.getTransCurrency();

        String tempCurrency = resp.getSettlementCurrency();
        ;
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
        String s = multipleSpaces(28 - tempAmount.length()) + tempCurrency + " " + tempAmount;
        return s;
    }

    public static HTMLPrintModel.LeftAndRightLine printHtmlActivity(DailyDetailResp resp) {

        String settlementCurrency = resp.getSettlementCurrency();
        String settlementAmount = divide100(resp.getSettlementAmount());
        settlementAmount = removeFuhao(settlementAmount);
        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", settlementCurrency + " " + settlementAmount);
        return leftAndRightLine;
    }

    public static String printStringActivity(DailyDetailResp resp) {
        String settlementCurrency = resp.getSettlementCurrency();
        String settlementAmount = divide100(resp.getSettlementAmount());
        settlementAmount = removeFuhao(settlementAmount);
        String str = multipleSpaces(28 - settlementAmount.length()) + settlementCurrency + " " + settlementAmount;
        return str;
    }
}
