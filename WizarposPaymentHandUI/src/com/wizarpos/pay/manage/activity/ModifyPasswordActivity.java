package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay2.lite.R;

public class ModifyPasswordActivity extends BaseViewActivity {
    EditText etOriginal, etNew, etPassword;// 原密码，新密码，确定的密码
    String originalPassword = "", newPassword = "", commitPassword = "";
    AppConfiger present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        present = new AppConfiger(this);
        initView();
        int[] btnIds = {R.id.btn_commit_password};
        setOnClickListenerByIds(btnIds, this);
    }

    private void initView() {
        setMainView(R.layout.modify_password_activity);
        setTitleText(getResources().getString(R.string.password));
        showTitleBack();
        etOriginal = (EditText) findViewById(R.id.et_original_password);
        etNew = (EditText) findViewById(R.id.et_new_password);
        etPassword = (EditText) findViewById(R.id.et_confirm_password);
        // M0隐藏软键盘 wu
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            etOriginal.setInputType(InputType.TYPE_NULL);
            etNew.setInputType(InputType.TYPE_NULL);
            etPassword.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit_password:
                originalPassword = etOriginal.getText().toString().trim();
                newPassword = etNew.getText().toString().trim();
                commitPassword = etPassword.getText().toString().trim();
                // 原密码如何校验
                if (present.checkSecurityPassword(originalPassword)) {
                    if (newPassword.equals(originalPassword)) {
                        UIHelper.ToastMessage(ModifyPasswordActivity.this, "The original password is the same as new password");
                        return;
                    }
                    if (newPassword.equals(commitPassword)) {
                        boolean state = present.modifySecurityPassword(originalPassword, newPassword);
                        if (state) {
                            AppConfigHelper.setConfig(AppConfigDef.secure_password, Tools.md5(newPassword));
                            UIHelper.ToastMessage(ModifyPasswordActivity.this, "Password is changed");
                            finish();
                        } else {
                            UIHelper.ToastMessage(ModifyPasswordActivity.this, "Password change failed");
                        }
                    } else {
                        UIHelper.ToastMessage(ModifyPasswordActivity.this, "The new password is not the same as comfirm password");
                    }
                } else {
                    UIHelper.ToastMessage(ModifyPasswordActivity.this, "The original password input error");
                }
                break;
        }
    }
}
