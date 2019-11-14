package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.view.XEditText;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.util.StringUtilUI;
import com.wizarpos.pay2.lite.R;

/**
 * Created by blue_sky on 2016/3/23.
 */
public class NewBATAuthCodeSettingActivity extends BaseViewActivity implements XEditText.DrawableRightListener{
    private XEditText etAuthCode;
    private ImageView ivDivider;
    private Button btnConfirm;
    private boolean isShowPassword;

    public static Intent getStartIntent(Context context){
        return new Intent(context, NewBATAuthCodeSettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setMainView(R.layout.activity_new_bat_authcode_setting);
        showTitleBack();
        setTitleText("移动支付验证码");
        etAuthCode = (XEditText) findViewById(R.id.etAuthCode);
        ivDivider = (ImageView) findViewById(R.id.ivDivider);
        findViewById(R.id.btnConfirm).setOnClickListener(this);
        etAuthCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ivDivider.setPressed(hasFocus);
            }
        });
        etAuthCode.setDrawableRightListener(this);
        String authCode = AppConfigHelper.getConfig(AppConfigDef.auth_code);
        if (!TextUtils.isEmpty(authCode)) {
            etAuthCode.setText(authCode);
        }
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnConfirm:
                String authCode = etAuthCode.getText().toString().trim();
                AppConfigHelper.setConfig(AppConfigDef.auth_code,authCode);
//                UIHelper.ToastMessage(this, "保存成功");
                CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_SUCCESS, "保存成功");
                finish();
                break;
        }
    }


    @Override
    public void onDrawableRightClick(View view) {
        isShowPassword = !isShowPassword;
        String tag = "";
        if (etAuthCode.getTag() != null){
            tag = etAuthCode.getTag().toString();
        }
        if (isShowPassword){
            etAuthCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (!TextUtils.isEmpty(tag)){
                etAuthCode.setText(tag);
            }
            etAuthCode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_show_pw), null);
        } else {
            etAuthCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
            if (!TextUtils.isEmpty(tag)){
                etAuthCode.setText(StringUtilUI.copyChar(tag.length(), "*"));
            }
            etAuthCode.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_hide_pw), null);
        }
    }
}
