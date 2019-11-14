package com.wizarpos.pay.common.utils;

/**
 * 
 * @Author: Huangweicai
 * @date 2016-2-1 下午1:25:01
 * @Description:正则验证工具类
 */
public class RegexpUtils {
	
	/**
     * 验证是否为纯数字卡号
     */
    public static boolean isAllNumberCard (String str) {
    		String cardNoRegex = "^[0-9]*$";
    		return str.matches(cardNoRegex);
    }
	
}
