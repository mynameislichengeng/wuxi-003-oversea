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
            sb.append(printAcct);

            if (acct.length() < PART_NUM_13) {
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
            return sb.toString();
        }
        return null;
    }


    private static int getAcctSpaceCount(String content) {
        if (getDeviceTypeForN3N5()) {
            int length = content.length();
            int countTr = 0;
            switch (length) {
                case 1:
                    countTr = 0;
                    break;
                case 2:
                    countTr = -3;
                    break;
                case 3:
                    countTr = -5;
                    break;
                case 4:
                    countTr = -7;
                    break;
                case 5:
                    countTr = -10;
                    break;
                case 6:
                    countTr = -13;
                    break;
                case 7:
                    countTr = -15;
                    break;
                case 8:
                    countTr = -18;
                    break;
                case 9:
                    countTr = -21;
                    break;
                case 10:
                    countTr = -23;
                    break;
                case 11:
                    countTr = -26;
                    break;
                case 12:
                    countTr = -28;
                    break;


            }

            return 57 + getCOUNTSPACE();

        } else {
            return 32;
        }
    }

    private static int getAcctSpaceCountArray(String content) {
        if (getDeviceTypeForN3N5()) {
            int length = content.length();
            int countTr = 0;
            switch (length) {

                case 13:
                    countTr = 0;
                    break;
                case 14:
                    countTr = 0;
                    break;
                case 15:
                    countTr = -2;
                    break;
                case 16:
                    countTr = -4;
                    break;
                case 17:
                    countTr = -6;
                    break;
                case 18:
                    countTr = -7;
                    break;
                case 19:
                    countTr = -8;
                    break;
                case 20:
                    countTr = -9;
                    break;
                case 21:
                    countTr = -10;
                    break;
                case 22:
                    countTr = -11;
                    break;

            }

            return 30 + countTr;

        } else {
            return 32 - 5;
        }
    }
}
