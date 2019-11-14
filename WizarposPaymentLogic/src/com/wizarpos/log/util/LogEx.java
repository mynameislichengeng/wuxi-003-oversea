/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 日志打印类实现文件
 * </p>
 *
 * @Title LogEx.java
 * @Package com.zte.iptvclient.android.common
 * @version 1.0
 * @author zhang.longfei 10126853
 * @date 2012-02-11
 * IPTV客户端Android通用功能包
 */

/** IPTV客户端Android通用功能包  */
package com.wizarpos.log.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.wizarpospaymentlogic.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


/**
 * 日志打印类，汇集日志打印相关的功能函数
 *
 * @ClassName:LogEx
 * @Description: 汇集日志打印相关的功能函数
 * @author: zhang.longfei 10126853
 * @date: 2012-2-11
 *
 */
public final class LogEx {
    private final static String LOG_TAG = "LogEx";
    private static final String STR_FORMAT_YEAR_MOUTH_DAY = "yyyyMMdd";

    /** 默认java的文件后缀名 */
    public final static String STR_DEFAULT_JAVA_FILE_EXT = ".java";

    /** 日志文件名 */
    public final static String STR_LOG_FILE_NAME = "iptvclientlog.txt";

    /** 备份日志文件名 */
    public final static String STR_BAK_LOG_FILE_NAME = "iptvclientlogbak.txt";

    /** 日志输出到文件的标识，Debug版本置true，输出到文件，release版本置false输出到文件 */
    private static boolean FLAG_OUTPUT_LOGINFO_TO_FILE = false;

    /** 日志级别 */
    private static LogLevelType m_typeLogLevel = LogLevelType.TYPE_LOG_LEVEL_DEBUG;

    /** SD卡路径 */
    private static String m_strSDAdress = null;

    /** 单个日志文件大小，单位MB */
    private static int m_ilogFileSize = 5;
    /** 日志文件总大小，单位MB */
    private static int miAllLogFileSize = 10;
    /** 存储日志打印的集合 */
    private static LinkedList<Object> mstoreLogList = new LinkedList<Object>();
    private static int MAX = 50;
    /** 优先存储类管理对象 */
    private static PreferenceHelper mprefHelper = null;
    /** 第一进入应用获取的当前时间 */
    private static String mstrCurTime = null;
    /** 当前保存文件的路径 */
    private static String mstrFilePath = null;
    /** 当天生成的第几份文件的标识 */
    private static int index = 1;
    /** 前天生成的最后一份文件的标识 */
    private static int preIndex = 1;
    /** 记录生成共几份日志文件 */
    private static int icount = 0;
    /** 生成的文件 */
    private static File fileLog = null;
    /** 文件输出流 */
    private static FileOutputStream outputStream = null;
    /** 设备唯一编号,优先获取imei号，如果获取不到则获取MAC。如果还是获取不到，则返回null */
//    private static String mstrAndroidId = null;
    /** 操作系统版本 */
    private static String mstrOSVersion = null;
    /** 设备软件版本 */
    private static String mstrSoftVersion = null;
    /** 终端类型 */
    private static String mstrDeviceType = "Pad";
    /** 登陆的父账号 */
    private static String mstrUserName = null;
    /** 登陆的OTT子账号 */
    private static String mstrSubUserName = null;
    /** 选择的机顶盒账号 */
    private static String mstrStbUserCode = null;
    /** 文件路径 */
    private static String mfilePath = null;
    /** 创建文件的时间 */
    private static Date mDataNewFile = null;
    /** 创建前一天文件的时间 */
    private static Date mDataPreNewFile = null;
    /** 创建前一天文件的时间 */
    private static String mstrPreTime = null;
    /** 是否继续接着前一天的最后一份日志写入 */
    private static boolean mbIsContinueWrite = false;
    /** 过期的天数 */
    private static long miDayExpired = 7;
    /** 是否创建新的日志文件标识 */
    private static boolean mbIsNewLogFile = false;

    private static final String LOG_Wizar = "wizarposLog";

    // 读文件

    /**
     * 日志级别类型，描述日志打印的级别
     *
     * @ClassName:LogLevelType
     * @Description: 描述日志打印的级别
     * @author: zhang.longfei 10126853
     * @date: 2012-2-11
     *
     */
    public enum LogLevelType {
        /** 调试类型日志 */
        TYPE_LOG_LEVEL_DEBUG(1),
        /** 关键信息类型日志 */
        TYPE_LOG_LEVEL_INFO(2),
        /** 警告类型日志 */
        TYPE_LOG_LEVEL_WARNING(3),
        /** 错误类型日志 */
        TYPE_LOG_LEVEL_ERROR(4);

        /**
         * 构造函数,初始化枚举元素值
         *
         * @param iValue
         *            输入整数值
         */
        LogLevelType(int iValue) {
            this.m_iEnumValue = iValue;
        }

