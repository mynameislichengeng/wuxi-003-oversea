package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class CashierIdContent extends PrintBase {

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

    public static HTMLPrintModel.SimpleLine printHtmlActivity(Context context, DailyDetailResp resp) {
        String optName = resp.getOptName();
        if (TextUtils.isEmpty(optName)) {
            optName = "";
        }
        HTMLPrintModel.SimpleLine html = new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + optName);
        return html;
    }


    public static String printString(Context context) {
        String title = context.getString(R.string.print_cashier_id);


        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        String operateNo = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
        StringBuffer sb = new StringBuffer();
        sb.append(cahierId);
        sb.append("(");
        sb.append(operateNo);
        sb.append(")");
        String value = sb.toString();


        StringBuffer result = new StringBuffer();
        if (isComputerSpaceForLeftRight()) {
            result.append(createTextLineForLeft(title, value));
            result.append(formartForLineSpace());
        } else {
            result.append(title);
            result.append(value);
            result.append(formatForBr());
            result.append(formatForBr());
            result.append(formatForNBr());
        }

        return result.toString();
    }


    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String title = context.getString(R.string.print_cashier_id);

        String optNameStr = resp.getOptName();
        if (TextUtils.isEmpty(optNameStr)) {
            optNameStr = "";
        }
        if (isComputerSpaceForLeftRight()) {
            optNameStr = createTextLineForLeft(title, optNameStr);
            optNameStr += formartForLineSpace();
        } else {
            optNameStr = title + optNameStr;
            optNameStr += formatForBr();
            optNameStr += formatForBr();
            optNameStr += formatForBr();
        }

        return optNameStr;
    }


}
