package com.wizarpos.pay.view.util;

import android.util.Log;

/**
 * 
 * @ClassName: com.wizarpos.pay.view.util 
 * @Author Huangweicai  
 * @date 2015-10-12 上午10:56:59
 * @Description: (用一句话描述该文件做什么)
 */
public class StringUtilUI {
	private final static String LOG_TAG = StringUtilUI.class.getName();
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-10-12 上午10:52:46 
	 * @param length 需要拷贝的倍数
	 * @param copyChar 拷贝字符串
	 * @return 
	 * @Description: 用一句话描述这个变量表示什么
	 */
	public static String copyChar(int length,String copyChar)
	{
//		Log.d(LOG_TAG, "copayChar length is :" + length + "  copyChar is :" + copyChars);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++)
		{
			sb.append(copyChar);
		}
		return sb.toString();
	}
}
