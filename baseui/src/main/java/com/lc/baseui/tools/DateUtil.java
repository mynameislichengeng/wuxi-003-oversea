package com.lc.baseui.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by licheng on 2017/5/21.
 */

public class DateUtil {

    /**
     * 求一个月最后一天的日期
     **/
    public static String getMonthEndDate() {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        //
        String str = sf.format(date);
        String datas[] = str.split("-");
        //
        int monthnumsDay = getMonthDayNumbers();
        String end = datas[0] + "-" + datas[1] + "-" + String.valueOf(monthnumsDay);
        return end;
    }

    /**
     * 求一个月第一天日期
     **/
    public static String getMonthStartDate() {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        //
        String str = sf.format(date);
        String datas[] = str.split("-");
        String start = datas[0] + "-" + datas[1] + "-" + "01";
        return start;
    }

    /**
     * 求一个月的天数
     **/
    public static int getMonthDayNumbers() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        int monthnumsDay = cal.get(Calendar.DAY_OF_MONTH);
        return monthnumsDay;
    }
}
