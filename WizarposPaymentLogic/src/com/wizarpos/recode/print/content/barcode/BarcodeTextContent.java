package com.wizarpos.recode.print.content.barcode;

import android.content.Context;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.data.TranLogIdDataUtil;
import com.wizarpos.wizarpospaymentlogic.R;

import java.util.List;

public class BarcodeTextContent {


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
        String transLogId = getTranLogId(transactionInfo);
        return transLogId;

    }


    private static String getTranLogId(TransactionInfo transactionInfo) {

        return TranLogIdDataUtil.removeCharPForTranLogId(transactionInfo.getTranLogId());
    }
}
