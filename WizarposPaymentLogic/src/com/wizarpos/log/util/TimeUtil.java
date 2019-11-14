/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 时间工具类实现文件
 * </p>
 * @Title TimeUtil.java
 * @Package com.zte.iptvclient.android.common
 * @version 1.0
 * @author zhang.longfei 10126853
 * @date 2012-02-11
 */

/** IPTV客户端Android通用功能包  */
package com.wizarpos.log.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import android.text.TextUtils;



/**
 * 时间工具类，汇集时间获取、时间转换时间处理函数
 * @ClassName:TimeUtil 
 * @Description: 汇集时间获取、时间转换时间处理函数
 * @author: zhang.longfei 10126853
 * @date: 2012-02-06
 *
 */
public final class TimeUtil
{
    /** 日志TAG */
    private static final String STR_LOG_TAG = TimeUtil.class.getSimpleName();
    /** Locale ID:UTC */
    private static final String STR_LOCALE_UTC = "UTC";

    /** 进行日期计算时可用的单位 */
    /** YEAR,等价于Calendar.YEAR */
    public static final int UNIT_YEAR = Calendar.YEAR;
    /** MONTH,等价于Calendar.MONTH */
    public static final int UNIT_MONTH = Calendar.MONTH;
    /** WEEK,等价于Calendar.WEEK_OF_YEAR */
    public static final int UNIT_WEEK = Calendar.WEEK_OF_YEAR;
    /** DATE,等价于Calendar.DATE */
    public static final int UNIT_DATE = Calendar.DATE;
    /** HOUR,等价于Calendar.HOUR_OF_DAY */
    public static final int UNIT_HOUR = Calendar.HOUR_OF_DAY;
    /** MINUTE,等价于Calendar.MINUTE */
    public static final int UNIT_MINUTE = Calendar.MINUTE;
    /** SECOND,等价于Calendar.SECOND */
    public static final int UNIT_SECOND = Calendar.SECOND;
    /** MILLISECOND,等价于Calendar.MILLISECOND */
    public static final int UNIT_MILLISECOND = Calendar.MILLISECOND;

    /** 字符串型日期的时区指示标识 */
    /** 表明日期字符串所在时区为UTC */
    public static final int INT_ZONEFLAG_UTC = 0;
    /** 表明日期字符串所在时区为服务器本地 */
    public static final int INT_ZONEFLAG_LOCAL = 1;

    /** 服务器时区 */
    static TimeZone mServerTimeZone = TimeZone.getDefault();
    /** 地区 */
    static Locale mLocale = Locale.getDefault();

    /**
     * 不允许创建实例，隐藏构造函数
     */
    private TimeUtil()
    {
    }

    /**
     * 
     * @Deprecated
     * <p>
     * Description: 获取服务器时区,一般是EPG的本地时区.一般无需使用
     * <p>
     * 
     * @return 服务器时区
     */
    @Deprecated
    public static TimeZone getServerTimeZone()
    {
        return mServerTimeZone;
    }

