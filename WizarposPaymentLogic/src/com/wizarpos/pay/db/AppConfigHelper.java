package com.wizarpos.pay.db;

import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.Constants;

import java.util.Iterator;
import java.util.Map;

public class AppConfigHelper {

	public static Map<String, String> cacheMap;

	public static String getConfig(String configName) {
		String _value = getFormCache(configName);
		if (!TextUtils.isEmpty(_value)) {
			return _value;
		} // 先从缓存中获取数据,如果缓存中有相应数据 ,则直接返回
		try { // 缓存中没有相应数据,则从数据库中获取
			DbUtils dbUtils = PaymentApplication.getInstance().getDbController();
			if (dbUtils != null) {
				AppConfig config = dbUtils.findFirst(Selector.from(
						AppConfig.class).where("config_name", "=", configName));
				if (config == null) {
					return "";
				}// 数据库也没有相应数据,返回"";
				_value = config.getConfig_value();
				if (TextUtils.isEmpty(_value) == false) {
					updataCache(configName, _value);// 数据库中有相应数据,更新缓存中的数据并返回
					return _value;
				}
			}else {
				Log.w("AppConfigHelper", "dbUtils is null.");
				return "";
			}
			
		} catch (DbException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getConfig(String configName, String defaultValue) {
		String value = getConfig(configName);
		return TextUtils.isEmpty(value) ? defaultValue : value;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 下午4:30:42 
	 * @param configName
	 * @param defaultValue
	 * @return 
	 * @Description:根据boolean类型取值(实际还是String类型存取)
	 */
	public static boolean getConfig(String configName,boolean defaultValue)
	{
		String value = getConfig(configName);
		if(TextUtils.isEmpty(value))
		{
			return defaultValue;
		}
		if(StringUtil.isSameString(value, Constants.TRUE))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 下午4:32:15 
	 * @param configName
	 * @param value
	 * @Description:设置boolean类型
	 */
	public static void setConfig(String configName, boolean value)
	{
		if(value)
		{
			setConfig(configName, Constants.TRUE);
		}else
		{
			setConfig(configName, Constants.FALSE);
		}
	}

	public static void setConfig(String configName, String value) {
		try {
			/* 更新数据中的数据 */
			AppConfig appConfig = new AppConfig(configName, value);
			DbUtils dbUtils = PaymentApplication.getInstance().getDbController();
			if(dbUtils != null) {
				AppConfig _config = dbUtils.findFirst(
						Selector.from(AppConfig.class).where("config_name",
								"=", configName));
				if (_config == null) {
					PaymentApplication.getInstance().getDbController()
					.save(appConfig);
				} else {
					PaymentApplication
					.getInstance()
					.getDbController()
					.update(appConfig,
							WhereBuilder.b("config_name", "=", configName),
							"config_value");
				}
				/* 更新缓存中的数据 */
				updataCache(configName, value);
			} else {
				Log.w("AppConfigHelper", "dbUtils is null.");
				updataCache(configName, value);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public static void setCacheMap(Map<String, String> cacheMap) {
		AppConfigHelper.cacheMap = cacheMap;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2016-2-16 下午5:38:39  
	 * @Description: 将数据未创建前保存的数据写入数据库
	 */
	public static void restoreMap() {
		if(cacheMap == null) {
			return;
		}
		//采用Iterator遍历HashMap  
        Iterator<String> it = cacheMap.keySet().iterator();  
        while(it.hasNext()) {  
            String key = it.next(); 
            String value = cacheMap.get(key);
            setConfig(key, value);
        }  
	}

	private static String getFormCache(String configName) {
		if (cacheMap == null) {
			return null;
		}
		return cacheMap.get(configName);
	}

	private static void updataCache(String configName, String value) {
		if (cacheMap == null) {
			return;
		}
		cacheMap.put(configName, value);
	}
	
	public static boolean isSameString(String strSource, String strTarget) {
		// 都不为null
		if (null != strSource && null != strTarget)
		{
			return strSource.equals(strTarget);
		}
		// 两个都为null
		else if (null == strSource && null == strTarget)
		{
			return true;
		} else
		// 有一个为null
		{
			return false;
		}
	}

	public static void clearCache(){
		cacheMap.clear();
	}
}