        /**
         * 获取枚举型对应的整数值
         * <p>
         * Description: 获取枚举型对应的整数值
         * <p>
         *
         * @date 2012-2-11
         * @return int型，该枚举元素对应的值
         */
        public int getValue() {
            return m_iEnumValue;
        }

        /** 枚举元素对应的整数值 */
        private final int m_iEnumValue;
    }

    /**
     * 不允许创建实例，隐藏构造函数
     */
    private LogEx() {
    }

    /**
     * 初始化数据
     */
    public static void initData(Context ctx) {

        getBasicInfo(ctx);

        getCurLogFileOfWrite();

    }

    private static void getCurLogFileOfWrite() {

        File file = new File(m_strSDAdress);
        // 获取当前时间的，如xx年xx月xx日
        mDataNewFile = TimeUtil.getSysTime();
        mDataPreNewFile = TimeUtil.addOffset(mDataNewFile, TimeUtil.UNIT_DATE, -1);
        LogEx.d(LOG_TAG, "mDataPreNewFile=" + mDataPreNewFile);
        mstrCurTime = TimeUtil.format(mDataNewFile, STR_FORMAT_YEAR_MOUTH_DAY);

        mbIsContinueWrite = isContinueWriteToPreLogFile(file);
        LogEx.d(LOG_TAG, "mbIsContinueWrite" + mbIsContinueWrite);
        LogEx.d(LOG_TAG, "file.length()=" + FileUtil.getFileSize(file));
        // 超过规定的大小，进行老化
        if (FileUtil.getFileSize(file) > miAllLogFileSize * 1024 * 1024) {
            deleteExpiredLog(file);
        }
    }

    /**
     *
     * isContinueWriteToPreLogFile
     * <p>
     * Description: 获取是否继续向前一天的日志写文件
     * <p>
     * @date 2014年9月26日
     * @author cgh6407000220
     * @param file
     * @return
     */
    private static boolean isContinueWriteToPreLogFile(File file) {
        boolean flag1 = false;
        boolean flag2 = false;
        mstrPreTime = TimeUtil.format(mDataPreNewFile, STR_FORMAT_YEAR_MOUTH_DAY);
        LogEx.d(LOG_TAG, "mstrPreTime=" + mstrPreTime);
        if (file.exists() && file.isDirectory()) {
            File[] childFiles = file.listFiles();
            int j = -1;
            int k = -1;
            for (int i = 0; i < childFiles.length; i++) {
                String strFileName = childFiles[i].getName();
                LogEx.d(LOG_TAG, "strFileName:" + strFileName);
                // 获取前天的最后一份日志的下标
                if (strFileName.contains(mstrPreTime)) {
                    flag1 = true;
                    k = Integer.valueOf(strFileName.substring(
                            strFileName.lastIndexOf("-") + 1, strFileName.lastIndexOf(".")));
                    // 交换数值，最后preIndex获取到文件名称下标的最大值
                    if (k > preIndex) {
                        preIndex = k;
                    }
                }
                // 获取当天的最后一份日志的下标
                if (strFileName.contains(mstrCurTime)) {
                    flag2 = true;
                    j = Integer.valueOf(strFileName.substring(
                            strFileName.lastIndexOf("-") + 1, strFileName.lastIndexOf(".")));
                    // 交换数值，最后index获取到文件名称下标的最大值
                    if (j > index) {
                        index = j;
                    }
                }
            }
        } else {
            return false;
        }
        LogEx.d(LOG_TAG, "flag1=" + flag1 + "flag2=" + flag2);
        return flag1 && !flag2;

    }

    /**
     *
     * getBasicInfo
     * <p>
     * Description: 获取基本信息
     * <p>
     * @date 2014年9月26日
     * @author cgh6407000220
     * @param ctx
     */
    private static void getBasicInfo(Context ctx) {
        mstrOSVersion = android.os.Build.VERSION.RELEASE;
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            mstrSoftVersion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mstrOSVersion = (TextUtils.isEmpty(mstrOSVersion)
                ? "unknown"
                : mstrOSVersion);
        mstrSoftVersion = (TextUtils.isEmpty(mstrSoftVersion)
                ? "unknown"
                : mstrSoftVersion);
        mprefHelper = new PreferenceHelper(ctx, "homePagePref");
        m_ilogFileSize = mprefHelper.getInt("FileSize", 5);
        miAllLogFileSize = mprefHelper.getInt("AllFileSize", 20);
        String filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/wizarpos/";
        m_strSDAdress = mprefHelper.getString("FilePath", filePath
                + "logs/");
        FLAG_OUTPUT_LOGINFO_TO_FILE = mprefHelper.getBoolean("IsLogToFile", false);
        String strLogLevel = mprefHelper.getString("LogLevel", "Error");
        if (strLogLevel.equals("Debug")) {
            m_typeLogLevel = LogLevelType.TYPE_LOG_LEVEL_DEBUG;
        } else if (strLogLevel.equals("Warning")) {
            m_typeLogLevel = LogLevelType.TYPE_LOG_LEVEL_WARNING;
        } else {
            m_typeLogLevel = LogLevelType.TYPE_LOG_LEVEL_ERROR;
        }
        LogEx.d(LOG_TAG, "m_typeLogLevel=" + m_typeLogLevel);

    }

