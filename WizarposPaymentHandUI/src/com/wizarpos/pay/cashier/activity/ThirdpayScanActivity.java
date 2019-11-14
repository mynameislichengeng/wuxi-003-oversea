package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.ScanFragment;
import com.wizarpos.pay.common.device.ScanFragment.DisplayListener;
import com.wizarpos.pay2.lite.R;

import cn.hugo.android.scanner.CaptureActivity2;
/**
 * 
 * @author hong
 * 第三方支付扫描界面
 *
 */
public abstract class ThirdpayScanActivity extends CaptureActivity2 implements DisplayListener {
	public static final String IS_SUCCESS = "isSuccess";
	public static final int TO_RESULT = 1;
	private ScanFragment fragment;
	private TextView remindInfoTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DeviceManager.getInstance().isWizarDevice()) {
			initWizarCamera(savedInstanceState);
		} else {
			initThirdCamera(savedInstanceState);
		}
		initPublicView();
	}

	private void initPublicView() {
		showTitleBack();
		remindInfoTv = (TextView) findViewById(R.id.remind_info);
		findViewById(R.id.btnMicroPay).setOnClickListener(this);
		findViewById(R.id.btnCheckOrder).setOnClickListener(this);
	}

	private void initWizarCamera(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		setMainView(R.layout.activity_micro);
		fragment = new ScanFragment();
		fragment.setListener(this);
		getSupportFragmentManager().beginTransaction().add(R.id.flReplace, fragment).commit();
	}

	private void initThirdCamera(Bundle savedInstanceState) {
		doOnCreatAction(R.layout.thirdpay_scan_activity, savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!DeviceManager.getInstance().isWizarDevice()) {
			doOnResumeAction();
		}
	}

	@Override
	protected void onPause() {
		if (!DeviceManager.getInstance().isWizarDevice()) {
			doOnPauseAction();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (!DeviceManager.getInstance().isWizarDevice()) {
			doOnDestroyAction();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!DeviceManager.getInstance().isWizarDevice()) { 
			return doOnKeyDownAction(keyCode, event); }
		return doOnKeyDownAction(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (!DeviceManager.getInstance().isWizarDevice()) {
			doOnActivityResultAction(requestCode, resultCode, intent);
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public void onClick(View v) {
		if (!DeviceManager.getInstance().isWizarDevice()) {
			doOnClickAction(v);
		}
		switch (v.getId()) {
		case R.id.btnMicroPay:
			toMicroPay();
			break;
		case R.id.btnCheckOrder:
			checkOrder();
			break;
		}
		super.onClick(v);
	}

	protected abstract void toMicroPay();
	
	protected abstract void checkOrder();

	protected void showRemindInfo(String info) {
		remindInfoTv.setText(info);
	}
}
