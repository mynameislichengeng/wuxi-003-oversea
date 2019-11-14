package com.wizarpos.pay.view.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.wizarpos.pay2.lite.R;

public class MyProgressDialog extends BaseDialog {
	// UI组件
	View loginLoading;

	// AnimationDrawable loadingAnimation;

	/**
	 * 说明：构造
	 * 
	 * @param context
	 *            上下文
	 */
	public MyProgressDialog(Context context, int theme) {
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
		this.setContentView(R.layout.myprogress_dialog);
		loginLoading = findViewById(R.id.login_loading);
		final AnimationDrawable loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
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
