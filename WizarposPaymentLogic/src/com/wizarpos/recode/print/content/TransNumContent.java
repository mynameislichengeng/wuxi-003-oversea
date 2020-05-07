package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class TransNumContent extends PrintBase {


    public static String printStringPayfor(Context context, TransactionInfo transactionInfo) {
        String thirdTransOrder = transactionInfo.getThirdTradeNo();
        try {
            return printStringBase(context, thirdTransOrder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String thirdTransOrder = resp.getThirdTradeNo();
        try {
            return printStringBase(context, thirdTransOrder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String thirdTransOrder = resp.getThirdTradeNo();

        try {
            return printStringBase(context, thirdTransOrder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String printStringBase(Context context, String thirdTradeNo) throws UnsupportedEncodingException {
        String thirdTransOrder = thirdTradeNo;
        if (!TextUtils.isEmpty(thirdTransOrder)) {
            StringBuffer sb = new StringBuffer();
            String tilte = context.getString(R.string.print_trans);
            if (isComputerSpaceForLeftRight()) {
                sb.append(createTextLineForLeftAndRight(tilte, thirdTransOrder));
            } else {
                sb.append(tilte);
                sb.append(formatForBr());
                String space = multipleSpaces(getTranNumSpaceCount() - thirdTransOrder.getBytes("GBK").length);
                sb.append(space);
                sb.append(thirdTransOrder);
                sb.append(formatForBr());
            }


            return sb.toString();
        }
        return null;
    }

    private static int getTranNumSpaceCount() {

        return 32;
    }
}
