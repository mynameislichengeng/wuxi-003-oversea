package com.wizarpos.recode.print;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.Config_Common;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.wizarpospaymentlogic.R;

public class PrintContent {

    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlement(String totalAmount, String exchangeRate, TransactionInfo transactionInfo) {

        String tranCurrency = transactionInfo.getTransCurrency();
        String settlementCurrency = transactionInfo.getSettlementCurrency();
        String tempAmount;
        String tempCurrency;
        if (tranCurrency.equals(settlementCurrency)) {
            String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
            }
            tempAmount = cnyAmount;
            tempCurrency = "CNY";
        } else {
            String settlementAmount = Calculater.formotFen(transactionInfo.getSettlementAmount()).trim();
            if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
                settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
            }
            tempAmount = settlementAmount;
            tempCurrency = settlementCurrency;
        }

        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", tempCurrency + " " + tempAmount);
        return leftAndRightLine;
    }

    public static String printStringSettlement(String totalAmount, String exchangeRate, TransactionInfo transactionInfo) {

        String tranCurrency = transactionInfo.getTransCurrency();
        String settlementCurrency = transactionInfo.getSettlementCurrency();
        String tempAmount;
        String tempCurrency;
        if (tranCurrency.equals(settlementCurrency)) {
            String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
            }
            tempAmount = cnyAmount;
            tempCurrency = "CNY";
        } else {
            String settlementAmount = Calculater.formotFen(transactionInfo.getSettlementAmount()).trim();
            if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
                settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
            }
            tempAmount = settlementAmount;
            tempCurrency = settlementCurrency;
        }

        String str = multipleSpaces(28 - tempAmount.length()) + tempCurrency + " " + tempAmount;
        return str;
    }


    public static String printStringSettlement(String exchangeRate, DailyDetailResp resp) {

        String tranCurrency = resp.getTransCurrency();
        String settlementCurrency = resp.getSettlementCurrency();
        String tempAmount;
        String tempCurrency;
        if (tranCurrency.equals(settlementCurrency)) {

            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount()), exchangeRate)));
            }
            tempAmount = cnyAmount;
            tempCurrency = "CNY";
        } else {
            String settlementAmount = Calculater.formotFen(resp.getSettlementAmount()).trim();
            if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
                settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(resp.getSingleAmount(), exchangeRate)));
            }
            tempAmount = settlementAmount;
            tempCurrency = settlementCurrency;
        }

        String str = multipleSpaces(28 - tempAmount.length()) + tempCurrency + " " + tempAmount;
        return str;
    }

    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlement(String exchangeRate, DailyDetailResp resp) {

        String tranCurrency = resp.getTransCurrency();
        String settlementCurrency = resp.getSettlementCurrency();
        String tempAmount;
        String tempCurrency;
        if (tranCurrency.equals(settlementCurrency)) {

            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount()), exchangeRate)));
            }
            tempAmount = cnyAmount;
            tempCurrency = "CNY";
        } else {
            String settlementAmount = Calculater.formotFen(resp.getSettlementAmount()).trim();
            if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
                settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(resp.getSingleAmount(), exchangeRate)));
            }
            tempAmount = settlementAmount;
            tempCurrency = settlementCurrency;
        }

        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", tempCurrency + " " + tempAmount);
        return leftAndRightLine;
    }

    public static HTMLPrintModel.LeftAndRightLine printHtmlSettlement(String exchangeRate, RefundDetailResp resp) {

        String tranCurrency = resp.getTransCurrency();
        String settlementCurrency = resp.getSettlementCurrency();
        String tempAmount;
        String tempCurrency;
        if (tranCurrency.equals(settlementCurrency)) {

            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getRefundAmount()), exchangeRate)));
            }
            tempAmount = cnyAmount;
            tempCurrency = "CNY";
        } else {
            String settlementAmount = Calculater.formotFen(resp.getSettlementAmount()).trim();
            if (TextUtils.isEmpty(settlementAmount) || "0.00".equals(settlementAmount)) {
                settlementAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(resp.getRefundAmount(), exchangeRate)));
            }
            tempAmount = settlementAmount;
            tempCurrency = settlementCurrency;
        }

        HTMLPrintModel.LeftAndRightLine leftAndRightLine = new HTMLPrintModel.LeftAndRightLine("", tempCurrency + " " + tempAmount);
        return leftAndRightLine;
    }






    public static HTMLPrintModel.SimpleLine printHtmlSn(Context context, String sn) {
        HTMLPrintModel.SimpleLine simpleLine = new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + sn);
        return simpleLine;
    }

    public static String printStringSn(Context context, String sn) {
        return context.getString(R.string.print_terminal_id) + sn;
    }


    public static HTMLPrintModel.SimpleLine printHtmlOptName(Context context, String optName) {
        if (TextUtils.isEmpty(optName)) {
            optName = "";
        }
        HTMLPrintModel.SimpleLine html = new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + optName);
        return html;
    }

    public static String printStringOptName(Context context, String optName) {
        if (TextUtils.isEmpty(optName)) {
            optName = "";
        }
        return context.getString(R.string.print_cashier_id) + optName;
    }

    private static String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }
}
