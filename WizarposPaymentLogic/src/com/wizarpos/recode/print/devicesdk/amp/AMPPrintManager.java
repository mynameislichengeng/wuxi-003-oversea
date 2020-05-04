package com.wizarpos.recode.print.devicesdk.amp;

import android.content.Context;
import android.util.Log;

import com.pos.device.SDKManager;
import com.pos.device.SDKManagerCallback;
import com.pos.device.beeper.Beeper;
import com.pos.device.config.DevConfig;

public class AMPPrintManager {

    private final String TAG = AMPPrintManager.class.getSimpleName();
    private static AMPPrintManager instance;

    public static synchronized AMPPrintManager getInstance() {
        if (instance == null) {
            instance = new AMPPrintManager();
        }
        return instance;
    }

    public void init(Context context) {
        SDKManager.init(context, new SDKManagerCallback() {
            @Override
            public void onFinish() {
                try {
                    Log.d("tag", TAG + ">>init()>>onFinish()");
                    Beeper.getInstance().beep(1600, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获得SN号
     *
     * @return
     */
    public String getSN() {
//        return DevConfig.getSN();
        return "0820647289";
    }


}
