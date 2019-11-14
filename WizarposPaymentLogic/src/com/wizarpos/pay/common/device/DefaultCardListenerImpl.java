package com.wizarpos.pay.common.device;

import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.util.POSFunction;
import com.wizarpos.atool.util.POSFunction.MSRCallback;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;

/**
 * 刷卡实现
 * 
 * @author wu
 */
public class DefaultCardListenerImpl implements CardListener {

	private POSFunction posFunction = new POSFunction();

	@Override
	public void getTrack2(final Handler handler, final ResultListener listener) {
		setSwipeCardListener(handler, new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				JSONArray cardArr = (JSONArray) response.getResult();
				String secondChannel = cardArr.getString(1);
				if (TextUtils.isDigitsOnly(secondChannel)) {// 纯数字,直接返回
					listener.onSuccess(new Response(0, "success", secondChannel));
				} else if (secondChannel.contains("=")) {// 银行卡的磁道二磁道信息有"="
					String cardNum = secondChannel.substring(0, secondChannel.indexOf("="));
					if (!TextUtils.isEmpty(cardNum)) {
						listener.onSuccess(new Response(0, "success", cardNum));
					} else {
						listener.onFaild(new Response(1, "获取磁条卡解析失败"));
					}
				}
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(new Response(1, "磁条卡信息解析失败"));
			}
		});
	}

	@Override
	public void setSwipeCardListener(final Handler handler, final ResultListener listener) {
		posFunction.setMSRCallback(new MSRCallback() {

			@Override
			public void msrCallback(final int code, final Object data) {
				try {
					posFunction.msrCancel();
					handler.post(new Runnable() {// 回调跑到子线程去了

						@Override
						public void run() {
							if (code == 0) {
								JSONArray cardArr = (JSONArray) data;
								String secondChannel = cardArr.getString(1);
								if (TextUtils.isEmpty(secondChannel)) {
									listener.onFaild(new Response(1, "数据获取失败"));
									Logger2.debug("磁条卡信息解析失败");
									return;
								}
								listener.onSuccess(new Response(0, "success", data));
							} else {
								listener.onFaild(new Response(1, "磁条卡信息解析失败"));
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
		posFunction.msrOpen();// 调用刷卡
	}

	@Override
	public void close() {
		try {
			 posFunction.msrCancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
