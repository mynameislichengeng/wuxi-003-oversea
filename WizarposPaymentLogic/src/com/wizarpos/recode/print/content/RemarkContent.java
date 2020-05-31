package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class RemarkContent extends PrintBase {

    public static String printStringActivity(Context context, DailyDetailResp resp) {
        String remark = resp.getDiffCode();

        if (TextUtils.isEmpty(remark)) {
            return null;
        }
        return printStringBase(context, remark);
    }

    public static String printStringRefund(Context context, RefundDetailResp resp) {
        String remark = resp.getDiffCode();

        if (TextUtils.isEmpty(remark)) {
            return null;
        }
        return printStringBase(context, remark);
    }

    private static String printStringBase(Context context, String remark) {
        String leftText = context.getString(R.string.print_remark);
        StringBuffer sb = new StringBuffer();
        if (isComputerSpaceForLeftRight()) {
            sb.append(createTextLineForLeftAndRight(leftText, remark));
            return sb.toString();
        } else {
            sb.append(createTextLineForLeftAndRight(leftText, remark));
            return sb.toString();
        }

    }
}
