package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class TransTypeContent extends PrintBase {


    public static String printStringPayfor(Context context, TransactionInfo transactionInfo) {
//        String printType = context.getString(R.string.print_type);
//        if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
//            printString += printType + multipleSpaces(26 - printType.getBytes("GBK").length) + "Alipay" + builder.br();
//        } else if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
//            printString += printType + multipleSpaces(22 - printType.getBytes("GBK").length) + "Wechat Pay" + builder.br();
//        } else if (Constants.UNION.equals(transactionInfo.getPayType())) {
//            printString += printType + multipleSpaces(20 - printType.getBytes("GBK").length) + "Union Pay QR" + builder.br();
//        }

        String payType = "";
        if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
            payType = "Alipay";
        } else if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
            payType = "Wechat Pay";
        } else if (Constants.UNION.equals(transactionInfo.getPayType())) {
            payType = "Union Pay QR";
        }

        String result = "";
        try {
            result = printStringBase(context, payType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String payType = resp.getTransKind();
        if (payType.contains("Wechat")) {
            payType = "Wechat Pay";
        } else if (payType.contains("Union")) {
            payType = "Union Pay QR";
        } else if (payType.contains("Ali")) {
            payType = "Alipay";
        }

        String result = "";
        try {
            result = printStringBase(context, payType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
//        String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
        String payType = resp.getTransName();
        if (payType.contains("Wechat")) {
            payType = "Wechat Pay";
        } else if (payType.contains("Union")) {
            payType = "Union Pay QR";
        } else if (payType.contains("Ali")) {
            payType = "Alipay";
        }

        String result = "";
        try {
            result = printStringBase(context, payType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;

    }


    private static String printStringBase(Context context, String payType) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String title = getTitle(context);
        sb.append(title);
        String space = multipleSpaces(getTranTypeSpaceCount() - title.getBytes("GBK").length - payType.length());
        sb.append(space);
        sb.append(payType);
        sb.append(formatForBr());
        return sb.toString();
    }

    private static String getTitle(Context context) {
        return context.getString(R.string.print_type);
    }

    private static int getTranTypeSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 32 + 20;
        } else {
            return 32;
        }
    }
}