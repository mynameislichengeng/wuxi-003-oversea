/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description:用于获取设备唯一ID
 * </p>
 * @Title AndroidUniqueCode.java
 * @Package com.zte.iptvclient.android.common
 * @version 1.0
 * @author jamesqiao10065075
 * @date 2012-3-10
 */
package com.wizarpos.log.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


/** 
 * 用于获取设备唯一ID
 * @ClassName:AndroidUniqueCode 
 * @Description: 用于获取设备唯一ID
 * @author: jamesqiao10065075
 * @date: 2012-3-10
 */
public final class AndroidUniqueCode
{
    /** 日志标签*/
    private static final String LOG_TAG = "AndroidUniqueCode";

    /** 存放到文件中的androidId*/
    private final static String ANDROID_ID = "AndroidId";

    /** 存放到文件中的MacAddress*/
    private final static String MAC_ADDRESS = "MacAddress";

    /**
     * 
     * 获取设备IMEI号
     * <p>
     * Description: 获取设备IMEI号
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @param ctx 上下文
     * @return 设备IMEI号
     */
    public static String getDeviceIMEI(Context ctx)
    {
        if (null == ctx)
        {
            LogEx.w(LOG_TAG, "null== ctx");
            return null;
        }

        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String strDeviceId = telephonyManager.getDeviceId();

        LogEx.d(LOG_TAG, "strDeviceId=" + strDeviceId);

        return strDeviceId;
    }

    /**
     * 
     * 获取CPU序列号
     * <p>
     * Description: 获取CPU序列号
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @return CPU序列号
     */
    public static String getCPUSerial()
    {
        String strCurrentLine = null;
        try
        {
            //读取CPU信息     
            Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream inputStream = process.getInputStream();
            if (null == inputStream)
            {
                LogEx.w(LOG_TAG, "getInputStream is null");
                return null;
            }
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            LineNumberReader lineNumberReader = new LineNumberReader(inputReader);
            String strCPUSerial = null;
            //查找CPU序列号 
            while ((strCurrentLine = lineNumberReader.readLine()) != null)
            {
                //查找到序列号所在行   
                if (strCurrentLine.indexOf("Serial") > -1)
                {
                    //提取序列号   
                    strCPUSerial = strCurrentLine.substring(
                        strCurrentLine.indexOf(':') + 1, strCurrentLine.length());
                    if (!TextUtils.isEmpty(strCPUSerial)
                        && !TextUtils.isEmpty(strCPUSerial.trim()))
                    {
                        //去空格
                        strCPUSerial = strCPUSerial.trim();
                    }
                    break;
                }
            }

            inputReader.close();
            inputStream.close();

            return strCPUSerial;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            LogEx.w(LOG_TAG, "Exception occured:" + ex.getMessage());
            return null;
        }

    }


}
