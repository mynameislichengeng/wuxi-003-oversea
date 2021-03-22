package com.wizarpos.pay.recode.zusao.activity;

import android.content.Context;

import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.motionpay.pay2.lite.R;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;

public class ZSShowQrCodeActivity extends TitleFragmentActivity {

    public static void showActivity(Context context, String realPath, String orderNo, ZsPayChannelEnum zsPayChannelEnum,String total){

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_zs_show_qrcode;
    }

    @Override
    public void init(android.view.View view) {

    }

    @Override
    public void initData() {

    }
}
