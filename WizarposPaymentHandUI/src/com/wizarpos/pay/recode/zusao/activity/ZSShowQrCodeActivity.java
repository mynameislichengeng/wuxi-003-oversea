package com.wizarpos.pay.recode.zusao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.motionpay.pay2.lite.R;
import com.wizarpos.pay.recode.http.HttpNewCallback;
import com.wizarpos.pay.recode.zusao.bean.acti.ZSShowQrCodeActivityParam;
import com.wizarpos.pay.recode.zusao.bean.req.ZSCloseOrderNoReq;
import com.wizarpos.pay.recode.zusao.bean.req.ZSQueryOrderStatusReq;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSQueryOrderStatusResp;
import com.wizarpos.pay.recode.zusao.callback.ZSShowQrCodeActivityCallback;
import com.wizarpos.pay.recode.zusao.connect.ZsConnectManager;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;
import com.wizarpos.pay.recode.zusao.http.ZSHttpManager;
import com.wizarpos.pay.recode.zusao.util.AmountUtil;
import com.wizarpos.recode.zxing.ZxingQRcodeManager;
import com.wizarpos.recode.zxing.bean.QRCodeParam;
import com.wizarpos.recode.zxing.callback.DrawListener;


public class ZSShowQrCodeActivity extends TitleFragmentActivity implements View.OnClickListener, ZSShowQrCodeActivityCallback {

    private final String TAG = ZSShowQrCodeActivity.class.getSimpleName();


    public static void showActivity(Activity context, String realPath, String orderNo, ZsPayChannelEnum zsPayChannelEnum, String total) {
        ZSShowQrCodeActivityParam param = new ZSShowQrCodeActivityParam();
        param.setRealPath(realPath);
        param.setOrderNo(orderNo);
        param.setZsPayChannelEnum(zsPayChannelEnum);
        param.setRealAmount(total);

        Intent intent = new Intent();
        intent.putExtra(INTENT_DATA, param);
        intent.setClass(context, ZSShowQrCodeActivity.class);
        ZsConnectManager.startActivityForResultMethodMiddle(context, intent);
    }

    private TextView tv_amount;
    private ImageView ivQrCode, ivQrCodeLogo;
    private RelativeLayout relChange, relCancel;
    private ZSShowQrCodeActivityParam activityParam;
    private final static int WHAT_QUERY = 1;
    private final static int WHAT_UI = 2;

    @Override
    public int getContentLayout() {
        return R.layout.activity_zs_show_qrcode;
    }

