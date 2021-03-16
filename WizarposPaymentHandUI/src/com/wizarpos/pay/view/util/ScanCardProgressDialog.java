package com.wizarpos.pay.view.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.motionpay.pay2.lite.R;

public class ScanCardProgressDialog extends BaseDialog {
	// UI组件
	View scancardLoading;

	// AnimationDrawable loadingAnimation;

	/**
	 * 说明：构造
	 * 
	 * @param context
	 *            上下文
	 */
	public ScanCardProgressDialog(Context context, int theme) {
		super(context);
	}

	/**
	 * 说明：创建
	 * 
	 * @param savedInstanceState
	 *            系统对象
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.scancard_progress_dialog);
		scancardLoading = findViewById(R.id.scancrd_loading);
		final AnimationDrawable loadingAnimation = (AnimationDrawable) scancardLoading.getBackground();
		Handler mHandler = new Handler();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				loadingAnimation.start();
			}
		});
		this.setCancelable(false);
	}
}
