package com.wizarpos.log.util;


import android.util.Log;

public final class StringUtil {
	public static final String LOG_TAG = "StringUtil";
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @Date: 2015-4-30 上午10:30:16
	 * @Reason: 判断两个String是否相同
	 */
	public static boolean isSameString(String strSource, String strTarget) {
		// 都不为null
		if (null != strSource && null != strTarget) {
			return strSource.equals(strTarget);
		}
		// 两个都为null
		else if (null == strSource && null == strTarget) {
			return true;
		} else
		// 有一个为null
		{
			return false;
		}
	}

	public static String replaceBankCardNo(String bankCardNo){
		int length = bankCardNo.length();
		if (length < 15 || length > 19){
			Log.e(LOG_TAG, "bankCardNo length error!");
			return null;
		}
		StringBuilder tempSb = new StringBuilder();
//		String temp = "";
//		temp += bankCardNo.substring(0,5);
		tempSb.append(bankCardNo.substring(0,6));
		for (int i = 6; i < length-4; i++){
//			temp += "*";
			tempSb.append("*");
		}
//		temp += bankCardNo.substring(length-5, length-1);
		tempSb.append(bankCardNo.substring(length-4, length));
		return tempSb.toString();
	}
}