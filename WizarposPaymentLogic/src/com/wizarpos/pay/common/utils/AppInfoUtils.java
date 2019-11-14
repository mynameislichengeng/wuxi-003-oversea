package com.wizarpos.pay.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.pay.model.AppInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Author: Huangweicai
 * @date 2016-04-22 13:38
 * @Description:应用信息类
 */
public class AppInfoUtils {
    private static final String LOG_TAG = "AppInfoUtils";
    /**
     * 需要获取包名和版本信息的应用包名
     */
    private static final String[] otherWizarPackages =
                                        {"com.wizarpos.member"                  //POS会员
                                        ,"com.wizarpos.pay"                  //POS收款
                                        ,"com.wizarpos.pay2"                  //手持收款
                                        ,"com.wizarpos.wizarposmemberui"        //手持会员
                                        ,"com.wizarpos.comcashier"              //pos/手持销售
                                        };
    /**
     * 需要过滤的字符串
     */
    private static final String removeWizarStr = "com.wizarpos.";

    /**
     *
     * @param mContext
     * @return 获得包名
     */
    public static String getPackageName(Context mContext) {
        String pkName = mContext.getPackageName();
        Log.d(LOG_TAG,"包名为:" + pkName);
        return pkName;
    }


    /**
     *
     * @param mContext
     * @return 获得收款规则的版本名称
     */
    public static String getPayVersionName(Context mContext) {
        String versionName = getVersionName(mContext);
        if (!TextUtils.isEmpty(versionName)) {
            if (versionName.contains("_")) {//现有规则是根据下划线来分割  如 1.0.0_LAWSON 取1.0.0
                versionName = versionName.split("_")[0];
            }
        }
        return versionName;
    }

    /**
     *
     * @param mContext
     * @return 获得版本名称
     */
    public static String getVersionName(Context mContext) {
        String pkName = getPackageName(mContext);
        String versionName = "";
        try {
            versionName = mContext.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG,"版本名称为:" + versionName);
        return versionName;
    }

    /**
     *
     * @param mContext
     * @return 获得版本code
     */
//    public static int getVersionCode(Context mContext) {
//        int versionCode = -1;
//        try {
//            versionCode = mContext.getPackageManager()
//                    .getPackageInfo(getPackageName(mContext), 0).versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionCode;
//    }

    /**
     * 获得应用渠道名
     * @param mContext
     * @return
     */
    public static String getPayVersionBelong(Context mContext) {
        String versionName = getVersionName(mContext);
        if (!TextUtils.isEmpty(versionName)) {
            if (versionName.contains("_")) {//现有规则是根据下划线来分割  如 1.0.0_LAWSON 取LAWSON
                versionName = versionName.split("_")[1];
            }
        }
        return versionName;
    }

    public static AppInfo getAppInfo(Context mContext){
        AppInfo info = new AppInfo();
        info.setBuildVersion(getBuildVersion());
        info.setPackageInfos(getWizarAppVersionInfo(mContext));
        info.setModel(getModelName());
        info.setKernelVersion(getKernelVersion());
        info.setAndroidVersion(getAndroidVersionName());
        info.setHsmVersion(getHsmVersion());
        return info;
    }

    /**
     * 获取所有慧商应用的应用与版本信息
     * @param context
     * @return
     */
    private static String getWizarAppVersionInfo(Context context){
        PackageManager mPackageManager = context.getPackageManager();
        StringBuilder sb = new StringBuilder();
//        sb.append(getPackageName(context).replace(removeWizarStr,"") + "_" + getVersionName(context));
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages){
            String packageName = packageInfo.packageName;
            String versionName = packageInfo.versionName;
            for (String pkg : otherWizarPackages) {
                if (packageName.contains(pkg)){
                    sb.append("|" + packageName.replace(removeWizarStr,"") + "_" + versionName);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取固件版本
     * @return
     */
    private static String getBuildVersion(){
        Log.i("YS!!",getsystemPropertie("ro.build.version.incremental"));
        return getsystemPropertie("ro.build.version.incremental");
    }

    /**
     * 获取系统信息
     * @param key
     * @return
     */
    public static String getsystemPropertie(String key){
        Object bootloaderVersion = "" ;
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Log.i("systemProperties", systemProperties.toString());
            bootloaderVersion = systemProperties.getMethod("get", new Class[]{String.class, String.class}).invoke(systemProperties, new Object[]{key,"unknown"});
            Log.i("bootloaderVersion", bootloaderVersion.getClass().toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bootloaderVersion.toString();
    }

    /**
     * 获取android版本
     * @return
     */
    private static String getAndroidVersionName(){
//        Log.i("YS!!","" + android.os.Build.VERSION.RELEASE);
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * 获取机器型号
     * @return
     */
    private static String getModelName(){
//        Log.i("YS!!",getsystemPropertie("ro.product.model") + "");
        return getsystemPropertie("ro.product.model");
    }

    /**
     * 获取安全模块固件版本
     * @return
     */
    private static String getHsmVersion(){
//        Log.i("YS!!",getsystemPropertie("ro.wp.hsm.ver"));
        return getsystemPropertie("ro.wp.hsm.ver");
    }

    /**
     * 获取内核版本
     * @return
     */
    public static String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (info != "") {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return kernelVersion;
    }
}
