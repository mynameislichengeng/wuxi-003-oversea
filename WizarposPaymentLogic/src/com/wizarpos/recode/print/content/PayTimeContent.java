package com.wizarpos.recode.print.content;

import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;

public class PayTimeContent extends PrintBase {

    public static String printStringPayfor(TransactionInfo transactionInfo) {
        String payTime = transactionInfo.getPayTime();

        return printStringBase(payTime);
    }

    public static String printStringRefund(RefundDetailResp resp) {
        String payTime = resp.getPayTime();

        return printStringBase(payTime);
    }


    public static String printStringActivity(DailyDetailResp resp) {
        String payTime = resp.getPayTime();
        return printStringBase(payTime);
    }

    private static String printStringBase(String payTime) {
        StringBuffer sb = new StringBuffer();
        String leftTime = payTime.substring(0, 10);
        String rightTime = payTime.substring(10);
        //
        sb.append(leftTime);
        //
        String space = multipleSpaces(getPaytimeSpaceCount() - rightTime.length());
        sb.append(space);
        sb.append(rightTime);
        sb.append(formatForBr());
        return sb.toString();
    }


    private static int getPaytimeSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 35;
        } else {
            return 22;
        }
    }
}
