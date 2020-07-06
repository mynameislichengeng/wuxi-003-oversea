package com.motion.libsa920sdk.printer.server.internal;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pax.poslink.CommSetting;
import com.pax.poslink.POSLinkCommon;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Convenience {

    private static final int BUTTON_TRIGGER_CNG = 20;
    public static final String KEY_CONVENIENCE_BUTTON_CLICK_CNT = "Convenience_button_click_cnt";

    private static int buttonClickCnt = 0;

//    public static void init(Context context) {
//        buttonClickCnt = SharedPreferenceHelper.getInt(KEY_CONVENIENCE_BUTTON_CLICK_CNT, 0);
//    }
//
//    public static void clickBtn() {
//        buttonClickCnt++;
//        SharedPreferenceHelper.save(KEY_CONVENIENCE_BUTTON_CLICK_CNT, buttonClickCnt);
//    }

    public static boolean isButtonClickEnough() {
        return buttonClickCnt >= BUTTON_TRIGGER_CNG;
    }

    public static void setHost(Context context, CommSetting commSetting, String host) {
        try {
            Method setHostMethod = commSetting.getClass().getDeclaredMethod("setHost", Context.class, String.class);
            setHostMethod.setAccessible(true);
            setHostMethod.invoke(commSetting, context, host);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static String getHost(Context context, CommSetting commSetting) {
        try {
            Method getHostMethod = commSetting.getClass().getDeclaredMethod("getHost", Context.class);
            getHostMethod.setAccessible(true);
            return (String) getHostMethod.invoke(commSetting, context);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    public static String omahaSendData(String s1c, String s1f) {
        return POSLinkCommon.S_STX
                                                    + "*1PPX81.022009001234566" + "#"
                                                    + "PAXP" + s1c
                                                    + "A01" + s1f
                                                    + "3C20" + s1f
                                                    + "1" + s1f
                                                    + "4" + s1f
                                                    + "9" + s1c
                                                    + "4012000033330026" + "="
                                                    + "49121015432112345601" + s1c
                                                    + "9.00" + s1c
                                                    + "" + s1c
                                                    + "00030999" + s1c
                                                    + "6" + s1c
                                                    + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c
                                                    + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c
                                                    + "" + s1f + "C" + s1f + "" + s1f + "" + s1f + "" + s1f + "" + s1f + "" + s1f + "1" + s1f
                                                    + "DPX003" + s1c
                                                    + "" + s1c + "" + s1c + "" + s1c + "" + s1c + "" + s1c
                                                    + "" + POSLinkCommon.S_ETX
                                                    + "f";
    }
}
