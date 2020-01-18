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
        String tranlogId = Tools.deleteMidTranLog(tranLogId, AppConfigHelper.getConfig(AppConfigDef.mid));
        String printRecepit = getTitle(context);
        String result = "";
        result += printRecepit;
        result += "#";
        result += multipleSpaces(getRecepitSpaceCount() - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId;
        result += formatForBr();
        return result;
    }

    private static String getTitle(Context context) {
        String printRecepit = context.getString(R.string.print_receipt);
        return printRecepit;
    }

    private static int getRecepitSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 31 + 5;
        } else {
            return 31;
        }
    }
}
