package com.wizarpos.pay.db;

import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.pay.app.PaymentApplication;

import java.util.Iterator;
import java.util.Map;

public class AppStateManager {

	public static Map<String, String> stateMap;

	public static String getState(String stateName) {
		String _value = getFormCache(stateName);
		if (!TextUtils.isEmpty(_value)) {
			return _value;
		} // 先从缓存中获取数据,如果缓存中有相应数据 ,则直接返回
		try { // 缓存中没有相应数据,则从数据库中获取
			DbUtils dbUtils = PaymentApplication.getInstance().getDbController();
			if(dbUtils != null) {
				AppState state = dbUtils.findFirst(
						Selector.from(AppState.class).where("state_name",
								"=", stateName));
				if (state == null) {
					return "";
				}// 数据库也没有相应数据,返回"";
				_value = state.getState_value();
				if (TextUtils.isEmpty(_value) == false) {
					updataCache(stateName, _value);// 数据库中有相应数据,更新缓存中的数据并返回
					return _value;
				}
			}else {
				Log.w("AppStateManager", "dbUtils is null.");
				return "";
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getState(String stateName, String defaultValue) {
		String value = getState(stateName);
		return TextUtils.isEmpty(value) ? defaultValue : value;
	}

	public static void setState(String stateName, String value) {
		try {
			/* 更新数据中的数据 */
			AppState appState = new AppState(stateName, value);
			DbUtils dbUtils = PaymentApplication.getInstance().getDbController();
			if(dbUtils !=  null) {
				AppState _state = dbUtils.findFirst(
						Selector.from(AppState.class).where("state_name",
								"=", stateName));
				if (_state == null) {
					PaymentApplication.getInstance().getDbController()
					.save(appState);
				} else {
					PaymentApplication
					.getInstance()
					.getDbController()
					.update(appState,
							WhereBuilder.b("state_name", "=", stateName),
							"state_value");
				}
			}else {
				Log.w("AppStateManager", "dbUtils is null.");
			}
			/* 更新缓存中的数据 */
			updataCache(stateName, value);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public static void setCacheMap(Map<String, String> cacheMap) {
		AppStateManager.stateMap = cacheMap;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2016-2-16 下午5:38:39  
	 * @Description: 将数据未创建前保存的数据写入数据库
	 */
	public static void restoreMap() {
		if(stateMap == null) {
			return;
		}
		//采用Iterator遍历HashMap  
        Iterator<String> it = stateMap.keySet().iterator();  
        while(it.hasNext()) {  
            String key = it.next(); 
            String value = stateMap.get(key);
            setState(key, value);
        }  
	}

	private static String getFormCache(String configName) {
		if (stateMap == null) {
			return null;
		}
		return stateMap.get(configName);
	}

	private static void updataCache(String configName, String value) {
		if (stateMap == null) {
			return;
		}
		stateMap.put(configName, value);
	}

	public static void clearCache(){
		stateMap.clear();
	}
}
