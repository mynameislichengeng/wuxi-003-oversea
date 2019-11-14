package com.wizarpos.atool.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @ClassName: CommonUtil
 * @author Huangweicai
 * @date 2015-8-31 下午4:06:47
 * @Description: 常用工具类
 */
public class CommonUtil {
	private final static String LOG_TAG = CommonUtil.class.getName();
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-8-31 下午4:07:21 
	 * @Description: 判断APK是否存在
	 */
	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
