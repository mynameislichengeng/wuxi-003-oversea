package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.LoginResp;
import com.wizarpos.pay.model.RefundOperResp;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay.view.util.Tools;
import com.wizarpos.pay2.lite.R;

import java.util.HashMap;
import java.util.Map;


public class InputPassWordActivity extends BaseViewActivity implements OnMumberClickListener {
    private String password = null;
    private EditText etPassword;
    private AppConfiger present;
    private static final String TAG = InputPassWordActivity.class.getSimpleName();
    public static final String ORDER_NO = "order";//交易撤销
    private InputPadFragment inputPadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        present = new AppConfiger(this);
        setMainView(R.layout.input_password);
        setTitleText(getResources().getString(R.string.input_password_title));
        showTitleBack();
        etPassword = (EditText) findViewById(R.id.input_password);
        etPassword.setSelection(etPassword.getText().length());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        if (DeviceManager.getInstance().getDeviceType() != DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
            ((FrameLayout) ((FrameLayout) findViewById(R.id.flInputPad))).setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
            inputPadFragment.setOnMumberClickListener(this);
            inputPadFragment.setEditView(etPassword, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_NORMAL);
        }

    }

    @Override
    public void onSubmit() {
        password = etPassword.getText().toString();
        if (password == null) {
            return;
        }
        Log.e(TAG, password);
        password = password.replace("\n", "").toString().trim();
        checkPassword(password);
//        if (present.checkSecurityPassword(password)) {
//            setResult(RESULT_OK);
//            this.finish();
//        }
//        else {
//            UIHelper.ToastMessage(InputPassWordActivity.this, getResources().getString(R.string.security_code_error), Toast.LENGTH_SHORT);
//        }
    }

    private void checkPassword(String password) {
        progresser.showProgress();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("refundPwd", Tools.md5(password));
        NetRequest.getInstance().addRequest(Constants.SC_960_REFUND, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                if (0 == response.code) {
                    RefundOperResp resp= JSONObject.parseObject(response.getResult().toString(), RefundOperResp.class);
                    AppConfigHelper.setConfig(AppConfigDef.refundOperId, resp.getRefundOperId());
                    AppConfigHelper.setConfig(AppConfigDef.refundOperName, resp.getRefundOperName());
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                Logger2.debug(JSONObject.toJSONString(response));
                UIHelper.ToastMessage(InputPassWordActivity.this, response.msg, Toast.LENGTH_SHORT);
                AppConfigHelper.setConfig(AppConfigDef.refundOperId, "");
                AppConfigHelper.setConfig(AppConfigDef.refundOperName, "");
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == keyCode) {
            password = etPassword.getText().toString();
            if (password == null) {
                return true;
            }
            Log.e(TAG, password);
            password = password.replace("\n", "").toString().trim();
            checkPassword(password);
//            if (present.checkSecurityPassword(password)) {
//                setResult(RESULT_OK);
//                this.finish();
//            }
//            else {
//                UIHelper.ToastMessage(InputPassWordActivity.this, getResources().getString(R.string.security_code_error), Toast.LENGTH_SHORT);
//            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
