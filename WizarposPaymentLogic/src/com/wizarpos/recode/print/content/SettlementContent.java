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

    public static String printStringSettlementPayFor(TransactionInfo transactionInfo) {

        String amount = divide100(transactionInfo.getSettlementAmount());
        String str = multipleSpaces(getSettlementSpaceCount() - amount.length()) + transactionInfo.getSettlementCurrency() + " " + amount;
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
        String s = multipleSpaces(getSettlementSpaceCount() - tempAmount.length()) + tempCurrency + " " + tempAmount;
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
        String str = multipleSpaces(getSettlementSpaceCount() - settlementAmount.length()) + settlementCurrency + " " + settlementAmount;
        return str;
    }

    private static int getSettlementSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 28 + 15;
        } else {
            return 28;
        }
    }
}