    @Override
    public void init(android.view.View view) {
        setTitleCenter(activityParam.getZsPayChannelEnum().getTitle());
        setLeftTitleRootVisible(View.GONE);
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount.setText(AmountUtil.showUi(activityParam.getRealAmount()));
        ivQrCode = findViewById(R.id.iv_qrcode);
        ivQrCodeLogo = findViewById(R.id.iv_qrcode_logo);
        switch (activityParam.getZsPayChannelEnum()) {
            case ALIPAY:
                ivQrCodeLogo.setImageResource(R.drawable.ic_qrcode_zfb);
                break;
            case WX:
                ivQrCodeLogo.setImageResource(R.drawable.ic_qrcode_wx);
                break;
            case CLOUD:
                ivQrCodeLogo.setImageResource(R.drawable.ic_qrcode_cloud);
                break;
        }

        relChange = findViewById(R.id.rel_bottom_change_pay);
        relChange.setOnClickListener(this);

        relCancel = findViewById(R.id.rel_bottom_cancel);
        relCancel.setOnClickListener(this);
        operateCreateQrCode();
        operateRotation();
        operateShowUITiming();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            activityParam = intent.getParcelableExtra(INTENT_DATA);

        }
//        activityParam = new ZSShowQrCodeActivityParam();
//        activityParam.setRealAmount("0.12");
//        activityParam.setOrderNo("123");
//        activityParam.setRealPath("www.baidu.com");
//        activityParam.setZsPayChannelEnum(ZsPayChannelEnum.ALIPAY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_bottom_change_pay:
                operateChange();
                break;
            case R.id.rel_bottom_cancel:
                operateCancel();
                break;
        }
    }


    @Override
    public synchronized void operateChange() {
        removeHandlerTask();
        showProgressDialog(R.string.zs_PROCESSING);
        operateQueryOrder(OperateTypeEnum.CHANGE);
    }

    @Override
    public synchronized void operateCancel() {
        removeHandlerTask();
        showProgressDialog(R.string.zs_PROCESSING);
        operateQueryOrder(OperateTypeEnum.CANCEL);
    }

    @Override
    public synchronized void operateRotation() {
        settingTimingTask(WHAT_QUERY, 2 * 1000);
    }

    @Override
    public synchronized void operateShowUITiming() {
        settingTimingTask(WHAT_UI, 3 * 60 * 1000);
    }

    private void operateQueryOrder(OperateTypeEnum operateTypeEnum) {
        if (isFinishing()) {
            return;
        }
        doQueryOrder(operateTypeEnum);
    }


    private void operateCreateQrCode() {
        doCreateQrCode();
    }


    private void operateCloseOrder(OperateTypeEnum operateTypeEnum) {
        showProgressDialog(R.string.zs_cancelling);
        doCloseOrder(operateTypeEnum);
    }


    private void handlerQrCodeCallback(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivQrCode.setImageBitmap(bitmap);
            }
        });
    }

    private synchronized void handlerQueryOrderOnSuccessCallback(ZSQueryOrderStatusResp resp, OperateTypeEnum operateTypeEnum) {
        Log.d("tagtagtag", TAG + " 订单状态:" + resp.getState());
        if (isFinishing()) {
            return;
        }
        hiddenProgressDialog();
        if (resp.getState() == 2) {
            ZSPaySuccessActivity.showActivity(this, activityParam, JSON.toJSONString(resp));
//            ZsConnectManager.intentSettingResultForSuccessStart(this, JSON.toJSONString(resp));
            return;
        }

        switch (operateTypeEnum) {
            case CANCEL:
                operateCloseOrder(operateTypeEnum);
                break;
            case CHANGE:
                operateCloseOrder(operateTypeEnum);
                break;
            case ROTATION:
                operateRotation();
                break;
            case UI_TIME:
                operateCloseOrder(operateTypeEnum);
                break;
        }
    }

    private synchronized void handlerQueryOrderOnErrorCallback(int code, String msg, OperateTypeEnum operateTypeEnum) {
        Log.d("tagtagtag", "code: " + code + " ,msg:" + msg);
        if (isFinishing()) {
            return;
        }
        hiddenProgressDialog();
        switch (operateTypeEnum) {
            case CANCEL:
                operateCloseOrder(operateTypeEnum);
                break;
            case CHANGE:
                operateCloseOrder(operateTypeEnum);
                break;
            case ROTATION:
                operateRotation();
                break;
            case UI_TIME:
                operateCloseOrder(operateTypeEnum);
                break;
        }

    }


    private void handlerCloseOrderOnSuccessCallback(OperateTypeEnum operateTypeEnum) {
        if (isFinishing()) {
            return;
        }
        hiddenProgressDialog();
        switch (operateTypeEnum) {
            case CANCEL:
                finish();
                break;
            case CHANGE:
                ZSSelectPayTypeActivity.showActivity(this);
                break;
            case UI_TIME:
                finish();
                break;
        }


    }

    private void handlerCloseOrderOnErrorCallback(OperateTypeEnum operateTypeEnum) {
        if (isFinishing()) {
            return;
        }
        hiddenProgressDialog();
        switch (operateTypeEnum) {
            case CANCEL:
                finish();
                break;
            case CHANGE:
                ZSSelectPayTypeActivity.showActivity(this);
                break;
            case UI_TIME:
                finish();
                break;
        }
    }


    private Handler mHandlerTask = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_QUERY:
                    operateQueryOrder(OperateTypeEnum.ROTATION);
                    break;
                case WHAT_UI:
                    removeCallbacksAndMessages(null);
                    operateQueryOrder(OperateTypeEnum.UI_TIME);
                    break;
            }
        }
    };

    private void settingTimingTask(int what, long time) {
        mHandlerTask.sendEmptyMessageDelayed(what, time);
    }


    private void removeHandlerTask() {
        mHandlerTask.removeCallbacksAndMessages(null);

    }

    private void doCreateQrCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                QRCodeParam qrCodeParam = QRCodeParam.createImgQRCode(activityParam.getRealPath(), 0, 500, 500);
                Bitmap qrbitmap = ZxingQRcodeManager.createOnlyImg(qrCodeParam, new DrawListener() {
                    @Override
                    public void draw(Canvas canvas) {

//                        ZsPayChannelEnum payChannelEnum = activityParam.getZsPayChannelEnum();
//                        Bitmap bitmap = null;
//                        switch (payChannelEnum) {
//                            case ALIPAY:
//                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_qrcode_zfb, null);
//                                break;
//                            case WX:
//                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_qrcode_wx, null);
//                                break;
//                            case CLOUD:
//                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_qrcode_cloud, null);
//                                break;
//                        }
//
//                        int width = bitmap.getWidth();
//                        int height = bitmap.getHeight();
//                        Log.d("tagtagtag", "width:" + width + ", height:" + height);
//
//
//                        Paint paint = new Paint();
//                        paint.setColor(Color.WHITE);
//                        canvas.drawBitmap(bitmap, 200, 200, paint);


                    }
                });
                handlerQrCodeCallback(qrbitmap);
            }
        }).start();
    }


    private void doQueryOrder(final OperateTypeEnum operateTypeEnum) {
        ZSQueryOrderStatusReq req = new ZSQueryOrderStatusReq();
        req.setOrderNo(activityParam.getOrderNo());
        req.setFlag(activityParam.getZsPayChannelEnum() == ZsPayChannelEnum.CLOUD ? ZsPayChannelEnum.CLOUD.getFlag() : null);
        ZSHttpManager.doPost954(req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                handlerQueryOrderOnSuccessCallback((ZSQueryOrderStatusResp) t, operateTypeEnum);
            }

            @Override
            public <M> void onError(int code, M m) {
                handlerQueryOrderOnErrorCallback(code, m == null ? null : (String) m, operateTypeEnum);
            }

            @Override
            public <N> void onError(N m) {

            }
        });
    }

    private void doCloseOrder(final OperateTypeEnum operateTypeEnum) {
        ZSCloseOrderNoReq req = new ZSCloseOrderNoReq();
        req.setOrderNo(activityParam.getOrderNo());
        req.setFlag(activityParam.getZsPayChannelEnum() == ZsPayChannelEnum.CLOUD ? ZsPayChannelEnum.CLOUD.getFlag() : null);
        ZSHttpManager.doPost962(req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                handlerCloseOrderOnSuccessCallback(operateTypeEnum);
            }

            @Override
            public <M> void onError(int code, M m) {
                handlerCloseOrderOnErrorCallback(operateTypeEnum);
            }

            @Override
            public <N> void onError(N m) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandlerTask();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("tagtagtag", TAG + "onKeyDown()");
        return false;
    }

    public enum OperateTypeEnum {
        ROTATION(0),
        CANCEL(1),
        UI_TIME(2),
        CHANGE(3),
        ;
        private int code;

        OperateTypeEnum(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
