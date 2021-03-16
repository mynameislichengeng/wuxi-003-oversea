package com.wizarpos.pay.cashier.view.input;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cn.hugo.android.scanner.CaptureActivity;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.ScanFragment;
import com.wizarpos.pay.common.device.ScanFragment.DisplayListener;
import com.motionpay.pay2.lite.R;

/**
 * 扫码
 * 
 * @author wu
 * 
 */
public class InputInfoCameraActivity extends CaptureActivity implements DisplayListener {

	private ScanFragment fragment;
	
	public static final String IS_SUCCESS = "isSuccess";
	public static final int TO_RESULT = 1;
	public static final int TO_SWIPE = 2;
	public static final int TO_TEXT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (DeviceManager.getInstance().isWizarDevice()) {
			super.onCreate(savedInstanceState);
//			setTitleText("会员卡");
			String title = getIntent().getStringExtra("title"); // 除bug wu
			setTitleText(TextUtils.isEmpty(title) ? "扫描" : title);
			showTitleBack();
			initWizarCamera();
		} else {
			initOtherCamera(savedInstanceState);
		}
	}

	/**
	 * 调用zxing摄像头驱动
	 * @param savedInstanceState
	 */
	private void initOtherCamera(Bundle savedInstanceState) {
		doOnCreateAction(savedInstanceState);
	}

	/**
	 * 调用公司内部的摄像头驱动
	 */
	private void initWizarCamera() {
		setMainView(R.layout.activity_input_card_num);
		fragment = new ScanFragment();
		fragment.setListener(this);
		getSupportFragmentManager().beginTransaction().add(R.id.flReplace, fragment).commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!DeviceManager.getInstance().isWizarDevice()){
			doOnResumeAction();
		}
	}
	
	@Override
	protected void onPause() {
		if(!DeviceManager.getInstance().isWizarDevice()){
			doOnPauseAction();
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if(!DeviceManager.getInstance().isWizarDevice()){
			doOnDestroyAction();
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!DeviceManager.getInstance().isWizarDevice()){
			return doOnKeyDownAction(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(!DeviceManager.getInstance().isWizarDevice()){
			doOnActivityResultAction(requestCode, resultCode, intent);
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.input_member_card, menu);
		int menuSize = 0;
		boolean isUseSwipe = isUseSwipe();
		boolean isUseQRCode = getIntent().getBooleanExtra(InputInfoActivity.IS_USE_QR_CODE, true);
		boolean isUseText = getIntent().getBooleanExtra(InputInfoActivity.IS_USE_TEXT, true);
		if(isUseSwipe){
			menuSize ++;
		}
		if(isUseQRCode){
			menuSize ++;
		}
		if(isUseText){
			menuSize ++;
		}
		if(menuSize <= 1){
			return true;
		}
		if(isUseSwipe()){ 
			menu.add(0, 1001, 0, "刷卡");
		}
		if(isUseQRCode){
			menu.add(0, 1002, 1, "扫码");
		}
		if(isUseText){
			menu.add(0, 1003, 2, "手输");
		}
		return true;
	}
	
	/**
	 * 是否启用刷卡
	 * @return
	 */
	private boolean isUseSwipe() {  //InputInfoActivity中有一份一样的代码
		return getIntent().getBooleanExtra(InputInfoActivity.IS_USE_SWIPE, true) && DeviceManager.getInstance().isSupportSwipe();//增加根据设备类型判断是否支持刷卡
	}

	@Override
	public void onClick(View v) {
		if(!DeviceManager.getInstance().isWizarDevice()){
			doOnClickAction(v);
		}
		super.onClick(v);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == 1001) {
			Intent data = getIntent();//使用事先传入的intent
			data.putExtra(IS_SUCCESS, TO_SWIPE);
			setResult(RESULT_OK, data);
			this.finish();
			return true;
		} else if (id == 1002) {
			return true;
		} else if (id == 1003) {
			Intent data = getIntent();//使用事先传入的intent
			data.putExtra(IS_SUCCESS, TO_TEXT);
			setResult(RESULT_OK, data);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void display(Response response) {
		if (response.code == 0) {
			Intent data = getIntent(); //使用事先传入的intent
			data.putExtra(IS_SUCCESS, TO_RESULT);
			data.putExtra("result", response.result.toString());
			setResult(RESULT_OK, data);
			this.finish();
		} else {
			setResult(RESULT_CANCELED);
		}
	}

}
