package com.wizarpos.recode.print.content;

import android.content.Context;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.wizarpospaymentlogic.R;

public class CashierIdContent {

    /**
     * [953\954支付中用到]
     *
     * @param context
     * @return
     */
    public static HTMLPrintModel.SimpleLine printHtml(Context context) {
        return new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName));
    }

    public static String printString(Context context) {
        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        return context.getString(R.string.print_cashier_id) + cahierId;
    }
}
