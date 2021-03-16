package com.wizarpos.pay.ui.newui;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay.view.util.StringUtilUI;
import com.motionpay.pay2.lite.R;

/**
 * Created by blue_sky on 2016/3/23.
 * @deprecated 极简收款中不用安全密码
 */
public class NewPasswordSettingActivity extends BaseViewActivity implements View.OnKeyListener{
    private EditText etOldPwd, etNewPwd, etNewPwdConfirm;
    String originalPassword = "", newPassword = "", commitPassword = "";
    AppConfiger present;
    private int MAX_PWD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        present = new AppConfiger(this);
    }

    private void initView() {
        setMainView(R.layout.activity_new_password_setting);
        showTitleBack();
        setTitleText("密码管理");
        etOldPwd = (EditText) findViewById(R.id.etOldPwd);
        etNewPwd = (EditText) findViewById(R.id.etNewPwd);
        etNewPwdConfirm = (EditText) findViewById(R.id.etNewPwdConfirm);
        etOldPwd.setOnKeyListener(this);
        etNewPwd.setOnKeyListener(this);
        etNewPwdConfirm.setOnKeyListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        // 隐藏软键盘
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            etOldPwd.setInputType(InputType.TYPE_NULL);
            etNewPwd.setInputType(InputType.TYPE_NULL);
            etNewPwdConfirm.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave :
                saveData();
                break;
        }
        super.onClick(v);
    }

    private void saveData() {
        originalPassword = etOldPwd.getTag().toString();
        newPassword = etNewPwd.getTag().toString();
        commitPassword = etNewPwdConfirm.getTag().toString();
        // 原密码如何校验
        if (present.checkSecurityPassword(originalPassword)) {
            if (newPassword.equals(originalPassword)) {
                UIHelper.ToastMessage(NewPasswordSettingActivity.this, "原密码与新密码相同");
                return;
            }
            if (newPassword.equals(commitPassword)) {
                boolean state = present.modifySecurityPassword(originalPassword, newPassword);
                if (state) {
                    UIHelper.ToastMessage(NewPasswordSettingActivity.this, "密码修改成功");
                    finish();
                } else {
                    UIHelper.ToastMessage(NewPasswordSettingActivity.this, "密码修改失败");
                }
            } else {
                UIHelper.ToastMessage(NewPasswordSettingActivity.this, getResources().getString(R.string.password_error));
            }
        } else {
            UIHelper.ToastMessage(NewPasswordSettingActivity.this, "原密码输入错误");
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //修复Q1密码明文问题 用onkey来进行控制
        if (v.getId() != R.id.etOldPwd
                && v.getId() != R.id.etNewPwd
                && v.getId() != R.id.etNewPwdConfirm ) {
            return false;
        }
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return true;
        }
        EditText etPassword = (EditText) v;
        /** 只对数字和删除键进行监听*/
        if (keyCode <= KeyEvent.KEYCODE_9 && keyCode >= KeyEvent.KEYCODE_0) {
            if (etPassword.getText().toString().length() >= MAX_PWD_LENGTH) {//密码长度不能大于MAX_PWD_LENGTH位
                return true;
            }
            String tag = keyCode - KeyEvent.KEYCODE_0 + "";
            String etTag;
            if (etPassword.getTag() == null) {
                etTag = tag;
            } else {//将密码保存在tag里面
                etTag = etPassword.getTag().toString() + tag;
            }
            etPassword.setTag(etTag);
            etPassword.setText(StringUtilUI.copyChar(etTag.length(), "*"));
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {//删除键
            if (etPassword.getTag() == null || TextUtils.isEmpty(etPassword.getTag().toString())) {
                return false;
            }
            String etTag = etPassword.getTag().toString().substring(0, etPassword.getTag().toString().length() - 1);
            etPassword.setTag(etTag);
            etPassword.setText(StringUtilUI.copyChar(etTag.length(), "*"));
            return true;
        }
        return true;
    }
}
