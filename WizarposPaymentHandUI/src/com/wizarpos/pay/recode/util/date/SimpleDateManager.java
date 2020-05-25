package com.wizarpos.pay.recode.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateManager {

    private static final String YYYY_MMM_DD = "yyyy-MM-dd";
    private static final String YYYY_MMM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String toStrYYYYMMDD(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MMM_DD);
        String str = simpleDateFormat.format(new Date(time));
        return str;
    }


    public static Date fromYYYYMMDDHHMMSS(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MMM_DD_HH_MM_SS);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     *
     * @param month
     * @return
     */
    public static String getMonthEnglish(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "";
        }
    }
}
