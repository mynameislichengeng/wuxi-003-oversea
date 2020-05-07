package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class AcctContent extends PrintBase {


    public static String printStringPayfor(Context context, TransactionInfo transactionInfo) {
        String thirdExtId = transactionInfo.getThirdExtId();
        try {
            return printStringBase(context, thirdExtId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;


    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String thirdExtId = resp.getThirdExtId();
        try {
            return printStringBase(context, thirdExtId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String thirdExtId = resp.getThirdExtId();
        try {
            return printStringBase(context, thirdExtId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String printStringBase(Context context, String thirdExtId) throws UnsupportedEncodingException {
        String acct = thirdExtId;
        if (!TextUtils.isEmpty(acct)) {
            StringBuffer sb = new StringBuffer();
            //
            String printAcct = context.getString(R.string.print_acct);

            if (isComputerSpaceForLeftRight()) {
                sb.append(createTextLineForLeftAndRight(printAcct, acct));
                sb.append(formartForLineSpace());
            } else {
                sb.append(printAcct);
                if (acct.length() < PART_LENGTH) {
                    String space = multipleSpaces(getAcctSpaceCount(acct) - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length);
                    sb.append(space);
                } else {
                    sb.append(formatForBr());
                    String space = multipleSpaces(getAcctSpaceCountArray(acct) - acct.getBytes("GBK").length);
                    sb.append(space);
                }
                //
                sb.append(acct);
                sb.append(formatForBr());
                sb.append(formatForBr());
                sb.append(formatForNBr());
            }

            return sb.toString();
        }
        return null;
    }


    private static int getAcctSpaceCount(String content) {
        return 32;
    }

    private static int getAcctSpaceCountArray(String content) {

        return 27;
    }
}
