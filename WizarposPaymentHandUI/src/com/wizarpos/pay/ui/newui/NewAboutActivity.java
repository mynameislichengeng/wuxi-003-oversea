package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.motionpay.pay2.lite.R;

/**
 * Created by blue_sky on 2016/3/23.
 */
public class NewAboutActivity extends BaseViewActivity {
    private TextView tvVersionNum;
    private TextView tvSerialNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setMainView(R.layout.activity_new_about);
        findViewById(R.id.llContactNum).setOnClickListener(this);
        tvSerialNum = (TextView) findViewById(R.id.tvSerialNum);
        tvVersionNum = (TextView) findViewById(R.id.tvVersionNum);
        tvVersionNum.setText(getVersion());
        tvSerialNum.setText(AppConfigHelper.getConfig(AppConfigDef.sn, ""));
        showTitleBack();
        setTitleText(getResources().getString(R.string.about));
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llContactNum://打电话
                String telNum = "010-1010";
                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + telNum));
                startActivity(phoneIntent);
                finish();
                break;
        }
    }
    private String getVersion() {
        try {
            PackageInfo pkg =getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            return pkg.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
