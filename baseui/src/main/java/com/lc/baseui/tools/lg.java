
package com.lc.baseui.tools;

import android.util.Log;

/**
 * 日志工具类
 *
 * @author uzoo
 */
public class lg {
	public static  String TAG = "haian";
    /**
     * 打印日志的开关
     */
    public static boolean isLogOn = true;// 打印信息开关量

    /**
     * 详细的log日志打印
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     */
    public static void v(String tag, String msg) {
        if (isLogOn)
            Log.v(tag, msg);
    }

    /**
     * 详细的log日志打印
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     * @param tr An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (isLogOn)
            Log.v(tag, msg, tr);
    }

    /**
     * 打印Debug 日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     */
    public static void d(String tag, String msg) {
        if (isLogOn)
//            Log.d(tag, msg);
            Log.e(tag, msg);
    }

    /**
     * 打印Debug 日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     * @param tr An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (isLogOn)
            Log.d(tag, msg, tr);
    }

    /**
     * 打印Debug日志信息2
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param method 日志所处方法名
     * @param msg 准备输出的日志信息
     */
    public static void d(String tag, String method, String msg) {
        if (isLogOn)
            Log.d(tag, method + ";" + msg);
    }

    /**
     * 打印Info 日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     * @param tr An exception to log
     */
    public static void i(String tag, String msg) {
        if (isLogOn)
            Log.i(tag, msg);
    }

    /**
     * 打印Info 日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     * @param tr An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (isLogOn)
            Log.i(tag, msg, tr);
    }

    /**
     * 打印warn（警告）日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     */
    public static void w(String tag, String msg) {
        if (isLogOn)
            Log.w(tag, msg);
    }

    /**
     * 打印warn（警告）日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     * @param tr an excption to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (isLogOn)
            Log.w(tag, msg, tr);
    }

    /**
     * 打印Error(错误)日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     */
    public static void e(String tag, String msg) {
        if (isLogOn)
            Log.e(tag, msg);
    }

    /**
     * 打印Error(错误)日志信息
     *
     * @param tag 打印日志的唯一表示，一般是类名或者方法名
     * @param msg 准备输出的日志信息
     * @param tr an excption to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (isLogOn)
            Log.e(tag, msg, tr);
    }

}
