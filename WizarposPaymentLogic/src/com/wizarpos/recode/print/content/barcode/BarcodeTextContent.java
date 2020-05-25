package com.wizarpos.recode.print.content.barcode;

import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.data.TranLogIdDataUtil;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;

import java.util.List;

public class BarcodeTextContent extends PrintBase {


    public static String printStringPayfor(TransactionInfo transactionInfo) {
        if (transactionInfo == null || TextUtils.isEmpty(transactionInfo.getTranLogId())) {
            return null;
        }

        return printStringBase(transactionInfo.getTranLogId());
    }


    public static String printStringRefund(RefundDetailResp refundDetailResp) {
        if (refundDetailResp == null || TextUtils.isEmpty(refundDetailResp.getTranLogId())) {
            return null;
        }
        return printStringBase(refundDetailResp.getTranLogId());
    }

    public static String printStringActivity(DailyDetailResp resp) {
        if (resp == null || TextUtils.isEmpty(resp.getTranLogId())) {
            return null;
        }
        return printStringBase(resp.getTranLogId());
    }


    private static String printStringBase(String transactionInfo) {
        if (isOpenBarCodeStatus()) {

//            String transLogId = TranLogIdDataUtil.removeCharPForTranLogId(transactionInfo);
            String transLogId = transactionInfo;

            if (isComputerSpaceForLeftRight()) {
                StringBuffer sb = new StringBuffer();

                //条形码
                String s = formatForBC(transLogId);
                sb.append(s);

                return sb.toString();
            } else {
                String content = multipleSpaces(2) + transLogId;
                String barcode = formatForBC(content) + formatForBr();

                return barcode;
            }

        }

        return null;
    }





}