    /**
     *
     * deleteExpiredLog
     * <p>
     * Description: 删除过期日志文件
     * <p>
     * @date 2014年9月26日
     * @author cgh6407000220
     * @param f
     */
    private static void deleteExpiredLog(File f) {
        LogEx.d(LOG_TAG, "------deleteExpiredLog-------");
        if (f == null || f.length() == 0) {
            LogEx.e(LOG_TAG, "f is null or length is 0");
            return;
        }
        //删除文件，当删除到文件规定的总大小的80%,停止删除操作
        if (FileUtil.getFileSize(f) < miAllLogFileSize * 1024 * 1024 * 0.8) {
            LogEx.e(LOG_TAG, "Don't need delete!!!");
            return;
        }
        File[] files = f.listFiles();
        // 将要删除的文件,默认为第一个文件
        File fileDelete = files[0];
        String strFileDeleteName = fileDelete.getName();
        String strFileDeleteTime = strFileDeleteName.substring(4,
                strFileDeleteName.lastIndexOf("-"));
        int i = Integer.valueOf(strFileDeleteName.substring(
                strFileDeleteName.lastIndexOf("-") + 1, strFileDeleteName.lastIndexOf(".")));
        Date dateFileDeleteTime = TimeUtil.getDate(strFileDeleteTime,
                STR_FORMAT_YEAR_MOUTH_DAY, TimeUtil.INT_ZONEFLAG_LOCAL);
        for (File file : files) {
            String strFileName = file.getName();
            String strFileTime = strFileName.substring(4, strFileName.lastIndexOf("-"));
            int j = Integer.valueOf(strFileName.substring(
                    strFileName.lastIndexOf("-") + 1, strFileName.lastIndexOf(".")));
            Date dateFileTime = TimeUtil.getDate(strFileTime, STR_FORMAT_YEAR_MOUTH_DAY,
                    TimeUtil.INT_ZONEFLAG_LOCAL);
            // 当前欲删除的文件比比较的文件的时间较晚，则交换删除的文件
            int iResult = TimeUtil.compare(dateFileDeleteTime, dateFileTime);
            if (iResult == 1) {
                fileDelete = file;
                strFileDeleteName = strFileName;
                strFileDeleteTime = strFileTime;
                dateFileDeleteTime = dateFileTime;
                i = j;
            } else if (iResult == 0) {
                if (i > j) {
                    fileDelete = file;
                    strFileDeleteName = strFileName;
                    strFileDeleteTime = strFileTime;
                    dateFileDeleteTime = dateFileTime;
                    i = j;
                }
            }
        }
        if (fileDelete.delete()) {
            deleteExpiredLog(f);
        } else {
            LogEx.e(LOG_TAG, "Delete files Failed!!!");
        }
    }

    /**
     * isExpired 检查日志是否过期
     *
     * @param file
     *            文件
     * @date 2012-3-3 日志级别
     */
    private static boolean isExpired(File file) {
        LogEx.d(LOG_TAG, "------isExpired-------");
        boolean bIsExpired = false;
        // 从文件的名称中获取文件创建时的时间
        String strFileName = file.getName();
        String strNewFileTime = strFileName.substring(4, strFileName.lastIndexOf("-"));
        LogEx.d(LOG_TAG, "strNewFileTime=" + strNewFileTime);
        Date datefileTime = TimeUtil.getDate(strNewFileTime, STR_FORMAT_YEAR_MOUTH_DAY,
                TimeUtil.INT_ZONEFLAG_LOCAL);
        long lOffSet = TimeUtil.calcOffset(mDataNewFile, datefileTime);
        LogEx.d(LOG_TAG, "lOffSet=" + lOffSet);
        LogEx.d(LOG_TAG, "mDataNewFile=" + mDataNewFile.toString());
        bIsExpired = TimeUtil.calcOffset(mDataNewFile, datefileTime) > miDayExpired * 24
                * 60 * 60 * 1000;
        LogEx.d(LOG_TAG, "bIsExpired=" + bIsExpired);
        return bIsExpired;
    }

    /**
     *
     * 设置当前日志级别，详见LogLevelType
     *
     * @date 2012-3-3
     * @param typeLevel
     *            日志级别
     */
    public static void setLogLevel(LogLevelType typeLevel) {
        LogEx.d(LOG_TAG, "typeLevel=" + typeLevel);
        m_typeLogLevel = typeLevel;
    }

