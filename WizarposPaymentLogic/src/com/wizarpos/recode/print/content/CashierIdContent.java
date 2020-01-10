package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.wizarpospaymentlogic.R;

public class CashierIdContent {

    /**
     * [953\954支付中用到]
     *
     * @param context
     * @return
     */
    public static HTMLPrintModel.SimpleLine printHtml(Context context) {

        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        String operateNo = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
        StringBuffer sb = new StringBuffer();
        sb.append(cahierId);
        sb.append("(");
        sb.append(operateNo);
        sb.append(")");
        return new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + sb.toString());
    }

    public static String printString(Context context) {
        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        String operateNo = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
        StringBuffer sb = new StringBuffer();
        sb.append(cahierId);
        sb.append("(");
        sb.append(operateNo);
        sb.append(")");
        return context.getString(R.string.print_cashier_id) + sb.toString();
    }


    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String optNameStr = resp.getOptName();
        if (TextUtils.isEmpty(optNameStr)) {
            optNameStr = "";
        }
        return context.getString(R.string.print_cashier_id) + optNameStr;
    }

    public static HTMLPrintModel.SimpleLine printHtmlActivity(Context context, DailyDetailResp resp) {
        String optName = resp.getOptName();
        if (TextUtils.isEmpty(optName)) {
            optName = "";
        }
        HTMLPrintModel.SimpleLine html = new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + optName);
        return html;
    }
}
