package com.wizarpos.pay.recode.zusao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.motionpay.pay2.lite.R;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.recode.http.HttpNewCallback;
import com.wizarpos.pay.recode.zusao.bean.acti.ZSShowQrCodeActivityParam;
import com.wizarpos.pay.recode.zusao.bean.req.ZSCloseOrderNoReq;
import com.wizarpos.pay.recode.zusao.bean.req.ZSQueryOrderStatusReq;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSQueryOrderStatusResp;
import com.wizarpos.pay.recode.zusao.connect.ZsConnectManager;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;
import com.wizarpos.pay.recode.zusao.http.ZSHttpManager;
import com.wizarpos.recode.zxing.ZxingQRcodeManager;
import com.wizarpos.recode.zxing.bean.QRCodeParam;


public class ZSShowQrCodeActivity extends TitleFragmentActivity {

    private final String TAG = ZSShowQrCodeActivity.class.getSimpleName();

    public static void showActivity(Context context, String realPath, String orderNo, ZsPayChannelEnum zsPayChannelEnum, String total) {
        ZSShowQrCodeActivityParam param = new ZSShowQrCodeActivityParam();
        param.setRealPath(realPath);
        param.setOrderNo(orderNo);
        param.setZsPayChannelEnum(zsPayChannelEnum);
        param.setRealAmount(total);
        Intent intent = new Intent();
        intent.putExtra(INTENT_DATA, param);
        ZsConnectManager.requestIntent(context, intent);
    }

    private TextView tv_amount;
    private ImageView ivQrCode;
    private ZSShowQrCodeActivityParam activityParam;
    private final static int WHAT_QUERY = 1;

    @Override
    public int getContentLayout() {
        return R.layout.activity_zs_show_qrcode;
    }

    @Override
    public void init(android.view.View view) {
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount.setText(activityParam.getRealAmount());
        ivQrCode = findViewById(R.id.iv_qrcode);
        operateCreateQrCode();
        settingTimingTask();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            activityParam = intent.getParcelableExtra(INTENT_DATA);
        }
    }

    private void operateCreateQrCode() {
        doCreateQrCode();
    }

    private void operateQueryOrder() {
        doQueryOrder();
    }


    private void handlerQrCodeCallback(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivQrCode.setImageBitmap(bitmap);
            }
        });
    }

    private void handlerQueryOrderOnSuccessCallback(ZSQueryOrderStatusResp resp) {
        Log.d("tagtagtag", TAG + " 订单状态:" + resp.getState());
        if (resp.getState() == 2) {
            doCloseOrder();
            ZsConnectManager.resultIntentForStart(this, JSON.toJSONString(resp));
        } else {
            settingTimingTask();
        }
    }

    private void handlerQueryOrderOnErrorCallback(int code, String msg) {
        if (code == 120) {
            settingTimingTask();
        }
    }


    private Handler mHandlerTask = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_QUERY:
                    operateQueryOrder();
                    break;
            }
        }
    };

    private void settingTimingTask() {
        mHandlerTask.sendEmptyMessageDelayed(WHAT_QUERY, 2000);
    }

    private void removeHandlerTask() {
        mHandlerTask.removeCallbacksAndMessages(null);

    }

    private void doCreateQrCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                QRCodeParam qrCodeParam = QRCodeParam.createImgQRCode(activityParam.getRealPath(), 0, 500, 500);
                Bitmap qrbitmap = ZxingQRcodeManager.createOnlyImg(qrCodeParam);
                handlerQrCodeCallback(qrbitmap);
            }
        }).start();
    }


    private void doQueryOrder() {
        ZSQueryOrderStatusReq req = new ZSQueryOrderStatusReq();
        req.setOrderNo(activityParam.getOrderNo());
        req.setFlag(activityParam.getZsPayChannelEnum() == ZsPayChannelEnum.CLOUD ? ZsPayChannelEnum.CLOUD.getFlag() : null);
        ZSHttpManager.doPost954(req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                handlerQueryOrderOnSuccessCallback((ZSQueryOrderStatusResp) t);
            }

            @Override
            public <M> void onError(int code, M m) {
                handlerQueryOrderOnErrorCallback(code, m == null ? null : (String) m);
            }

            @Override
            public <N> void onError(N m) {

            }
        });
    }

    private void doCloseOrder() {
        ZSCloseOrderNoReq req = new ZSCloseOrderNoReq();
        req.setOrderNo(activityParam.getOrderNo());
        req.setFlag(activityParam.getZsPayChannelEnum() == ZsPayChannelEnum.CLOUD ? ZsPayChannelEnum.CLOUD.getFlag() : null);
        ZSHttpManager.doPost962(req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
//                handlerQueryOrderOnSuccessCallback((ZSQueryOrderStatusResp) t);
            }

            @Override
            public <M> void onError(int code, M m) {
//                handlerQueryOrderOnErrorCallback(code, m == null ? null : (String) m);
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
}
