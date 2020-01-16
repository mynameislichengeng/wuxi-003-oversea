package com.wizarpos.pay.cashier.view.input;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay2.lite.R;

public class TicketScanUseActivity extends BaseViewActivity implements InputInfoListener {
	private static final int SCAN_REQUEST = 1002;
	private InputInfoSwipeFragment inputInfoSwipeFragment;
	private InputInfoTextFragment inputInfoTextFragment;
	
	private static final int MIX_REQUEST_CODE = 1001;
	
	public static final String TITLE = "title";
	public static final String IS_USE_TEXT = "isUseText";
	public static final String IS_USE_QR_CODE = "isUseQRCode";
	public static final String IS_USE_SWIPE = "isUseSwipe";
	
	public static final String content = "CONTENT";
	public static final String type = "TYPE";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra(TITLE);
		setTitleText(TextUtils.isEmpty(title)?"会员卡":title);
		showTitleBack();
		setMainView(R.layout.activity_input_card_num);
		initFragments();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flReplace, inputInfoTextFragment);
        if(isUseSwipe()){
        	transaction.add(R.id.flReplace, inputInfoSwipeFragment);
        }
        transaction.commit();
        if(isUseSwipe()){
//        	transaction.showFromDialog(inputInfoSwipeFragment);
//        	transaction.hide(inputInfoTextFragment);
        	showSwipeFragment();
        }else{
        	showTextFragment();
//        	transaction.showFromDialog(inputInfoTextFragment);
        }
        
		if(DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 
				||DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_OTHER
				||(isUseSwipe() == false)
				){//手机和M0,默认打开摄像头 wu@[20150826]
			showQRCodeFragment();
		}
	}

	private void initFragments() {
		inputInfoSwipeFragment = new InputInfoSwipeFragment();
		inputInfoTextFragment = new InputInfoTextFragment();	
		inputInfoSwipeFragment.setListener(this);
		inputInfoTextFragment.setListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int menuSize = 0;
		boolean isUseSwipe = isUseSwipe();
		boolean isUseQRCode = getIntent().getBooleanExtra(IS_USE_QR_CODE, true);
		boolean isUseText = getIntent().getBooleanExtra(IS_USE_TEXT, true);
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
//		getMenuInflater().inflate(R.menu.input_member_card, menu);
		return true;
	}

	/**
	 * 是否启用刷卡
	 * @return
	 */
	private boolean isUseSwipe() { //InputInfoCameraActivity中有一份一样的代码
		return getIntent().getBooleanExtra(IS_USE_SWIPE, true) && DeviceManager.getInstance().isSupportSwipe();//增加根据设备类型判断是否支持刷卡
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == 1001) {
			showSwipeFragment();
			return true;
		} else if (id == 1002) {
			showQRCodeFragment();
			return true;
		} else if (id == 1003) {
			showTextFragment();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showSwipeFragment() {
		getSupportFragmentManager().beginTransaction()
		.show(inputInfoSwipeFragment)
		.hide(inputInfoTextFragment)
		.commit();
		inputInfoSwipeFragment.openCardListener();
	}
	
	private void showQRCodeFragment() {
		Intent scanIntent  = getIntent();
		scanIntent.setClass(TicketScanUseActivity.this, TicketUseCameraActivity.class);
		startActivityForResult(scanIntent,SCAN_REQUEST);
		inputInfoSwipeFragment.closeCardListener();
	}
	
	private void showTextFragment() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if(inputInfoSwipeFragment != null && inputInfoSwipeFragment.isAdded()){
			transaction.hide(inputInfoSwipeFragment);
		}
		transaction.show(inputInfoTextFragment);
		transaction.commit();
		inputInfoSwipeFragment.closeCardListener();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == MIX_REQUEST_CODE) {
			if (arg1 == RESULT_OK) {
				setResult(RESULT_OK, arg2);
			}
			this.finish();
		}else if(arg0 == SCAN_REQUEST){
			if(arg1 == RESULT_OK){
				int resultCode = arg2.getIntExtra(InputInfoCameraActivity.IS_SUCCESS, 0);
				if (resultCode == 0) { return; }
				if (resultCode == InputInfoCameraActivity.TO_RESULT) {//获取到结果
					onGetInfo(arg2.getStringExtra("result"), INPUT_TYPE_CAMERA);
				} else if (resultCode == InputInfoCameraActivity.TO_SWIPE) {//跳转到刷卡界面
					showSwipeFragment();
				} else if (resultCode == InputInfoCameraActivity.TO_TEXT){//跳转到手输界面
					showTextFragment();
				}
			}else{
				onBackPressed();
			}
		}
	}

	@Override
	public void onGetInfo(String info, int infoType) {
		Intent intent = getIntent();//使用事先传入的intent
		intent.putExtra(content, info);
		intent.putExtra(type, infoType);
		setResult(RESULT_OK,intent);
		this.finish();
	}
	
	
}
