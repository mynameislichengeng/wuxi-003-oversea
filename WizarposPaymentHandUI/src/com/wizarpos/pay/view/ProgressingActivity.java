package com.wizarpos.pay.view;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.wizarpos.pay.MainFragmentActivity2;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.view.util.MyProgressDialog;
import com.motionpay.pay2.lite.R;

public class ProgressingActivity extends BaseViewActivity {
	MyProgressDialog myDialog;
	View loginLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myprogress_dialog);
		loginLoading = findViewById(R.id.login_loading);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		final AnimationDrawable loadingAnimation = (AnimationDrawable) loginLoading.getBackground();
		Handler mHandler = new Handler();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				loadingAnimation.start();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(ProgressingActivity.this, MainFragmentActivity2.class);
						startActivity(intent);
						ProgressingActivity.this.finish(); // 结束启动动画界面
					}
				}, 1000);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) { return false; }

		return super.onTouchEvent(event);
	}
}
