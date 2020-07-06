package com.wizarpos.recode.print.content.barcode;

import android.text.TextUtils;

import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;

public class QrCodeTextContent extends PrintBase {


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
        if (isOpenQrCodeStatus()) {

            String transLogId = transactionInfo;

            if (isComputerSpaceForLeftRight()) {
                StringBuffer sb = new StringBuffer();
                //二维码
                String qrCode = formatForQC(transLogId);
                sb.append(qrCode);
                //如果没有打开二维码，那么还要显示一下transLogId
//                if (!isOpenBarCodeStatus()) {
//                    String qrText = formatForC(transLogId);
//                    sb.append(qrText);
//                }

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
