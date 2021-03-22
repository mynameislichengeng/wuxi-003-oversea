package com.wizarpos.pay.recode.zusao.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.motionpay.pay2.lite.R;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.Calculater;
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

import java.math.BigDecimal;

public class ZSSelectPayTypeActivity extends TitleFragmentActivity implements View.OnClickListener, ZSSelectTypeActivityCallback {

    private final static String TAG = ZSSelectPayTypeActivity.class.getSimpleName();

    public static void showActivity(Context context) {
//        ZsConnectManager.requestIntent(context, ZSSelectPayTypeActivity.class);
        context.startActivity(new Intent(context, ZSSelectPayTypeActivity.class));
    }

    private RelativeLayout relCancle;

    private ImageView ivAlipay, ivWxpay, ivCloudpay;

    private TextView tvTotalValue;

    private ZSSelectTypeActivityParam activityParam = new ZSSelectTypeActivityParam();

    @Override
    public int getContentLayout() {
        return R.layout.activity_zs_select_type;
    }

    @Override
    public void init(View view) {
        setTitleCenter(R.string.zs_select_payment);
        tvTotalValue = view.findViewById(R.id.tv_head_right);
        tvTotalValue.setText(getTotalValue());
        relCancle = view.findViewById(R.id.rel_bottom);
        relCancle.setOnClickListener(this);
        ivAlipay = view.findViewById(R.id.iv_ali);
        ivAlipay.setOnClickListener(this);
        ivWxpay = view.findViewById(R.id.iv_wx);
        ivWxpay.setOnClickListener(this);
        ivCloudpay = view.findViewById(R.id.iv_cloud);
        ivCloudpay.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            ZsConnectIntentBeanReq req = ZsConnectManager.getConnectIntentBeanParam();
            activityParam.setRealAmount(req.getReqPayload().getBaseAmount());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_bottom:
                finish();
                break;
            case R.id.iv_ali:
                activityParam.setPayChannelEnum(ZsPayChannelEnum.ALIPAY);
                doHttpGetQrCode();
                break;
            case R.id.iv_wx:
                activityParam.setPayChannelEnum(ZsPayChannelEnum.WX);
                doHttpGetQrCode();
                break;
            case R.id.iv_cloud:
                activityParam.setPayChannelEnum(ZsPayChannelEnum.CLOUD);
                doHttpGetQrCode();
                break;
        }
    }


    private void doHttpGetQrCode() {
        showProgressDialog(R.string.zs_http_dialog_get_qrcode);
        ZSGetQrCode953Req req = new ZSGetQrCode953Req();
        BigDecimal bigDecimal1 = new BigDecimal(getTotalValue()).multiply(new BigDecimal(100));
        req.setRealAmount(bigDecimal1.setScale(0, BigDecimal.ROUND_DOWN).toString());
        req.setTipAmount(activityParam.getTipAmount());
        req.setFlag(activityParam.getPayChannelEnum().getFlag());
        req.setPayChannel(activityParam.getPayChannelEnum().getChannel());
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

            }
        });
    }


    private String getTotalValue() {
        return activityParam == null ? "0" : activityParam.getRealAmount();
    }


    @Override
    public void onSuccessGetQrCodeCallback(final ZSGetQrCode953Resp resp) {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                toast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZsConstants.INTENT_MYSELF_REQUEST_CODE && requestCode == ZsConstants.INTENT_MYSELF_RESULT_CODE) {
            Log.d("tagtagtag", TAG + "主扫返回--onActivityResult()--");
            ZsConnectManager.onActivityMyselfIntentResult(this, data);
        }
    }
}
