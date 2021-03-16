package com.wizarpos.pay.manage.activity;

import java.util.List;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.manager.presenter.CashierManager;
import com.motionpay.pay2.lite.R;

public class EditCashierManagerActivity extends BaseViewActivity {
    private CashierManager cashierManager;
    private EditText etLoginNo, etPassword, etName, etContaceWay;
    private ToggleButton tbManager;
    private String longNum = null, passWord = null, contactWay = null, realName = null;
    private UserBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cashierManager = new CashierManager(this);
        initView();
    }

    private void initView() {
        setMainView(R.layout.activity_cashier_manager_new);
        etLoginNo = (EditText) findViewById(R.id.et_login_num);
        etPassword = (EditText) findViewById(R.id.et_password);
        etName = (EditText) findViewById(R.id.et_name);
        etContaceWay = (EditText) findViewById(R.id.et_contact);
        tbManager = (ToggleButton) findViewById(R.id.tb_is_manager);
        bean = (UserBean) getIntent().getSerializableExtra("bean");
        if (!TextUtils.isEmpty(String.valueOf(bean.getType())) && bean.getType() != 0) {
            tbManager.setChecked(true);
        }
        if (TextUtils.isEmpty(bean.getOperId())) {
            etLoginNo.setText("");
        } else {
            etLoginNo.setText(bean.getOperId());
        }
        if (TextUtils.isEmpty(bean.getPassword())) {
            etPassword.setText("");
        } else {
            etPassword.setText(bean.getPassword());
        }
        if (TextUtils.isEmpty(bean.getPhone())) {
            etContaceWay.setText("");
        } else {
            etContaceWay.setText(bean.getPhone());
        }
        etName.setText(bean.getRealName());
        etLoginNo.setFocusable(false);
        etLoginNo.setEnabled(false);
        setTitleText(getResources().getString(R.string.edit_chashier_manger));
        showTitleBack();
        setTitleRight("保存");
        int[] btnIds = { R.id.btn_add_confirm };
        setOnClickListenerByIds(btnIds, this);

        //M0隐藏软键盘 wu
//		if(DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1){
//			etLoginNo.setInputType(InputType.TYPE_NULL);
//			etPassword.setInputType(InputType.TYPE_NULL);
//			etContaceWay.setInputType(InputType.TYPE_NULL);
//		}
        initInputViews();
    }

    @Override
    protected void onTitleRightClicked() {
        super.onTitleRightClicked();
        View view = findViewById(R.id.btn_add_confirm);
        if (view != null){
            view.performClick();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_add_confirm:// 修改保存
                int type = 0;
                if (tbManager.isChecked()) {
                    type = 1;
                }
                longNum = etLoginNo.getText().toString();
                passWord = etPassword.getText().toString();
                realName = etName.getText().toString();
                contactWay = etContaceWay.getText().toString();
                if (TextUtils.isEmpty(longNum) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(realName)) {
                    UIHelper.ToastMessage(this, "请将信息补充完整");
                    return;
                }
                //bugfix @yaosong [20160105]
                if (contactWay.length() < 11) {
                    UIHelper.ToastMessage(this, "手机号格式不正确");
                    return;
                }
                if (tbManager.isChecked()) {
                    type = 1;
                } else {
                    type = 0;
                }
                cashierManager.updateCashier(longNum, passWord, realName, contactWay, type);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private void initInputViews() {
        LinearLayout llOperatorId = (LinearLayout) findViewById(R.id.llOperatorId);
        LinearLayout llOperatorPwd = (LinearLayout) findViewById(R.id.llOperatorPwd);
        LinearLayout llOperatorName = (LinearLayout) findViewById(R.id.llOperatorName);
        LinearLayout llOperatorTel = (LinearLayout) findViewById(R.id.llOperatorTel);
        final ImageView ivDividerOperatorId = (ImageView) findViewById(R.id.ivDividerOperatorId);
        final ImageView ivDividerOperatorPwd = (ImageView) findViewById(R.id.ivDividerOperatorPwd);
        final ImageView ivDividerOperatorName = (ImageView) findViewById(R.id.ivDividerOperatorName);
        final ImageView ivDividerOperatorTel = (ImageView) findViewById(R.id.ivDividerOperatorTel);
        if (llOperatorId != null
                && llOperatorPwd != null
                && llOperatorName != null
                && llOperatorTel != null
                && ivDividerOperatorId != null
                && ivDividerOperatorPwd != null
                && ivDividerOperatorName != null
                && ivDividerOperatorTel != null){
            llOperatorId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etLoginNo.setSelection(etLoginNo.getText().length());
                    etLoginNo.requestFocus();
                }
            });
            etLoginNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ivDividerOperatorId.setPressed(hasFocus);
                    ivDividerOperatorPwd.setPressed(false);
                    ivDividerOperatorName.setPressed(false);
                    ivDividerOperatorTel.setPressed(false);
                }
            });
            llOperatorPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etPassword.setSelection(etPassword.getText().length());
                    etPassword.requestFocus();
                }
            });
            etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ivDividerOperatorId.setPressed(false);
                    ivDividerOperatorPwd.setPressed(hasFocus);
                    ivDividerOperatorName.setPressed(false);
                    ivDividerOperatorTel.setPressed(false);
                }
            });
            llOperatorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etName.setSelection(etName.getText().length());
                    etName.requestFocus();
                }
            });
            etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ivDividerOperatorId.setPressed(false);
                    ivDividerOperatorPwd.setPressed(false);
                    ivDividerOperatorName.setPressed(hasFocus);
                    ivDividerOperatorTel.setPressed(false);
                }
            });
            llOperatorTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etContaceWay.setSelection(etContaceWay.getText().length());
                    etContaceWay.requestFocus();
                }
            });
            etContaceWay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ivDividerOperatorId.setPressed(false);
                    ivDividerOperatorPwd.setPressed(false);
                    ivDividerOperatorName.setPressed(false);
                    ivDividerOperatorTel.setPressed(hasFocus);
                }
            });
        }
    }
}
