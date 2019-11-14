package com.wizarpos.pay.common.base;

import android.content.Context;

import com.wizarpos.base.net.Response;

/**
 * 控制类基类
 * 
 * @author wu
 */
public class BasePresenter {

	protected Context context;

	public interface ResultListener {

		void onSuccess(Response response);

		void onFaild(Response response);
	}

	public BasePresenter(Context context) {
		this.context = context;
		init();
	}

	private void init() {
	}

	protected void showDialog() {

	}

	protected void dismissDialog() {

	}

	// protected void addCache(String key, Object value) {// 简化调用
	// PaymentApplication.getInstance().addCache(key, value);
	// }
	//
	// protected Object getCache(String key) {// 简化调用
	// return PaymentApplication.getInstance().getCache(key);
	// }
	//
	// protected Object getCache(String key, String defaultValue) {
	// Object obj = getCache(key);
	// if (obj != null) { return obj; }
	// return defaultValue;
	// }

}
