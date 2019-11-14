package com.wizarpos.atool.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * yyyy-MM-dd
     */
    public static final String P1 = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String P2 = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy/MM/dd
     */
    public static final String P3 = "yyyy/MM/dd";
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    public static final String P4 = "yyyy/MM/dd HH:mm:ss";
    /**
     * yyyy/MM
     */
    public static final String P5 = "yyyy/MM";
    /**
     * MM/dd
     */
    public static final String P6 = "MM/dd";
    /**
     * HH:mm
     */
    public static final String P7 = "HH:mm";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String P8 = "yyyy-MM-dd HH:mm";
    /**
     * MM-dd HH:mm:ss
     */
    public static final String P9 = "MM-dd HH:mm:ss";
    /**
     * yyyyMMddHHmmss
     */
    public static final String P10 = "yyyyMMddHHmmssSSS";
    /**
     * MM-dd HH:mm
     */
    public static final String P11 = "MM-dd HH:mm";
    /**
     * HH:mm:ss
     */
    public static final String P12 = "HH:mm:ss";

    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(date);
    }

    public static String format(long milliSeconds, String pattern) {
        return format(new Date(milliSeconds), pattern);
    }

    public static Date setTimeOfDay(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 加 天
     *
     * @param currentTime
     * @param day
     * @return
     */
    public static Date addDay(Date currentTime, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 加 分
     *
     * @param currentTime
     * @param minute
     * @return
     */
    public static Date addMinute(Date currentTime, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 天 开始
     *
     * @param currentTime
     * @return
     */
    public static Date getDayStart(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar.getTime();
    }

    /**
     * 天 结束
     *
     * @param currentTime
     * @return
     */
    public static Date getDayEnd(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 今天开始
     *
     * @return
     */
    public static Date getDayStart() {
        return getDayStart(new Date());
    }

    /**
     * 今天结束
     *
     * @return
     */
    public static Date getDayEnd() {
        return getDayEnd(new Date());
    }

    public static Date addWeek(Date currentTime, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return calendar.getTime();
    }

    /**
     * 一周中，一天的索引
     *
     * @param dayOfWeek
     * @return
     */
    public static int getIndexForDayOfWeek(int dayOfWeek) {
        if (dayOfWeek == 1) {
            return 6;
        } else {
            return dayOfWeek - 2;
        }
    }

    /**
     * 一周的开始
     *
     * @param currentTime
     * @return
     */
    public static Date getWeekStart(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -getIndexForDayOfWeek(dayOfWeek));
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar.getTime();
    }

    /**
     * 一周的结束
     *
     * @param currentTime
     * @return
     */
    public static Date getWeekEnd(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, 6 - getIndexForDayOfWeek(dayOfWeek));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 月 加
     *
     * @param currentTime
     * @param amount
     * @return
     */
    public static Date addMonth(Date currentTime, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }

    /**
     * 月开始
     *
     * @param currentTime
     * @return
     */
    public static Date getMonthStart(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar.getTime();
    }

    /**
     * 月结束
     *
     * @param currentTime
     * @return
     */
    public static Date getMonthEnd(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, count - dayOfMonth - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static boolean isToday(Date date) {
        Date currentTime = new Date();
        Date startDate = getDayStart(currentTime);
        Date endDate = getDayEnd(currentTime);
        if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
            return true;
        }
        return false;
    }

    public static Date addMillSeconds(Date currentTime, long amount) {
        return new Date(currentTime.getTime() + amount);
    }

    public static Date addYear(Date currentTime, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.YEAR, amount);
        return calendar.getTime();
    }

    public static Date getYearStart(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar.getTime();
    }

    public static Date getYearEnd(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static TimeRange getTimeRange(int p, Date currentTime) {
        TimeRange range = new TimeRange();
        switch (p) {
            case 1: // 今天
                range.setBeginTime(getDayStart());
                range.setEndTime(getDayEnd());
                return range;
            case 2: // 昨天
                Date yesterday = addDay(currentTime, -1);
                range.setBeginTime(getDayStart(yesterday));
                range.setEndTime(getDayEnd(yesterday));
                return range;
            case 3: // 本周
                range.setBeginTime(getWeekStart(currentTime));
                range.setEndTime(getWeekEnd(currentTime));
                return range;
            case 4: // 上周
                Date lastWeek = addWeek(currentTime, -1);
                range.setBeginTime(getWeekStart(lastWeek));
                range.setEndTime(getWeekEnd(lastWeek));
                return range;
            case 5: // 本月
                range.setBeginTime(getMonthStart(currentTime));
                range.setEndTime(getMonthEnd(currentTime));
                return range;
            case 6: // 上月
                Date lastMonth = addMonth(currentTime, -1);
                range.setBeginTime(getMonthStart(lastMonth));
                range.setEndTime(getMonthEnd(lastMonth));
                return range;
            case 7: // 前两个月
                Date twoMonth = addMonth(currentTime, -2);
                range.setBeginTime(getDayStart(twoMonth));
                range.setEndTime(getDayEnd(currentTime));
                return range;
            case 8: // 前三个月
                Date threeMonth = addMonth(currentTime, -3);
                range.setBeginTime(getDayStart(threeMonth));
                range.setEndTime(getDayEnd(currentTime));
            case 9: // 前六个月
                Date sixMonth = addMonth(currentTime, -6);
                range.setBeginTime(getDayStart(sixMonth));
                range.setEndTime(getDayEnd(currentTime));
            case 10: // 前一年
                Date lastYear = addYear(currentTime, -1);
                range.setBeginTime(getDayStart(lastYear));
                range.setEndTime(getDayEnd(currentTime));
                return range;
            case 11: // 前三年
                Date threeYear = addYear(currentTime, -3);
                range.setBeginTime(getDayStart(threeYear));
                range.setEndTime(getDayEnd(currentTime));
                return range;

            default:
                range.setBeginTime(getDayStart());
                range.setEndTime(getDayEnd());
                return range;
        }
    }

    public static class TimeRange {
        public Date beginTime;
        public Date endTime;

        public Date getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Date beginTime) {
            this.beginTime = beginTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
    }

    public static String formatInternationalDate(Date date) {
        return formatInternationalDate(date, DateFormat.FULL);
    }

    public static String formatInternationalDate(String dateTime) {
        return formatInternationalDate(dateTime, DateFormat.FULL);
    }

    public static String formatInternationalDate(String dateTime, int style) {
        try {
            DateFormat format = new SimpleDateFormat(P2);
            Date parse = format.parse(dateTime);
            return formatInternationalDate(parse, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatInternationalDate(Date date, int style) {
        try {
            final DateFormat dateInstance = DateFormat.getDateInstance(style, new Locale("en", "US"));
            return dateInstance.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
