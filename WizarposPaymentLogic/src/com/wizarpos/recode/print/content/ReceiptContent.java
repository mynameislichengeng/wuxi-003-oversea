package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;

public class ReceiptContent extends PrintBase {


    public static String printStringPayfor(Context context, TransactionInfo transactionInfo) {
        String tranLogId = transactionInfo.getTranLogId();
        String str = "";
        try {
            str = printStringBase(context, tranLogId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String tranLogId = resp.getTranLogId();
        String str = "";
        try {
            str = printStringBase(context, tranLogId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String tranLogId = resp.getTranLogId();
        String str = "";
        try {
            str = printStringBase(context, tranLogId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

    private static String printStringBase(Context context, String tranLogId) throws UnsupportedEncodingException {
        String result = "";
        String printRecepit = getTitle(context);
        String tranlogId = Tools.deleteMidTranLog(tranLogId, AppConfigHelper.getConfig(AppConfigDef.mid));
        if (isComputerSpaceForLeftRight()) {
            StringBuffer sb = new StringBuffer();
            String left = printRecepit + "#";
            sb.append(createTextLineForLeftAndRight(left, tranlogId));
            return sb.toString();
        } else {

            result += printRecepit;
            result += "#";
            result += multipleSpaces(getRecepitSpaceCount(tranlogId) - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId;
            result += formatForBr();
        }

        return result;
    }

    private static String getTitle(Context context) {
        String printRecepit = context.getString(R.string.print_receipt);
        return printRecepit;
    }

    private static int getRecepitSpaceCount(String content) {
        int length = content.length();
        if (getDeviceTypeForN3N5()) {

            int countTr = 0;
            switch (length) {
                case 1:
                    countTr = 0;
                    break;
                case 2:
                    countTr = -1;
                    break;
                case 3:
                    countTr = -3;
                    break;
                case 4:
                    countTr = -4;
                    break;
                case 5:
                    countTr = -5;
                    break;
                case 6:
                    countTr = -7;
                    break;
                case 7:
                    countTr = -8;
                    break;
                case 8:
                    countTr = -9;
                    break;
                case 9:
                    countTr = -11;
                    break;
                case 10:
                    countTr = -12;
                    break;
                case 11:
                    countTr = -13;
                    break;
                case 12:
                    countTr = -15;
                    break;
                case 13:
                    countTr = -16;
                    break;
                case 14:
                    countTr = -17;
                    break;
                case 15:
                    countTr = -19;
                    break;
                case 16:
                    countTr = -20;
                    break;
                case 17:
                    countTr = -21;
                    break;
                case 18:
                    countTr = -23;
                    break;
                case 19:
                    countTr = -24;
                    break;
                case 20:
                    countTr = -25;
                    break;
            }

            return 53 + countTr;
        } else {
            return 31;
        }
    }
}
