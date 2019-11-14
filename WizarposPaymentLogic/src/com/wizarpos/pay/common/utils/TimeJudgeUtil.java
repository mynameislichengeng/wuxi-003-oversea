package com.wizarpos.pay.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间判断类
 * Created by Song on 2016/6/29.
 */
public class TimeJudgeUtil {

    /**
     * 本周内首次登陆
     * @return
     */
    public static boolean isWeeklyFirst(long lastTime){
        try {
            SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
            if (lastTime <= getCurrentMonday().getTime()){
                return true;
            }
            return false;
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获取本周日23点59分59秒
     * @return
     */
    public static Date getCurrentSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        Date sunday = currentDate.getTime();
        return sunday;
    }

    /**
     * 获取本周一0点
     * @return
     */
    public static Date getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        Date monday = currentDate.getTime();
        return monday;
    }
}
