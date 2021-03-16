package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.manager.presenter.CashierManager;
import com.motionpay.pay2.lite.R;

import java.util.List;

public class AddCashierManagerActivity extends BaseViewActivity {
    CashierManager cashierManager;
    EditText etLoginNo, etPassword, etName, etContaceWay;
    ToggleButton tbManager;
    String longNum = "", passWord = "", name = "", contactWay = "";
    int state = 0;

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
        setTitleText(getResources().getString(R.string.add_chashier_manger));
        showTitleBack();
        setTitleRight("保存");
        int[] btnIds = {R.id.btn_add_confirm};
        setOnClickListenerByIds(btnIds, this);

        // M0隐藏软键盘 wu   MO硬件键盘不能识别已经输入的内容,故去除 wu
//		if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0
//				|| DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
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
            case R.id.btn_add_confirm:
                longNum = etLoginNo.getText().toString();
                passWord = etPassword.getText().toString();
                name = etName.getText().toString();
                contactWay = etContaceWay.getText().toString();
                if (TextUtils.isEmpty(longNum) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(name)) {
                    UIHelper.ToastMessage(this, "请将信息补充完整");
                    return;
                }
                //bugfix @yaosong [20160105]
                if (contactWay.length() < 11) {
                    UIHelper.ToastMessage(this, "手机号格式不正确");
                    return;
                }
                List<UserBean> uList = cashierManager.getAllCashier();
                if (TextUtils.isEmpty(longNum)) {
                    UIHelper.ToastMessage(AddCashierManagerActivity.this, "登录名不能为空！");
                    return;
                }
                for (int i = 0; i < uList.size(); i++) {
                    if (uList.get(i).getOperId().equals(longNum)) {
                        UIHelper.ToastMessage(AddCashierManagerActivity.this, "登录名相同，请重新输入");
                        return;
                    }
                }
                if (tbManager.isChecked()) {
                    state = 1;
                } else {
                    state = 0;
                }
                UserBean user = new UserBean();
                user.setOperId(longNum);
                user.setPassword(passWord);
                user.setRealName(name);
                user.setPhone(contactWay);
                user.setType(state);
                cashierManager.addCashier(user);
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
