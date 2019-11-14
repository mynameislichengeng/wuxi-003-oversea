package com.wizarpos.pay.cashier.pay_tems.wepay;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;

/**
 * 请求微信会员卡二维码 Created by wu on 2015/5/11 0011.
 */
public class WepayQRCodeRequest extends HandlerThread {

	public WepayQRCodeRequest() {
		super("WepayQRCodeRequest");
	}

	private String ticketId;

	private ResponseListener listener;

	public void setListener(ResponseListener listener) {
		this.listener = listener;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	private String fileName;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				listener.onSuccess(new Response(0, "success", msg.obj));
			} else {
				listener.onFaild(new Response(1, "请求微信二维码发生异常"));
			}
		}
	};

	@Override
	protected void onLooperPrepared() {
		super.onLooperPrepared();
		fileName = "";
		handler.sendEmptyMessage(1);
		return;
	}

}
