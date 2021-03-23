package com.wizarpos.pay.recode.zusao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.motionpay.pay2.lite.R;
import com.wizarpos.pay.recode.http.HttpNewCallback;
import com.wizarpos.pay.recode.zusao.bean.acti.ZSSelectTypeActivityParam;
import com.wizarpos.pay.recode.zusao.bean.connect.ZsConnectIntentBeanReq;
import com.wizarpos.pay.recode.zusao.bean.req.ZSGetQrCode953Req;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSGetQrCode953Resp;
import com.wizarpos.pay.recode.zusao.callback.ZSSelectTypeActivityCallback;
import com.wizarpos.pay.recode.zusao.connect.ZsConnectManager;
import com.wizarpos.pay.recode.zusao.constants.ZsConstants;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;
import com.wizarpos.pay.recode.zusao.http.ZSHttpManager;
import com.wizarpos.pay.recode.zusao.util.AmountUtil;

import java.math.BigDecimal;

public class ZSSelectPayTypeActivity extends TitleFragmentActivity implements View.OnClickListener, ZSSelectTypeActivityCallback {

    private final static String TAG = ZSSelectPayTypeActivity.class.getSimpleName();

    public static void showActivity(Activity context) {
        ZsConnectManager.startActivityForResultMethodMiddle(context, ZSSelectPayTypeActivity.class);
//        ZsConnectManager.startActivityForResultMethodMiddle(context,);
    }

    private RelativeLayout relCancle, relAliPay, relWxPay, relCloudPay;


    private TextView tvTotalValue;


    private ZSSelectTypeActivityParam activityParam = new ZSSelectTypeActivityParam();

    private final static int WHAT_SHOW_UI_TIMMING = 1;

    @Override
    public int getContentLayout() {
        return R.layout.activity_zs_select_type;
    }

    @Override
    public void init(View view) {
        setTitleCenter(R.string.zs_select_payment);
        setLeftTitleRootVisible(View.GONE);
        tvTotalValue = view.findViewById(R.id.tv_head_right);
        tvTotalValue.setText(AmountUtil.showUi(getTotalValue()));

        //
        relCancle = view.findViewById(R.id.rel_bottom);
        relCancle.setOnClickListener(this);


        relAliPay = view.findViewById(R.id.rel_content_zfb);
        relAliPay.setOnClickListener(this);


        relWxPay = view.findViewById(R.id.rel_content_wx);
        relWxPay.setOnClickListener(this);


        relCloudPay = view.findViewById(R.id.rel_content_cloud);
        relCloudPay.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            ZsConnectIntentBeanReq req = ZsConnectManager.getConnectIntentBeanParam();
            setTotalValue(req);
            activityParam.setInvoiceNo(req.getReqPayload().getUserDefinedEchoData());
        }
        settingHandlerUiShow();
    }

    private void operateCancel() {
        if (isFinishing()) {
            return;
        }
        toast(R.string.zs_cancelling);
        finish();
    }

    /**
     * 左边按钮响应事件
     **/
    protected void onLeftClick() {
        operateCancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_bottom:
                operateCancel();
                break;
            case R.id.rel_content_zfb:
            case R.id.iv_ali:
                activityParam.setPayChannelEnum(ZsPayChannelEnum.ALIPAY);
                doHttpGetQrCode();
                break;
            case R.id.rel_content_wx:
            case R.id.iv_wx:
                activityParam.setPayChannelEnum(ZsPayChannelEnum.WX);
                doHttpGetQrCode();
                break;
            case R.id.rel_content_cloud:
            case R.id.iv_cloud:
                activityParam.setPayChannelEnum(ZsPayChannelEnum.CLOUD);
                doHttpGetQrCode();
                break;
        }
    }


    private void doHttpGetQrCode() {
        showProgressDialog(R.string.zs_loading);
        ZSGetQrCode953Req req = new ZSGetQrCode953Req();
        //
        BigDecimal bigDecimal1 = new BigDecimal(getTotalValue()).multiply(new BigDecimal(100));
        req.setRealAmount(bigDecimal1.setScale(0, BigDecimal.ROUND_UP).toString());
        //
        req.setTipAmount(activityParam.getTipAmount());
        req.setFlag(activityParam.getPayChannelEnum().getFlag());
        req.setPayChannel(activityParam.getPayChannelEnum().getChannel());
        req.setInvoiceNo(activityParam.getInvoiceNo());
        ZSHttpManager.doPost953(req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                onSuccessGetQrCodeCallback((ZSGetQrCode953Resp) t);
            }

            @Override
            public <M> void onError(M r) {
                onErrorGetQrCodeCallback((String) r);
            }

            @Override
            public <M> void onError(int code, M r) {
                onErrorGetQrCodeCallback((String) r);
            }
        });
    }


    private String getTotalValue() {
        return activityParam == null ? "0" : activityParam.getRealAmount();
    }

    private void setTotalValue(ZsConnectIntentBeanReq req) {
        String baseAmount = req.getReqPayload().getBaseAmount();
        if (TextUtils.isEmpty(baseAmount)) {
            baseAmount = "0";
        }
        String tipAmount = req.getReqPayload().getTipAmount();
        if (TextUtils.isEmpty(tipAmount)) {
            tipAmount = "0";
        }
        BigDecimal baseAmountDecimal = new BigDecimal(baseAmount);
        BigDecimal tipAmountDecimal = new BigDecimal(tipAmount);

        activityParam.setRealAmount(baseAmountDecimal.add(tipAmountDecimal).toString());
        activityParam.setTipAmount(tipAmount);
    }


    @Override
    public void onSuccessGetQrCodeCallback(final ZSGetQrCode953Resp resp) {
        if (isFinishing()) {
            return;
        }
        removeHandler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                ZSShowQrCodeActivity.showActivity(ZSSelectPayTypeActivity.this,
                        resp.getRealPath(),
                        resp.getOrderDef().getOrderNo(),
                        activityParam.getPayChannelEnum(),
                        getTotalValue());
            }
        });
    }

    @Override
    public void onErrorGetQrCodeCallback(final String msg) {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                toast(msg);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("tagtagtag", TAG + "onActivityResult()--requestCode:" + requestCode + ",resultCode:" + resultCode);
//        if (requestCode == ZsConstants.INTENT_MYSELF_REQUEST_CODE && resultCode == ZsConstants.INTENT_MYSELF_RESULT_CODE) {
//            ZsConnectManager.intentSettingResultForSuccessOnActivity(this, data);
//        }
//    }

    private Handler mHandlerTask = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_SHOW_UI_TIMMING:
                    operateCancel();
                    break;
            }
        }
    };

    private void settingHandlerUiShow() {
        mHandlerTask.sendEmptyMessageDelayed(WHAT_SHOW_UI_TIMMING, 60 * 1000);
    }

    private void removeHandler() {
        mHandlerTask.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("tagtagtag", TAG + "onKeyDown()");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler();
    }
}