    /**
     * 
     * setDSTRules
     * <p>
     * Description: 设置DaylightSaving规则
     *              要设置一个带有夏令时安排的一组规则，用开始规则 和结束规则来描述该安排。夏令时开始或结束的那一天通过 month、day-of-month 和 day-of-week 值联合指定。
     *              month 值由 Calendar 的 MONTH 字段值表示，如 Calendar.MARCH。day-of-week 值由 Calendar 的 DAY_OF_WEEK 值表示，如 SUNDAY。
     *              这些值的组合含意如下。 

                                                           ※  一个月的具体某一天
                                                                 要指定一个月的具体某一天，可将 month 和 day-of-month 设置为一个具体的值，并将 day-of-week 设置为 0。
                                                                 例如，要指定 3 月 1 日，可将 month 设置为 MARCH，day-of-month 设置为 1，并将 day-of-week 设置为 0。 
                                                           ※  一个月的某一天或之后的星期几
                                                                 要具体指定一个月的某一天或之后的星期几，可将 month 设置为一个具体的月份值，将 day-of-month 设置为在该日期或在该日期之后应用规则的那一天，并将 day-of-week 设置为负的 DAY_OF_WEEK 字段值。
                                                                 例如，要指定 4 月的第二个星期天，可将 month 设置为 APRIL，day-of-month 设置为 8，并将 day-of-week 设置为 -SUNDAY。 
                                                           ※  一个月的某一天或之前的星期几
                                                                要具体指定一个月的某一天或之前的星期几，可将 day-of-month 和 day-of-week 设置为负值。
                                                                例如，要指定 3 月 21 日或之前的最后一个星期三，可将 month 设置为 MARCH，将 day-of-month 设置为 -21，并将 day-of-week 设置为 -WEDNESDAY。 
                                                           ※  一个月的最后一个星期几
                                                                 要指定一个月的最后一个星期几，可将 day-of-week 设置为 DAY_OF_WEEK 值，并将 day-of-month 设置为 -1。
                                                                 例如，要设置 10 月的最后一个星期日，可将 month 设置为 OCTOBER，将 day-of-week 设置为 SUNDAY，并将 day-of-month 设置为 -1。 

     * <p>
     * 
     * @param rawOffset
     *          the given base time zone offset to GMT.
     * @param ID
     *          the time zone ID which is obtained from TimeZone.getAvailableIDs.
     * @param startMonth
     *           the daylight savings starting month. Month is 0-based. eg, 0 for January.
     * @param startDay
     *           the daylight savings starting day-of-week-in-month.
     * @param startDayOfWeek
     *           the daylight savings starting day-of-week.
     * @param startTime
     *           the daylight savings starting time in local wall time, which is standard time in this case.
     * @param endMonth
     *           the daylight savings ending month. Month is 0-based. eg, 0 for January.
     * @param endDay
     *           the daylight savings ending day-of-week-in-month.
     * @param endDayOfWeek
     *           the daylight savings ending day-of-week.
     * @param endTime
     *           the daylight savings ending time in local wall time, which is daylight time in this case.
     * @param dstSavings
     *           the daylight savings time difference in milliseconds.
     */
    public static void setDSTRules(int rawOffset, String ID, int startMonth,
            int startDay, int startDayOfWeek, int startTime, int endMonth, int endDay,
            int endDayOfWeek, int endTime, int dstSavings)
    {
        try
        {
            mServerTimeZone = new SimpleTimeZone(rawOffset, ID, startMonth, startDay,
                startDayOfWeek, startTime, endMonth, endDay, endDayOfWeek, endTime,
                dstSavings);
        }
        catch (Exception e)
        {
            LogEx.w(
                    STR_LOG_TAG,
                    "If the month, day, dayOfWeek, or time parameters are out of range for the start or end rule. ["
                            + rawOffset
                            + ","
                            + ID
                            + ","
                            + startMonth
                            + ","
                            + startDay
                            + ","
                            + startDayOfWeek
                            + ","
                            + startTime
                            + ","
                            + endMonth
                            + ","
                            + endDay
                            + "," + endDayOfWeek + "," + endDayOfWeek + "," + endDayOfWeek + "]");

            try
            {
                int hour = Math.abs(rawOffset) / 3600000;
                int minute = (Math.abs(rawOffset) - hour * 3600000) / 60000;
                mServerTimeZone = TimeZone.getTimeZone("GMT"
                    + (rawOffset > 0 ? "+" : "-") + hour
                    + ":" + (minute > 9 ? minute : minute));
            }
            catch (Exception er)
            {
                if (null != ID && !"".equals(ID))
                {
                    try
                    {
                        mServerTimeZone = TimeZone.getTimeZone(ID);
                    }
                    catch (Exception err)
                    {
                        mServerTimeZone = TimeZone.getDefault();
                    }
                }
                else
                {
                    mServerTimeZone = TimeZone.getDefault();
                }
            }
        }
        
        LogEx.d(STR_LOG_TAG, "Server TimeZone:" + mServerTimeZone.toString());
    }

    /**
     * 
     * setLanguage
     * <p>
     * Description: 设置显示语言
     *              语言参数是一个有效的 ISO 语言代码。这些代码是由 ISO-639 定义的小写两字母代码。
     * <p>
     * 
     * @param language
     *              语言，小写的两字母 ISO-639 代码。
     */
    public static void setLanguage(String language)
    {
        if (null != language)
        {
            for (String lang : Locale.getISOLanguages())
            {
                if (lang.equals(language))
                {
                    mLocale = new Locale(language);
                    return;
                }
            }
        }

        LogEx.w(STR_LOG_TAG, "Parameter[language] not valid!" + language);
    }

