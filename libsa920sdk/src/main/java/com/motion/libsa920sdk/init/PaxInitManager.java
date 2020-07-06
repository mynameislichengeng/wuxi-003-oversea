package com.motion.libsa920sdk.init;

import android.content.Context;
import android.os.Build;

import com.motion.libsa920sdk.R;
import com.motion.libsa920sdk.printer.server.config.SettingINI;
import com.pax.poslink.CommSetting;
import com.pax.poslink.LogSetting;
import com.pax.poslink.POSLinkAndroid;

import java.util.Arrays;

public class PaxInitManager {


    /**
     * PAX初始化
     *
     * @param context
     */
    public static void init(Context context) {
//        CommSetting commSetting = setupSetting(context);
//        POSLinkAndroid.init(context, commSetting);

        //如果是不需要二维码
        POSLinkAndroid.init(context);
    }


    private static CommSetting setupSetting(Context context) {
        String settingIniFile = SettingINI.getCommonSettingPath(context);
        CommSetting commSetting = SettingINI.getCommSettingFromFile(context, settingIniFile);
        String commType = commSetting.getType();
        int index = Arrays.asList(context.getResources().getStringArray(R.array.commSetting_types)).indexOf(commType);

        if (index == -1) {
            if (Build.MODEL.startsWith("E")) {
                commSetting.setType(CommSetting.USB);
            } else if (Build.MODEL.startsWith("A9")) {
                commSetting.setType(CommSetting.AIDL);
            } else {
                commSetting.setType(CommSetting.TCP);
            }
            commSetting.setTimeOut("60000");
            commSetting.setSerialPort("COM1");
            commSetting.setBaudRate("9600");
            commSetting.setDestIP("172.16.20.15");
            commSetting.setDestPort("10009");
            commSetting.setMacAddr("");
            commSetting.setEnableProxy(false);

            SettingINI.saveCommSettingToFile(context, settingIniFile, commSetting);
        }

        if (!SettingINI.loadSettingFromFile(settingIniFile)) {
            //String LogOutputFile = getApplicationContext().getFilesDir().getAbsolutePath() + "/POSLog.txt";
            String LogOutputFile = context.getExternalFilesDir(null).getPath();
            LogSetting.setLogMode(true);
            LogSetting.setLevel(LogSetting.LOGLEVEL.DEBUG);
            LogSetting.setOutputPath(LogOutputFile);
            SettingINI.saveLogSettingToFile(settingIniFile);
        }
        return SettingINI.getCommSettingFromFile(context, settingIniFile);
    }

}
