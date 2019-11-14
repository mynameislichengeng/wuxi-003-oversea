package com.wizarpos.pay.cashier.pay_tems;

import android.os.Handler;
import android.os.Message;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.utils.Logger2;

/**
 * 轮询 Created by wu on 2015/6/26 0026.
 */
public abstract class LooperTask {

	private long totalTime = 10 * 60 * 1000;// 最长轮询时间
	private int loopMaxTime = 800; // 最大轮询次数
	private int loopTime = 0;

	private static final int ACTION_QUERY = 999;
	private static final int ACTION_FINISH = 998;
	private static final int DELAY_TIME = 3 * 1000;

	private long stopLoopTime; // 停止轮询时间

	public interface LooperFinishListener {

		void onFinish(Object object);
	}

	private LooperFinishListener listener;

	public void setListener(LooperFinishListener listener) {
		this.listener = listener;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ACTION_QUERY:
				loop();
				break;
			case ACTION_FINISH:
				if (listener != null) {
					listener.onFinish(msg.obj);
				}
				break;
			}
		}
	};

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			doTask();
		}
	};

	protected final void goon() {
		if (handler != null) {
			handler.sendEmptyMessage(ACTION_QUERY);
		}
	}

	protected final void finish(Object object) {
		if(handler != null){
			Message msg = handler.obtainMessage();
			msg.what = ACTION_FINISH;
			msg.obj = object;
			handler.sendMessage(msg);
		}
	}

	protected abstract void doTask();

	public void start() {
		if (loopTime != 0) { throw new IllegalStateException("无法重复调用"); }
		stopLoopTime = System.currentTimeMillis() + totalTime;
		loop();
	}

	private void loop() {
		if (isOutTotalTime()) {
			LogEx.d("loopTask", "超过最长轮询时间,停止轮询");
			loopOutTime();
		} else if (loopTime >= loopMaxTime) {
			LogEx.d("loopTask", "超过最大轮询次数,停止轮询");
			loopOutTime();
		} else {
			loopTime++;
			Logger2.debug("第" + loopTime + "次轮询");
			handler.postDelayed(runnable, DELAY_TIME);
		}
	}

	/**
	 * 轮询超时
	 */
	private void loopOutTime() {
		if(handler != null){
			Message msg = handler.obtainMessage();
			msg.what = ACTION_FINISH;
			msg.obj = new Response(1, "订单超时");
			handler.sendMessage(msg);
		}
		onDestory();
	}

	/**
	 * 释放操作，必须调用
	 */
	public void onDestory() {
		loopTime = 0;
		stopLoopTime = 0;
		try {
			handler.removeCallbacks(runnable);
		} catch (Exception e) {
		}
		handler = null;
	}

	/**
	 * 设置最大轮询次数
	 * 
	 * @param max
	 */
	public void setMaxTime(int max) {
		this.loopMaxTime = max;
	}

	/**
	 * 设置订单超时时间
	 * 
	 * @param totalTime
	 */
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * 判断是否超过最长轮询时间
	 */
	private boolean isOutTotalTime() {
		return System.currentTimeMillis() >= stopLoopTime;
	}
}
