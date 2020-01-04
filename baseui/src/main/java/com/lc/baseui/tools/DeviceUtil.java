package com.lc.baseui.tools;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/4/9.
 */

public class DeviceUtil {

    /**
     * 设备的分辨率
     **/
    public static void getDevicePx(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPix = displayMetrics.widthPixels;
        int heightPix = displayMetrics.heightPixels;
        float density = displayMetrics.density;
        int densityDpi =displayMetrics.densityDpi;
        lg.d(lg.TAG,"getDevicePx()>>widthPix:"+widthPix+",heightPix:"+heightPix);
        lg.d(lg.TAG,"getDevicePx()>>density:"+density+",densityDpi:"+densityDpi);
    }
}