    /**
     *
     * 获取当前日志级别
     *
     * @date 2012-3-3
     */
    public static LogLevelType getLogLevel() {
        return m_typeLogLevel;
    }

    /**
     * 格式化日志输出，如Login 2012-02-08 14:32:15.042 F[IPTVBaseActivity] L[52]
     * [onCreate] onCreate start
     *
     * @date 2012-2-28
     * @param strLevel
     *            日志级别
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     * @param strLineInfo
     *            行、文件名、方法名信息
     */
    private static String formatLogString(String strLevel, String strModuleName,
                                          String strInfo, String strLineInfo) {

        if (null == strModuleName || null == strInfo || null == strLineInfo) {
            return null;
        }

        // 日志格式：[D][Login][2012-02-08
        // 14:32:15.042]L[52][IPTVBaseActivity.java][onCreate] onCreate start
        String strReturnInfo = null;

        strReturnInfo = String.format("[%s][%s][%s]%s%s", strLevel, strModuleName,
        		dateToString(TimeUtil.getSysTime()), strLineInfo, strInfo);
        return strReturnInfo;
        // return String.format("%23s", TimeUtil.getFormattedLocalTimeStr());

    }

    /**
     * 格式化日志输出，如Login 2012-02-08 14:32:15.042 F[IPTVBaseActivity] L[52]
     * [onCreate] onCreate start
     *
     * @date 2012-2-28
     * @param strAndroidID
     *            设备唯一编号
     * @param strOSVersion
     *            操作系统版本
     * @param strSoftVersion
     *            软件版本
     * @param strDeviceType
     *            设备类型
     * @param strLevel
     *            日志级别
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     * @param strLineInfo
     *            行、文件名、方法名信息
     */
    private static String formatLogString(String strAndroidID, String strOSVersion,
                                          String strSoftVersion, String strDeviceType, String strLevel,
                                          String strModuleName, String strInfo, String strLineInfo) {

        if (null == strModuleName || null == strInfo || null == strLineInfo) {
            return null;
        }

        // 日志格式：[D][Login][2012-02-08
        // 14:32:15.042]L[52][IPTVBaseActivity.java][onCreate] onCreate start
        String strReturnInfo = null;

        strReturnInfo = String.format("[%s][%s][%s][%s][%s][%s][%-24s]%s%s",
                strAndroidID, strOSVersion, strSoftVersion, strDeviceType, strLevel,
                strModuleName, dateToString(TimeUtil.getSysTime()), strLineInfo, strInfo);
        return strReturnInfo;
        // return String.format("%23s", TimeUtil.getFormattedLocalTimeStr());

    }

    /**
     * 格式化日志输出，如2012-02-08 14:32:15.042 F[IPTVBaseActivity] L[52] [onCreate]
     * onCreate start
     *
     * @date 2012-2-28
     * @param strInfo
     *            日志信息
     * @param strLineInfo
     *            行、文件名、方法名信息
     */
    public static String formatDateString(String strInfo, String strLineInfo) {
        if (null == strInfo || null == strLineInfo) {
            return "";
        }

        // 日志格式：Login 2012-02-08 14:32:15.042
        // L[52][IPTVBaseActivity.java][onCreate] onCreate start

        String strReturnInfo = null;

        strReturnInfo = String.format("%-23s%s%s",
                dateToString(TimeUtil.getSysTime()), strLineInfo, strInfo);
        return strReturnInfo;
        // return String.format("%23s", TimeUtil.getFormattedLocalTimeStr());

    }

    public static String dateToString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     *
     * 设置是否输出日志文件到文件
     *
     * @date 2012-3-23
     * @param bWrite
     *            写还是不写
     */
    public static void setLogToFileFlag(boolean bWrite) {
        FLAG_OUTPUT_LOGINFO_TO_FILE = bWrite;
    }

    /**
     *
     * 获取当前输出日志开关
     *
     * @date 2012-3-3
     */
    public static boolean isLogToFileFlag() {
        return FLAG_OUTPUT_LOGINFO_TO_FILE;
    }

    /**
     * 设置日志文件存放路径
     *
     * @date 2012-3-23
     * @param strPath
     *            日志文件存放路径
     */
    public static void setLogFilePath(String strPath) {
        m_strSDAdress = strPath;
    }

    /**
     *
     * 获取当前输出日志路径
     *
     * @date 2012-3-3
     */
    public static String getLogFilePath() {
        return m_strSDAdress;
    }

    /**
     * 设置单个日志文件大小
     *
     * @date 2012-3-23
     * @param ilogFileSize
     *            日志文件大小，单位MB
     */
    public static void setLogFileSize(int ilogFileSize) {
        m_ilogFileSize = ilogFileSize;
    }

