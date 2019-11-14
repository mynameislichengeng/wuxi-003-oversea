package com.wizarpos.pay.common.utils;

import com.wizarpos.log.util.LogEx;

/**
 * 替换atool中的log框架,使用huangweicai的log框架
 */
public class Logger2 {

	public static void debug(String msg) {
		LogEx.d("payment", msg);
	}

}
