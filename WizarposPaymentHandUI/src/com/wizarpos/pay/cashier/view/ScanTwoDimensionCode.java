package com.wizarpos.pay.cashier.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wizarpos.barcode.scanner.IScanEvent;
import com.wizarpos.barcode.scanner.ScannerRelativeLayout;
import com.wizarpos.barcode.scanner.ScannerResult;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.view.fragment.LazyFragment;
import com.wizarpos.pay2.lite.R;

public class ScanTwoDimensionCode extends LazyFragment {

	private ScanListener listener;
	private static final int START_SCANNER = 1;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private boolean mHasLoadedOnce;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case START_SCANNER:
				if (scanner != null) {
					scanner.startScan();
				}
				break;

			default:
				break;
			}
		}

		;
	};

	public void setListener(ScanListener listener) {
		this.listener = listener;
	}

	ScannerRelativeLayout scanner;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scan_two_dimensioncode, container , false);
		setupView(view);
		isPrepared = true;
		lazyLoad();
		return view;
	}

	private void setupView(View view) {
		scanner = (com.wizarpos.barcode.scanner.ScannerRelativeLayout) view.findViewById(R.id.re_scanner_two_dimension_progress);
		initScan();
	}

	/*
	 * @Override public void setUserVisibleHint(boolean isVisibleToUser) { super.setUserVisibleHint(isVisibleToUser); if(scanner == null){ return; }
	 * if(isVisibleToUser){ scanner.onResume(); }else{ scanner.onPause(); } }
	 */

	private void initScan() {
		scanner.setFrontFacingCamera(false);
		scanner.setCameraIndex(DeviceManager.getInstance().getCameraIndex());
		scanner.startScan();
		scanner.setScanSuccessListener(new IScanEvent() {
			public void scanCompleted(ScannerResult result) {
				scanner.stopScan();
				scanner.onPause();
				String authCode = result.getResult();
				if (listener != null) {
					listener.onScan(authCode, true);
				}
			}
		});

	}

	@Override
	public void onResume() {
		// if (scanner != null) {
		// scanner.onResume();
		// }
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (scanner != null) {
			scanner.onPause();
		}
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) { return; }
		if (scanner != null) {
			scanner.onResume();
		}
		new Thread() {
			public void run() {
				try {
					sleep(500L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(START_SCANNER);
			}
		}.start();
		initScan();

	}

	@Override
	protected void invisible() {
		if (scanner != null) scanner.stopScan();
	}
}
