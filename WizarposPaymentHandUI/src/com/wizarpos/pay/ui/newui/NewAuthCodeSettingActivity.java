package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay2.lite.R;

/**
 * Created by wu on 16/3/25.
 */
public class NewAuthCodeSettingActivity extends BaseViewActivity{

    public static Intent getStartIntent(Context context){
        return new Intent(context, NewAuthCodeSettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("移动支付验证码");
        showTitleBack();
        setMainView(R.layout.activity_new_auth_code_setting);
        findViewById(R.id.llBATAuthcode).setOnClickListener(this);
        findViewById(R.id.llCardLinkDownload).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.llBATAuthcode:
                startActivity(NewBATAuthCodeSettingActivity.getStartIntent(this));
                break;
            case R.id.llCardLinkDownload:
                startActivity(NewCardLinkSettingActivity.getStartIntent(this));
                break;
        }
    }


}
