package com.wizarpos.pay.common.device;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wizarpos.base.net.Response;
import com.wizarpos.barcode.scanner.IScanEvent;
import com.wizarpos.barcode.scanner.ScannerRelativeLayout;
import com.wizarpos.barcode.scanner.ScannerResult;

import com.wizarpos.wizarpospaymentlogic.R;

/**
 * 
 * @Author:Huangweicai
 * @Date:2015-6-30 上午11:23:49
 * @Reason:摄像头fragment,
 */
public class ScanFragment extends Fragment {

	private ScannerRelativeLayout scanner;

	private IScanEvent iScanSuccessListener;

	/**
	 *                 0                       1
	 *  wizarpos 默认摄像头（前置）             客显摄像头（后置）
	 *  wizarpad 默认摄像头（后置）             客显摄像头（后置）
	 *  H0              变焦摄像头（后置）             定焦摄像头（后置）
	 **/
	private final int CAMERA_INDEX_0 = 0;
	private final int CAMERA_INDEX_1 = 1;

	private String padOrpos = "";

	private int cameraIndex = CAMERA_INDEX_1;

	private DisplayListener listener;
	
	private boolean isScaned = false;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				Response response = new Response();
				response.setCode(0);
				response.setResult(msg.obj);
				scanner.stopScan();
				if (listener != null) {
					listener.display(response);
				}
//				Toast.makeText(getActivity(), "" + msg.obj, Toast.LENGTH_SHORT)
//						.show();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Window window = getActivity().getWindow();//不熄灭屏幕 wu@[20150826]
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		Log.d("SCAN_FRAGMENT", "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scan_fragment, null);
		scanner = (ScannerRelativeLayout) view.findViewById(R.id.scanner);
		initScan();
		return view;
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-6-30 上午11:23:22
	 * @Reason:初始化摄像头
	 */
	private void initScan() {
		padOrpos = getsystemPropertie("ro.product.model");
		if (padOrpos.contains("WIZARPOS")) {

		}
//		cameraIndex = DeviceManager.getInstance().getCameraIndex(); //改为从DeviceManager中维护
		scanner.setCameraIndex(cameraIndex);
		iScanSuccessListener = new ScanSuccesListener();
		scanner.setScanSuccessListener(iScanSuccessListener);
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-6-30 上午10:19:42
	 * @Reason:切换摄像头
	 */
	public void switchCamera() {
		/** 切换摄像头下标 */
		cameraIndex = cameraIndex == CAMERA_INDEX_0 ? CAMERA_INDEX_1
				: CAMERA_INDEX_0;
		scanner.stopScan();
		scanner.setCameraIndex(cameraIndex);
		scanner.startScan();
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-6-23 上午10:25:49
	 * @Reason:设置摄像头下标
	 * @param cameraIndex
	 */
	public void setCameraIndex(int cameraIndex) {
		this.cameraIndex = cameraIndex;
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-6-30 上午11:24:20
	 * @Reason:设置摄像头监听
	 * @param listener
	 */
	public void setListener(DisplayListener listener) {
		this.listener = listener;
	}

	public interface DisplayListener {
		void display(Response response);
	}

	private class ScanSuccesListener extends IScanEvent {
		@Override
		public void scanCompleted(ScannerResult scannerResult) {
			if(isScaned){
				return;
			}
			isScaned = true;
			Message msg = new Message();
			msg.what = 10;
			msg.obj = scannerResult.getResult().toString();
			// Toast.makeText(getActivity(),
			// scannerResult.getResult().toString(), 0).show();
			Log.e("TAG", scannerResult.getResult().toString());
			Log.d("SCAN_FRAGMENT", "scanCompleted");
			
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		scanner.onResume();
		scanner.startScan();
	}

	public void onDestory() {
		super.onDestroy();
		scanner.stopScan();
	}

	@Override
	public void onStart() {
		super.onStart();
//		scanner.startScan();
	}

	@Override
	public void onPause() {
		super.onPause();
		scanner.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		scanner.stopScan();
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-6-29 下午6:11:11
	 * @Reason:判断客显是否存在
	 * @return
	 */


	// 判断是wizarpad还是wizarpos
	private String getsystemPropertie(String name) {
		Object bootloaderVersion = null;
		try {
			Class<?> systemProperties = Class
					.forName("android.os.SystemProperties");
			Log.i("systemProperties", systemProperties.toString());
			bootloaderVersion = systemProperties.getMethod("get",
					new Class[] { String.class, String.class }).invoke(
					systemProperties, new Object[] { name, "unknown" });
			Log.i("bootloaderVersion", bootloaderVersion.getClass().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bootloaderVersion.toString();
	}

}
