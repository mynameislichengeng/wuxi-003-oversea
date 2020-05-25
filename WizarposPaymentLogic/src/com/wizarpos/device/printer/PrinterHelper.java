package com.wizarpos.device.printer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.wizarpos.jni.PrinterInterface;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.recode.print.constants.KeyWords;
import com.wizarpos.recode.print.service.impl.PrintDeviceForA920HandleImpl;
import com.wizarpos.recode.print.service.impl.PrintDeviceForAMPHandleImpl;
import com.wizarpos.recode.print.service.impl.PrintDeviceForN3N5HandleImpl;
import com.wizarpos.recode.print.service.impl.PrintDeviceForQ2HandleImpl;

import java.util.List;

public class PrinterHelper {

    public static final int BIT_WIDTH = 384;
    private static final int WIDTH = 48;
    private static final int GSV_HEAD = 8;


    public static void print(String text) {
        Log.d("print", "PrinterHelper 打印的内容:" + text);
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {
            KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
            trigger.setHandle(new PrintDeviceForN3N5HandleImpl());
            trigger.setSource(text);
            trigger.parse();
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
            KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
            trigger.setHandle(new PrintDeviceForA920HandleImpl());
            trigger.setSource(text);
            trigger.parse();
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_AMP8) {
            KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
            trigger.setHandle(new PrintDeviceForAMPHandleImpl());
            trigger.setSource(text);
            trigger.parse();
        } else {
            try {
                KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
                trigger.setHandle(new PrintDeviceForQ2HandleImpl());
                trigger.setSource(text);
                trigger.parse();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                PrintDeviceForQ2HandleImpl.closePrint();
            }

        }
    }


}
