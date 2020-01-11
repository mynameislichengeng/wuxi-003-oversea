package com.wizarpos.recode.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageAndroidManager {

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}
