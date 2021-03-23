package com.wizarpos.pay.recode.zusao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.motionpay.pay2.lite.R;
import com.wizarpos.pay.recode.zusao.bean.acti.ZSPaySuccessActivityParam;
import com.wizarpos.pay.recode.zusao.bean.acti.ZSShowQrCodeActivityParam;
import com.wizarpos.pay.recode.zusao.connect.ZsConnectManager;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;
import com.wizarpos.pay.recode.zusao.util.AmountUtil;


public class ZSPaySuccessActivity extends TitleFragmentActivity {

    private final String TAG = ZSPaySuccessActivity.class.getSimpleName();
    public static void showActivity(Activity context, ZSShowQrCodeActivityParam showQrCodeActivityParam, String json) {

        ZSPaySuccessActivityParam activityParam = new ZSPaySuccessActivityParam();
        activityParam.setRealAmount(showQrCodeActivityParam.getRealAmount());
        activityParam.setResultJson(json);
        activityParam.setZsPayChannelEnum(showQrCodeActivityParam.getZsPayChannelEnum());

        Intent intent = new Intent();
        intent.putExtra(INTENT_DATA, activityParam);
        intent.setClass(context, ZSPaySuccessActivity.class);
        ZsConnectManager.startActivityForResultMethodMiddle(context, intent);
    }

    private TextView tvAmount, tvPayType;

    private ImageView ivLogo;
    private ZSPaySuccessActivityParam activityParam = new ZSPaySuccessActivityParam();

    private final static int WHAT_TIME = 1;


    @Override
    public int getContentLayout() {
        return R.layout.activity_zs_pay_success;
    }

    @Override
    public void init(View view) {
        setTitleCenter(R.string.zs_APPROVED);
        setLeftTitleRootVisible(View.GONE);
        tvAmount = findViewById(R.id.tv_amount);
        tvAmount.setText(AmountUtil.showUi(activityParam.getRealAmount()));
        ivLogo = findViewById(R.id.iv_logo);
        tvPayType = findViewById(R.id.tv_pay_type);
        setLayoutLogo();
        operateSettingTime();
    }

    private void setLayoutLogo() {
        ZsPayChannelEnum zsPayChannelEnum = activityParam.getZsPayChannelEnum();
        tvPayType.setText(zsPayChannelEnum.getTitle());
        switch (zsPayChannelEnum) {
            case ALIPAY:
                ivLogo.setBackgroundResource(R.drawable.ic_zfb_small);
                break;
            case WX:
                ivLogo.setBackgroundResource(R.drawable.ic_wx_small);
                break;
            case CLOUD:
                ivLogo.setBackgroundResource(R.drawable.ic_cloud_small);
                break;
            default:
                break;
        }

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            activityParam = intent.getParcelableExtra(INTENT_DATA);
        }
    }

    private void operateSettingTime() {
        settingTime();
    }


    private Handler mHandlerTask = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_TIME:
                    ZsConnectManager.intentSettingResultForSuccessStart(ZSPaySuccessActivity.this, activityParam.getResultJson());
                    break;

            }
        }
    };

    private void settingTime() {
        mHandlerTask.sendEmptyMessageDelayed(WHAT_TIME, 5 * 1000);
    }

    private void removeTask() {
        mHandlerTask.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeTask();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("tagtagtag", TAG + "onKeyDown()");
        return false;
    }
}
