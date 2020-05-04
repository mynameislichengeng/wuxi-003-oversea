package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.wizarpospaymentlogic.R;

public class DeviceContent extends PrintBase {

    /**
     * 【953、 954,955】 使用
     *
     * @param context
     * @return
     */
    public static HTMLPrintModel.SimpleLine printHtmlDevice(Context context) {
        return new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn));
    }

    public static HTMLPrintModel.SimpleLine printHtmlDeviceActivity(Context context, DailyDetailResp resp) {
        String devicetitle = context.getString(R.string.print_terminal_id);
        String snStr = resp.getSn();
        HTMLPrintModel.SimpleLine simpleLine = new HTMLPrintModel.SimpleLine(devicetitle + snStr);
        return simpleLine;
    }


    /**
     * 【953、 954,955】 使用
     *
     * @param context
     * @return
     */
    public static String printStringDevice(Context context) {
        String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
        return printStringBase(context, terminalId);
    }

    public static String printStringDeviceActivity(Context context, DailyDetailResp resp) {

        return printStringBase(context, resp.getSn());
    }

    private static String printStringBase(Context context, String snStr) {
        String devicetitle = context.getString(R.string.print_terminal_id);
        if (TextUtils.isEmpty(snStr)) {
            snStr = "";
        }
        if (isComputerSpaceForLeftRight()) {
            devicetitle = createTextLineForLeft(devicetitle, snStr);
        } else {
            if (snStr.length() > PART_LENGTH) {
                devicetitle += formatForBr();
                devicetitle += multipleSpaces(33 - snStr.length()) + snStr;

            } else {
                devicetitle += snStr;
            }
            devicetitle += formatForBr();
        }
        return devicetitle;
    }
}