    /**
     * getSysTime
     * <p>
     * Description: 取系统本地日历设置的时间
     * <p>
     * 
     * @return 系统本地日历时间
     */
    public static Date getSysTime()
    {
        return Calendar.getInstance().getTime();
    }

    /**
     * getNow
     * <p>
     * Description: 取当前服务器时间，默认当前本地时间，时区对应EPG服务器时区
     * <p>
     * 
     * @return 服务本地时间
     */
    public static Date getNow()
    {
        Calendar cdr = Calendar.getInstance();
        cdr.setTimeZone(mServerTimeZone);
        cdr.setTime(ServerDate.getEpgTime());

        return cdr.getTime();
    }

    /**
     * getDate
     * <p>
     * Description: 通过输入时间字符串、指定格式及时区 转换成日期型
     * <p>
     * 
     * @param strDate 待转的时间字符串
     * @param strFormat 待转时间字符串的格式
     * @param iFlag 传入时间的时区标志: INT_ZONEFLAG_UTC - UTC; INT_ZONEFLAG_LOCAL - EPG服务器时区
     * @return 对应字符型的日期型时间
     */
    public static Date getDate(String strDate, String strInFormat, int iFlag)
    {
        if (TextUtils.isEmpty(strDate) || TextUtils.isEmpty(strInFormat))
        {
            return null;
        }

        Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(strInFormat, mLocale);
        switch (iFlag)
        {
            case TimeUtil.INT_ZONEFLAG_UTC :
            {
                sdf.setTimeZone(TimeZone.getTimeZone(TimeUtil.STR_LOCALE_UTC));
                break;
            }
            case TimeUtil.INT_ZONEFLAG_LOCAL :
            {
                sdf.setTimeZone(mServerTimeZone);
                break;
            }
            default :
            {
                break;
            }
        }

        try
        {
            d = sdf.parse(strDate);
        }
        catch (ParseException e)
        {
            LogEx.d(STR_LOG_TAG, strDate + " parse error! Format:" + strInFormat + " "
                    + e.getMessage());
        }

        return d;
    }

    /**
     * addOffset
     * <p>
     * Description: 取一个偏差时间
     * <p>
     * 
     * @param date 基准时间
     * @param iUnit 单位,入参限制Calendar单位.参见TimeUti常量
     * @param iOffset 时差
     * @return
     */
    public static Date addOffset(Date date, int iUnit, int iOffset)
    {
        if (null == date)
        {
            return null;
        }

        Calendar cdr = Calendar.getInstance();
        cdr.setTime(date);
        cdr.add(iUnit, iOffset);

        return cdr.getTime();
    }

    /**
     * compare
     * <p>
     * Description: 时间比较
     * <p>
     * 
     * @param date 基准时间
     * @param dateAnother 比较时间
     * @return -2:基准时间null; -1:早; 0:等; 1:晚; 2:比较时间null
     */
    public static int compare(Date date, Date dateAnother)
    {
        if (null == date)
        {
            return -2;
        }

        if (null == dateAnother)
        {
            return 2;
        }

        return date.compareTo(dateAnother);
    }

    /**
     * 
     * calcOffset
     * <p>
     * Description: 计算时间相差的毫秒数
     * <p>
     * 
     * @param date 基准时间
     * @param dateAnother 比较时间
     * @return 时差
     * @throws NullPointerException
     */
    public static long calcOffset(Date date, Date dateAnother)
            throws NullPointerException
    {
        return date.getTime() - dateAnother.getTime();
    }

    /**
     * formatUTC
     * <p>
     * Description: 格式化UTC时间
     * <p>
     * 
     * @param date 待转日期
     * @param strOutFormat 转换后格式
     * @return 格式化的字符型日期
     *         转换失败,返回""
     */
    public static String formatUTC(Date date, String strOutFormat)
    {
        if (null == date || TextUtils.isEmpty(strOutFormat))
        {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(strOutFormat, mLocale);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeUtil.STR_LOCALE_UTC));
        return sdf.format(date);
    }

    /**
     * format
     * <p>
     * Description: 格式化本地时间
     * <p>
     * 
     * @param date 待转日期
     * @param strOutFormat 转换后格式
     * @return 格式化的字符型日期
     *         转换失败,返回""
     */
    public static String format(Date date, String strOutFormat)
    {
        if (null == date || TextUtils.isEmpty(strOutFormat))
        {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(strOutFormat, mLocale);
        sdf.setTimeZone(mServerTimeZone);
        return sdf.format(date);
    }
}
