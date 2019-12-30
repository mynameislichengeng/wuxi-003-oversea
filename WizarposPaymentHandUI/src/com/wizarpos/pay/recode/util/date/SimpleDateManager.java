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
}