    /**
     *
     * 获取当前输出日志大小
     *
     * @date 2012-3-3
     */
    public static int getLogFileSize() {
        return m_ilogFileSize;
    }

    /**
     * 设置日志文件总大小
     *
     * @date 2012-3-23
     * @param ilogFileSize
     *            日志文件大小，单位MB
     */
    public static void setAllLogFileSize(int ilogFileSize) {
        miAllLogFileSize = ilogFileSize;
    }

    /**
     *
     * 获取当前输出日志限制的总大小
     *
     * @date 2012-3-3
     */
    public static int getAllLogFileSize() {
        return miAllLogFileSize;
    }

    public static void d(String strInfo) {
        d(LOG_Wizar, strInfo);
    }

    /**
     * 打印调试日志
     *
     * @date 2012-2-11
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void d(String strModuleName, String strInfo) {
        // 只有调试级别的日志才打
        if (LogLevelType.TYPE_LOG_LEVEL_DEBUG != m_typeLogLevel) {
            return;
        }

        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        if (!BuildConfig.DEBUG) {
            Log.d(strModuleName, strInfo);
            return;
        }

        String strMsg = getLineInfo(new Throwable());

        String strLogInfo = formatLogString("D", strModuleName, strInfo, strMsg);
        // String strLogInfo = formatLogString(mstrAndroidId, mstrOSVersion,
        // mstrSoftVersion, mstrDeviceType, "D", strModuleName, strInfo,
        // strMsg);
        Log.d(strModuleName, formatDateString(strInfo, strMsg));
        addLogInfoToList(strLogInfo);
        // synchronized (mstoreLogList) {
        // if (mstoreLogList.add(strLogInfo)) {
        // mstoreLogList.notify();
        // }
        // }

        // 日志格式：Login 2012-02-08 14:32:15.042
        // L[52][IPTVBaseActivity.java][onCreate] onCreate start

        // Debug版本将日志写入文件，release版本将日志输出到LogCat
        // Log.d(strModuleName,
        // formatDateString(strInfo, getLineInfo(new Throwable())));
        // if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
        // // try {
        //
        // // saveLogToLocalFileThread(formatLogString("D", strModuleName,
        // // strInfo, getLineInfo(new Throwable())));
        // // } catch (IOException e) {
        // // e.printStackTrace();
        // // }
        // }
    }

    public static void w(String strInfo) {
        w(LOG_Wizar, strInfo);
    }

    /**
     * 打印警告日志
     *
     * @date 2012-2-11
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void w(final String strModuleName, final String strInfo) {
        // 只有debug、info、warning级别的日志才打
        if (LogLevelType.TYPE_LOG_LEVEL_DEBUG != m_typeLogLevel
                && LogLevelType.TYPE_LOG_LEVEL_INFO != m_typeLogLevel
                && LogLevelType.TYPE_LOG_LEVEL_WARNING != m_typeLogLevel) {
            return;
        }

        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        if (!BuildConfig.DEBUG) {
            Log.w(strModuleName, strInfo);
            return;
        }
        String strMsg = getLineInfo(new Throwable());
        String strLogInfo = formatLogString("W", strModuleName, strInfo, strMsg);
        // String strLogInfo = formatLogString(mstrAndroidId, mstrOSVersion,
        // mstrSoftVersion, mstrDeviceType, "W", strModuleName, strInfo,
        // strMsg);
        Log.w(strModuleName, formatDateString(strInfo, strMsg));
        addLogInfoToList(strLogInfo);
    }

    /**
     * 添加日志信息到集合中
     *
     * @date 2012-2-11
     * @param strInfo
     *            日志信息
     */
    private static void addLogInfoToList(String strLogInfo) {
        if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
            synchronized (flag) {
                mstoreLogList.add(strLogInfo);
                if (mstoreLogList.size() > MAX) {
                    flag.notify();
                }
            }
        }
    }

    public static void e(String strInfo) {
        e(LOG_Wizar, strInfo);
    }

    /**
     * 打印错误日志
     *
     * @date 2012-2-11
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void e(String strModuleName, String strInfo) {
        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        if (!BuildConfig.DEBUG) {
            Log.e(strModuleName, strInfo);
            return;
        }

        // 日志格式：Login 2012-02-08 14:32:15.042
        // L[63][IPTVBaseActivity.java][onCreate] onCreate finish
        // Debug版本将日志写入文件，release版本将日志输出到LogCat
//        Log.e(strModuleName, formatDateString(strInfo, getLineInfo(new Throwable())));
        String strMsg = getLineInfo(new Throwable());
        // String strLogInfo = formatLogString(mstrAndroidId, mstrOSVersion,
        // mstrSoftVersion, mstrDeviceType, "E", strModuleName, strInfo,
        // strMsg);
        String strLogInfo = formatLogString("E", strModuleName, strInfo, strMsg);
        Log.e(strModuleName, formatDateString(strInfo, strMsg));
        addLogInfoToList(strLogInfo);
        // if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
        // // try {
        // // saveLogToLocalFileThread(formatLogString("E", strModuleName,
        // // strInfo, getLineInfo(new Throwable())));
        // // } catch (IOException e) {
        // // e.printStackTrace();
        // // }
        // }
    }

    public static void i(String strInfo) {
        i(LOG_Wizar, strInfo);
    }

    /**
     * 打印提示日志
     *
     * @date 2012-2-11
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void i(String strModuleName, String strInfo) {
        // 只有debug和info级别的日志才打
        if (LogLevelType.TYPE_LOG_LEVEL_DEBUG != m_typeLogLevel
                && LogLevelType.TYPE_LOG_LEVEL_INFO != m_typeLogLevel) {
            return;
        }

        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
//            Log.w(LOG_TAG, "strModuleName is null or strInfo is null!");
            return;
        }
        if (!BuildConfig.DEBUG) {
            Log.i(strModuleName, strInfo);
            return;
        }

        // 日志格式：Login 2012-02-08 14:32:15.042
        // L[64][IPTVBaseActivity.java][onCreate] onCreate finish
        // Debug版本将日志写入文件，release版本将日志输出到LogCat

        String strMsg = getLineInfo(new Throwable());
        String strLogInfo = formatLogString("I", strModuleName, strInfo, strMsg);
        // String strLogInfo = formatLogString(mstrAndroidId, mstrOSVersion,
        // mstrSoftVersion, mstrDeviceType, "I", strModuleName, strInfo,
        // strMsg);
        Log.i(strModuleName, formatDateString(strInfo, strMsg));
        addLogInfoToList(strLogInfo);

        // if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
        // // try {
        // // saveLogToLocalFileThread(formatLogString("I", strModuleName,
        // // strInfo, getLineInfo(new Throwable())));
        // // } catch (IOException e) {
        // // e.printStackTrace();
        // // }
        // }
    }

    /**
     * 打印异常
     *
     * @date 2013-8-15
     * @param err
     *            异常
     */
    public static void exception(Exception err) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement track : err.getStackTrace()) {
            Log.e(LOG_TAG, track.toString());
            sb.append(track.toString());
        }

        if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
            try {
                saveLogToLocalFileOriginal(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取代码行的信息，包括文件名、行号、函数名等信息
     *
     * @date 2012-2-11
     * @param ta
     *            Throwable对象
     * @return String类型，包含格式化的文件名、行号、函数名。
     */
    public static String getLineInfo(Throwable ta) {
        if (null == ta) {
            Log.w(LOG_TAG, "ta is null!");
            return null;
        }

        try {
            // 线程栈信息
            StackTraceElement element = ta.getStackTrace()[1];
            // 行号
            String strLineNumber = String.format(" L[%d]", element.getLineNumber());
            // 文件名
            String strFileName = element.getFileName();
            // 方法名
            String strMethodName = String.format("[%s]", element.getMethodName());

            // 文件名前缀（file.java得到字符串file）
            String strFileNamePostfix = null;
            // 若文件名以.java为后缀，则去除后缀
            if (strFileName.substring(
                    strFileName.length() - STR_DEFAULT_JAVA_FILE_EXT.length(),
                    strFileName.length()).equalsIgnoreCase(STR_DEFAULT_JAVA_FILE_EXT)) {
                strFileNamePostfix = strFileName.substring(0, strFileName.length()
                        - STR_DEFAULT_JAVA_FILE_EXT.length());
            } else {
                strFileNamePostfix = strFileName;
            }

            // 格式化文件名字符串
            // strFileName = null;
            strFileName = String.format(" F[%s]", strFileNamePostfix);

            // 获得格式字符串-行号、文件名、格式字符串-方法名
            // 返回值示例："F[IPTVBaseActivity]   L[51]  [onCreate]           "
            return String.format("%-23s%-7s%-22s", strFileName, strLineNumber,
                    strMethodName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Thread writeLogTask = new Thread() {

    };

    static Object flag = new Object();

    public static void saveLogToLocalFileThread() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    // 写日志开关如果关闭，则等待
                    synchronized (flag) {
                        if (!FLAG_OUTPUT_LOGINFO_TO_FILE) {
                            try {
                                flag.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    synchronized (flag) {
                        StringBuilder sb = new StringBuilder();
                        synchronized (mstoreLogList) {
                            for (Object object : mstoreLogList) {
                                sb.append((String) object + "\n");
                            }
                        }
                        if (sb.toString().length() > 0) {
                            // TODO
                            mstoreLogList.clear();
                            saveLogToLocalFileNew(sb.toString());

                        } else {
                            try {
                                flag.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    // synchronized (mstoreLogList) {
                    // try {
                    // while (mstoreLogList.size() == 0) {
                    // mstoreLogList.wait();
                    // }
                    // while (!FLAG_OUTPUT_LOGINFO_TO_FILE) {
                    // mstoreLogList.wait();
                    // }
                    // String strLogInfo = (String) mstoreLogList
                    // .removeFirst();
                    // saveLogToLocalFileNew(strLogInfo);
                    // mstoreLogList.notify();
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    //
                    // }
                }

            }
        }).start();
        // ExecutorService es = ThreadPoolMgr.getInstance().getThreadPool(
        // LOG_SAVE_FILE_THREADPOOL_NAME, 1);
        // if (null == es) {
        // LogEx.w(LOG_TAG, "saveLog Pool is null!");
        // return;
        // }
        // es.submit(new Runnable() {
        // @Override
        // public void run() {
        // try {
        // saveLogToLocalFileNew(strLogInfo);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
        // });
    }

    /**
     *
     * saveLogToLocalFileOriginal 以前的保存日志方法 日志写入本地文件，
     * 日志信息写入SD卡本地文件，如果存在指定文件则写入，不存在则创建文件后写入 限制文件大小，若超过10M，则删除文件，重新创建。
     *
     * @date 2012-2-15
     * @param strLogInfo
     *            日志信息
     * @throws IOException
     *             输入输出异常
     */
    public static void saveLogToLocalFileOriginal(String strLogInfo) throws IOException {

        if (TextUtils.isEmpty(strLogInfo)) {
            return;
        }

        if (TextUtils.isEmpty(m_strSDAdress)) {
            return;
        }

        int iReturn = 0;
        // 日志创建时间
        String strLogFileCreateTime = null;
        Date dateLogFileCreate = null;
        // Calendar类的日志创建时间及当前时间
        Calendar calOldLogFileCreateTime = null;
        Calendar calNewLogFileCreateTime = null;

        // 日志文件、备份文件
        File fileLog = null;
        File fileLogBak = null;
        // 文件输出流
        FileOutputStream outputStream = null;
        // 读文件
        BufferedReader bufReader = null;

        long lDaysBetween = 0;
        fileLog = new File(m_strSDAdress + "log-"
                + (TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMdd")));
        fileLogBak = new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME);

        iReturn = FileUtil.append2File(fileLog,
                TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMddHHmmss") + "\n");
        if (FileUtil.FILE_UTIL_STATUS_FILE_EXIST == iReturn) {
            // 日志文件存在时，检查文件大小,超过5M则删除文件，重新创建
            if ((fileLog.length() / (1024.0 * 1024.0)) > m_ilogFileSize) {
                if (fileLogBak.exists()) {
                    fileLogBak.delete();
                }
                fileLog.renameTo(new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME));

                fileLog = new File(m_strSDAdress + STR_LOG_FILE_NAME);

                FileUtil.appendContent2File(fileLog,
                        TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMddHHmmss") + "\n");

            }
            // 检查日志存在时间，超过7天则备份文件，删除上一次的备份文件（如果存在的话）。
            // 重新创建Iptv.log
            FileReader fileReader = new FileReader(m_strSDAdress + STR_LOG_FILE_NAME);
            bufReader = new BufferedReader(fileReader);
            strLogFileCreateTime = bufReader.readLine();
            if (null == strLogFileCreateTime) {
                bufReader.close();
                fileReader.close();
                return;
            }

            SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            try {
                dateLogFileCreate = sdfDateFormat.parse(strLogFileCreateTime);
            } catch (ParseException e) {
                e.printStackTrace();
                if (fileLogBak.exists()) {
                    fileLogBak.delete();
                }
                fileLog.renameTo(new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME));
                bufReader.close();
                fileReader.close();
                return;
            }

            calOldLogFileCreateTime = Calendar.getInstance();
            calOldLogFileCreateTime.set(dateLogFileCreate.getYear() + 1900,
                    dateLogFileCreate.getMonth(), dateLogFileCreate.getDate(),
                    dateLogFileCreate.getHours(), dateLogFileCreate.getMinutes(),
                    dateLogFileCreate.getSeconds());

            bufReader.close();
            fileReader.close();
            calNewLogFileCreateTime = Calendar.getInstance();
            while (calOldLogFileCreateTime.before(calNewLogFileCreateTime)
                    && lDaysBetween <= 7) {
                calOldLogFileCreateTime.add(Calendar.DAY_OF_MONTH, 1);
                lDaysBetween++;
            }
            if (lDaysBetween > 7) {
                if (fileLogBak.exists()) {
                    fileLogBak.delete();
                }
                fileLog.renameTo(new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME));
                fileLog = new File(m_strSDAdress + STR_LOG_FILE_NAME);

                FileUtil.appendContent2File(fileLog,
                        TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMddHHmmss") + "\n");

                calOldLogFileCreateTime = Calendar.getInstance();
            }
        }

        // 创建文件输出流对象
        outputStream = new FileOutputStream(fileLog, true);
        // 写日志到文件中
        outputStream.write((strLogInfo + "\n").getBytes("utf-8"));

        // 关闭文件流
        outputStream.close();
    }

    /**
     *
     * saveLogToLocalFileNew 修改后的保存日志的方法
     * 日志写入本地文件，日志信息写入SD卡本地文件，如果存在指定文件则写入，不存在则创建文件后写入 限制文件大小，若超过10M，则删除文件，重新创建。
     *
     * @date 2012-2-15
     * @param strLogInfo
     *            日志信息
     * @throws IOException
     *             输入输出异常
     */
    public static void saveLogToLocalFileNew(String strLogInfo) {

        if (TextUtils.isEmpty(strLogInfo)) {
            return;
        }

        if (TextUtils.isEmpty(m_strSDAdress)) {
            return;
        }
        try {
            recursionCreateFile(strLogInfo);
            // 创建文件输出流对象
            outputStream = new FileOutputStream(fileLog, true);
            // bufWriter=new BufferedWriter(new FileWriter(fileLog,true));
            // bufWriter.write((strLogInfo + "\n").getBytes("utf-8"));
            if (mbIsNewLogFile) {
                mstrUserName = (String) GlobalDataMgr.getInstance().getData("UserName");
                mstrSubUserName = (String) GlobalDataMgr.getInstance().getData(
                        "SubUserName");
                mstrStbUserCode = (String) GlobalDataMgr.getInstance().getData(
                        "stbusercode");
                LogEx.e(LOG_TAG, "mstrUserName=" + mstrUserName + ",mstrSubUserName="
                        + mstrSubUserName + ",mstrStbUserCode=" + mstrStbUserCode);
                outputStream.write((" [OSVersion] "
                        + mstrOSVersion + " [SoftVersion] " + mstrSoftVersion
                        + " [DeviceType] " + mstrDeviceType + " [UserName] " + mstrUserName
                        + " [SubUserName] " + mstrSubUserName + " [STBUserCode] "
                        + mstrStbUserCode + "\n").getBytes("utf-8"));
            }
            outputStream.write((strLogInfo).getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * 循环创建文件
     * <p>
     * Description: 使用递归创建文件，当前文件大于设置最大的值，则重新创建文件；
     * 按日期生成日志文件，如第一个日志文件名称为log-20140711-1,
     * 达到1M(设置的最大值)创建新的文件，名称为log-20140711-2，以
     * 此类推;另外如果不满1M(规定的大小)，则即使在新的日期也不另外生成新的日志。
     * <p>
     *
     * @date 2014年7月17日
     * @author cuiguohui 6407000220
     * @param strLogInfo
     *            打印的信息
     */
    private static void recursionCreateFile(String strLogInfo) throws IOException {
        // 获取当前时间的，如xx年xx月xx日
        String strCurTime = TimeUtil.format(TimeUtil.getSysTime(),
                STR_FORMAT_YEAR_MOUTH_DAY);
        if (mbIsContinueWrite) {
            mfilePath = m_strSDAdress + "log-" + mstrPreTime;
            mstrFilePath = mfilePath + "-" + preIndex + ".txt";
        } else {
            mfilePath = m_strSDAdress + "log-" + strCurTime;
            mstrFilePath = mfilePath + "-" + index + ".txt";
        }
        fileLog = new File(mstrFilePath);
        // 检查父路径是否存在
        if (!fileLog.getParentFile().exists()) {
            fileLog.getParentFile().mkdirs();
        }
        // 文件存在
        if (FileUtil.checkFileExist(mstrFilePath) == FileUtil.FILE_UTIL_STATUS_SUCCESS) {
            mbIsNewLogFile = false;
            // 日志文件存在时，检查文件大小,超过规定大小则重新创建
            long lTotalLenth = (fileLog.length() + strLogInfo.length());
            if (lTotalLenth > (m_ilogFileSize * (1024 * 1024))) {
                if (mbIsContinueWrite) {
                    mbIsContinueWrite = false;
                } else {
                    // 依旧是当天的
                    if (strCurTime.equals(mstrCurTime)) {
                        index++;
                    }
                    // 不是当天的或者是第一次保存文件
                    else {
                        index = 1;
                        mstrCurTime = strCurTime;
                    }
                }
                recursionCreateFile(strLogInfo);

            }
        }
        // 文件不存在
        else {
            mbIsNewLogFile = true;
            fileLog.createNewFile();
        }
    }

}
