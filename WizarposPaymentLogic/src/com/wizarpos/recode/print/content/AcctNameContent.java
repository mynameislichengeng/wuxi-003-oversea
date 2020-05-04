package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class AcctNameContent extends PrintBase {


    public static String printStringPayfor(Context context, TransactionInfo transactionInfo) {
        String acctName = transactionInfo.getThirdExtName();
        try {
            return printStringBase(context, acctName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String acctName = resp.getThirdExtName();
        try {
            return printStringBase(context, acctName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String acctName = resp.getThirdExtName();
        try {
            return printStringBase(context, acctName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String printStringBase(Context context, String thirdExtName) throws UnsupportedEncodingException {


        String acctName = thirdExtName;
        if (!TextUtils.isEmpty(acctName)) {
            StringBuffer sb = new StringBuffer();
            String printAcctName = context.getString(R.string.print_acctName);

            if (isComputerSpaceForLeftRight()) {
                sb.append(formartForLeftAndRight(printAcctName, acctName));
            } else {
                sb.append(printAcctName);
                //
                String space = multipleSpaces(getAccNameSpaceCount() - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length);
                sb.append(space);
                //
                sb.append(acctName);
                sb.append(formatForBr());
            }


            return sb.toString();
        }
        return null;
    }

    private static int getAccNameSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 32 + 6;
        } else {
            return 32;
        }
    }
}
