package com.wizarpos.recode.print.content.barcode;

import android.content.Context;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.data.TranLogIdDataUtil;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;

import java.util.List;

public class BarcodeTextContent extends PrintBase {


    public static void printHtmlPayfor(List<HtmlLine> lines, TransactionInfo transactionInfo) {

        if (transactionInfo == null) {
            return;
        }
        String transLogId = getTranLogId(transactionInfo);
        lines.add(new HTMLPrintModel.SimpleLine(transLogId, true, true));
    }

    public static String printStringPayfor(TransactionInfo transactionInfo) {
        if (transactionInfo == null) {
            return null;
        }

        if (isOpenStatus()) {
            String transLogId = getTranLogId(transactionInfo);
            String barcode = formatForBC(transLogId);
            String result = formatForC(barcode);
            return result;
        }

        return null;
    }


    protected static boolean isOpenStatus() {
        return ReceiptDataManager.isOpenStatus();
    }

    private static String getTranLogId(TransactionInfo transactionInfo) {

        return TranLogIdDataUtil.removeCharPForTranLogId(transactionInfo.getTranLogId());
    }
}
