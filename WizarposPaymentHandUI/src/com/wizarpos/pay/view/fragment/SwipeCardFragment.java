package com.wizarpos.pay.view.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.view.ScanListener;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.util.ScanCardProgressDialog;
import com.motionpay.pay2.lite.R;

/**
 * 磁条卡刷卡
 */
public class SwipeCardFragment extends Fragment {
	private ScanListener listener;
	ScanCardProgressDialog scanCardProgressDialog;
	View scanCardLoading;

	public void setListener(ScanListener listener) {
		this.listener = listener;
	}
	
	/**
	 * 关闭刷卡
	 */
	public void closeCardListener(){
		DeviceManager.getInstance().closeCardListener();
	}
	
	/**
	 * 打开刷卡 
	 */
	public void openCardListener(){
		DeviceManager.getInstance().getTrack2(new Handler(), new ResultListener() {// 刷卡

			@Override
			public void onSuccess(Response response) {
				DeviceManager.getInstance().closeCardListener();
				if(listener == null){
					return;
				}
				listener.onScan(response.result.toString(), false);
			}

			@Override
			public void onFaild(Response response) {
				DeviceManager.getInstance().closeCardListener();
				UIHelper.ToastMessage(getActivity(), response.msg);
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scancard_progress_dialog, container , false);
		setupView(view);
		return view;
	}

	private void setupView(View view) {
		scanCardLoading = (View) view.findViewById(R.id.scancrd_loading);
		final AnimationDrawable loadingAnimation = (AnimationDrawable) scanCardLoading.getBackground();
		Handler mHandler = new Handler();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				loadingAnimation.start();
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DeviceManager.getInstance().closeCardListener();
	}
}
