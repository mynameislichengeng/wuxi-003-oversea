package com.wizarpos.recode.data.info;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.atool.util.GetSnHelper;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UuidUitl;
import com.wizarpos.recode.print.devicesdk.amp.AMPPrintManager;

public class SnManager {

    public static String getSn(Context context) {
        String terminalUniqNo;
        if (DeviceManager.getInstance().isWizarDevice() || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_SHENGTENG_M10) {
            terminalUniqNo = android.os.Build.SERIAL;//终端序列号
//            terminalUniqNo = "G3100000003";//终端序列号
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {
            terminalUniqNo = PaymentApplication.getInstance().deviceEngine.getDeviceInfo().getSn();
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PULAN) {
            terminalUniqNo = GetSnHelper.getMacAndSn(context);
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
            terminalUniqNo = android.os.Build.SERIAL;//终端序列号
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_AMP8) {

            terminalUniqNo = AMPPrintManager.getInstance().getSN();
        } else {
            terminalUniqNo = DeviceManager.getImei(context);//IMEI地址
        }
        if (TextUtils.isEmpty(terminalUniqNo)) {
            terminalUniqNo = UuidUitl.getUuid();
        }
        return terminalUniqNo;
    }


}
