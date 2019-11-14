/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 服务器时间和日期管理类
 * </p>
 * @Title ServerDate.java
 * @Package com.zte.iptvclient.android.operation.common
 * @version 1.0
 * @author 0043200560
 * @date 2012-4-24
 */
package com.wizarpos.log.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/** 
 * 服务器时间和日志管理类
 * @ClassName:ServerDate 
 * @Description: 服务器时间和日期管理类
 * @author: 0043200560
 * @date: 2012-4-24
 *  
 */
public class ServerDate
{
    /** 日志TAG */
    private static String TAG = ServerDate.class.getSimpleName();

    /** EPG UTC时间与本地UTC时间的时间差（毫秒） */
    private static long mlEpgTimeOffset = 0l;

    private static SimpleDateFormat msdfNormal = new SimpleDateFormat(
        "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private static SimpleDateFormat msdfOther1 = new SimpleDateFormat(
        "EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);

    /**
     * 
     * setEpgTimeOffset
     * <p>
     * Description: 计算EPG服务器时间和系统本地时间偏移量并缓存待用
     *              如果给定的EPG时间格式等错误，那么偏移量更新失败，将继续保持上一次成功更新时的值
     * <p>
     * 
     * @param epgTime EPG时间，字串UTC格式
     */
    public static void setEpgTimeOffset(String epgTime)
    {
        LogEx.d(TAG, "epgTime:" + epgTime);

        boolean bConvertSucessfully = false;
        Date dateEPG = null;
        try
        {
            synchronized (msdfNormal)
            {
                dateEPG = msdfNormal.parse(epgTime.trim());
            }
            mlEpgTimeOffset = dateEPG.getTime() - System.currentTimeMillis();

            bConvertSucessfully = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogEx.w(TAG, "epgTime foramt error: " + epgTime);
        }

        if (bConvertSucessfully)
        {
            return;
        }
        try
        {
            synchronized (msdfOther1)
            {
                dateEPG = msdfOther1.parse(epgTime.trim());
            }
            mlEpgTimeOffset = dateEPG.getTime() - System.currentTimeMillis();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogEx.w(TAG, "epgTime foramt error2: " + epgTime);
        }
    }

    /**
     * 
     * getEpgTime
     * <p>
     * Description: 获取EPG时间，此方法采用当前时间加上偏移量的形式获得。
     * <p>
     * 
     * @return 当前EPG服务器时间
     */
    public static Date getEpgTime()
    {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() + mlEpgTimeOffset);

        LogEx.d(TAG, "epgtime[LocalZone]:" + cal.getTime());

        return cal.getTime();
    }
}
