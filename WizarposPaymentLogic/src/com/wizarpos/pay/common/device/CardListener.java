package com.wizarpos.pay.common.device;

import android.os.Handler;

import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

/**
 * 刷卡
 * 
 * @author wu
 */
public interface CardListener {

	/**
	 * 获取二磁道信息
	 */
	void getTrack2(Handler handler, ResultListener listener);


	void setSwipeCardListener(Handler handler, ResultListener listener);

	void close();
}
