package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.barcode.scanner.ScannerResult;
import com.wizarpos.barcode.scanner.interfaces.IScanner;
import com.wizarpos.barcode.scanner.interfaces.IScannerResult;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.ScanFragment.DisplayListener;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.motionpay.pay2.lite.R;

import cn.hugo.android.scanner.CaptureActivity2;

/**
 * @author hong
 * 第三方支付扫描界面
 * 仅为极简版设计的第三方支付扫描界面/仅适配Q1
 */
public abstract class NewThirdpayScanActivity extends CaptureActivity2 implements DisplayListener, IScannerResult {

    private final String TAG = NewThirdpayScanActivity.class.getSimpleName();
    public static final String IS_SUCCESS = "isSuccess";
    public static final int TO_RESULT = 1;
    protected boolean isSuccessPayed = false;
    private ImageView capture_flashlight, iv_capture_close, iv_capture_change;
    private IScanner scanner;
    //	private ScanFragment fragment;
    private TextView result;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		if (DeviceManager.getInstance().isWizarDevice()) {
        initWizarCamera(savedInstanceState);
//		} else {
//			Logger.error("不支持的设备类型");
//		}
        initPublicView();
    }

    private void initPublicView() {
        showTitleBack();
    }

    private void initWizarCamera(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_micro_new);
        ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
        TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mQrLineView.setAnimation(mAnimation);
        // 监听按钮事件
        // change_camera
        Button changeCamera = (Button) this.findViewById(R.id.change_camera);
        changeCamera.setOnClickListener(this);
        // flashlight
        Button flashlight = (Button) this.findViewById(R.id.flashlight);
        flashlight.setOnClickListener(this);
        scanner = (IScanner) findViewById(R.id.scanner);
        scanner.setCameraIndex(0);
        scanner.setScanSuccessListener(this);
        //默认关闭灯
        if (scanner.isOpenLED()) {
            scanner.closeLED();
        }

        Animation mScaleAnimation = new ScaleAnimation(0.0f, 1.5f, 0.0f, 1.5f, Animation.RELATIVE_TO_PARENT, 0.5f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mScaleAnimation.setDuration(100);
//		scanner.setAnimation(mScaleAnimation);
        result = (TextView) this.findViewById(R.id.scan_result);


    }

    @Override
    protected void onResume() {
        scanner.onResume();
        super.onResume();
        scanner.startScan();
    }

    @Override
    protected void onPause() {
        scanner.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        scanner.stopScan();
        if (null != mHandler) {
            mHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return doOnKeyDownAction(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flashlight:
                changeLight();
                break;
        }
        super.onClick(v);
    }

    protected abstract void toMicroPay();

    protected abstract void checkOrder();

    protected abstract void onScanSuccess(ScannerResult scannerResult);

    protected void showRemindInfo(String info) {
        result.setText(info);
    }

    @Override
    public void scanCompleted(final ScannerResult scannerResult) {
        scanner.onPause();
        if (!TextUtils.isEmpty(scannerResult.getResult())) {
            try {
                Log.d("tagtagtag", TAG+"scanCompleted()");
                onScanSuccess(scannerResult);
            } catch (Exception e) {
                CommonToastUtil.showMsgBelow(NewThirdpayScanActivity.this, CommonToastUtil.LEVEL_ERROR, "扫描失败");
//				UIHelper.ToastMessage(NewThirdpayScanActivity.this, "扫描失败");
                e.printStackTrace();
                resumeScan(2000);
            }
        }
    }

    protected void resumeScan(long time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isSuccessPayed) {
                    scanner.onResume();
                }
            }
        }, time);
    }

    @Override
    protected void changeLight() {
//		super.changeLight();
        if (scanner.isOpenLED()) {
            scanner.closeLED();
        } else {
            scanner.openLED();
        }
    }
}
