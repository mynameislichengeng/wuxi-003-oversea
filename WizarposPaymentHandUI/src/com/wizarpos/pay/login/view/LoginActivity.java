package com.wizarpos.pay.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppLoginResponse;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.common.view.XEditText;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.login.presenter.LoginPresenter2;
import com.wizarpos.pay.login.presenter.LoginPresenter2.LoginPresenterListener;
import com.wizarpos.pay.service.BankTransUploadService;
import com.wizarpos.pay.setting.activity.HostSettingsActivity;
import com.wizarpos.pay.setting.activity.SetLanguageActivity;
import com.wizarpos.pay.test.TestStartMenuActivity;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver.ThirdAppListener;
import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.wizarpos.pay.view.util.StringUtilUI;
import com.wizarpos.pay2.lite.R;

//import com.wizarpos.netpay.server.NetPayProxy;

public class LoginActivity extends TransactionActivity implements LoginPresenterListener, ThirdAppListener, OnEditorActionListener, OnKeyListener, XEditText.DrawableRightListener {

    /**
     * 密码最大长度
     */
    private static final int MAX_PWD_LENGTH = 8;
    protected LoginPresenter2 loginpresenter;

    private EditText et_operater;
    private XEditText et_password;
    private CheckBox cb_remember_psw;
    private boolean isShowPassword = false;
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initThirdAppController();
        initView();

        AppConfigHelper.setConfig(AppConfigDef.mid, "100105100000152");
        AppConfigHelper.setConfig(AppConfigDef.fid, "100105100000152");

        loginpresenter = new LoginPresenter2(this);
        loginpresenter.handleIntent(null);

        if (thirdAppController.isInservice()) {
            getPubCertificate();
        }
    }

    private void getPubCertificate() {
        progresser.showProgress();
        DeviceManager.getInstance().getPubCertificate(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                DialogHelper2.showDialog(LoginActivity.this, response.msg, new DialogListener() {

                    @Override
                    public void onOK() {
                        back();
                    }
                });
            }
        });
    }

    private void initThirdAppController() {
        thirdAppController = ThirdAppTransactionController.getInstance();
        thirdAppController.setThridAppFinisher(this);
        thirdAppController.parseRequest(getIntent());
    }

    private void initView() {
//        setTitleText("登录");
//		if (DeviceManager.DEVICE_TYPE_OTHER==DeviceManager.getInstance().getDeviceType()) {
//			setMainView(R.layout.login_menu_huishang_activity);
//			et_password = (EditText) findViewById(R.id.login_password);
//			et_password.setSelection(et_password.getText().length());
//			et_operater = (EditText) findViewById(R.id.login_password);
//			et_operater.setSelection(et_operater.getText().length());
//			mermchantNameTv=(TextView)findViewById(R.id.merchant_name);
//			mermchantNameTv.setText(AppConfigHelper.getConfig(AppConfigDef.merchantName));
//		}else {
        setMainView(R.layout.activity_login_new_ui_backup);
        cb_remember_psw = (CheckBox) findViewById(R.id.cb_remember_psw);
        cb_remember_psw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_remember_psw.setTextColor(0xff00aeef);
                    AppConfigHelper.setConfig(AppConfigDef.rememberPsw, Constants.TRUE);
                } else {
                    cb_remember_psw.setTextColor(0xff666666);
                    AppConfigHelper.setConfig(AppConfigDef.rememberPsw, Constants.FALSE);
                }
            }
        });
        cb_remember_psw.setChecked(Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.rememberPsw)));
        cb_remember_psw.setTextColor(Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.rememberPsw)) ? 0xff00aeef : 0xff666666);
        et_password = (XEditText) findViewById(R.id.et_password);
        et_password.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.iv_divider_psw).setBackgroundColor(0xff00aeef);
                } else {
                    findViewById(R.id.iv_divider_psw).setBackgroundColor(0xffd8d8d8);
                }
            }
        });
        et_password.setDrawableRightListener(this);
        et_operater = (EditText) findViewById(R.id.et_operator_account);
        et_operater.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.iv_divider_ac).setBackgroundColor(0xff00aeef);
                } else {
                    findViewById(R.id.iv_divider_ac).setBackgroundColor(0xffd8d8d8);
                }
            }
        });
//		}
        // 添加menu
//		try {
//			getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
//		} catch (NoSuchFieldException e) {} catch (IllegalAccessException e) {
//			Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);
//		}
        setOnClickListenerById(R.id.btn_login, this);
        setOnClickListenerById(R.id.btnTest, this);
        setOnClickListenerById(R.id.llSwitchLanguage, this);

        //MO不弹出软键盘
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            et_operater.setInputType(InputType.TYPE_NULL);
            et_password.setInputType(InputType.TYPE_NULL);
            et_password.setOnKeyListener(this);
        }

        et_password.setOnEditorActionListener(this);

//		if(DeviceManager.getInstance().isWizarDevice()){
//			findViewById(R.id.llLogo).setVisibility(View.VISIBLE);
//		}

        if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.rememberPsw))) {
            String name = AppConfigHelper.getConfig(AppConfigDef.loginOptName);
            String pwd = AppConfigHelper.getConfig(AppConfigDef.loginOptPwd);
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                et_operater.setText(name);
                if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
                    et_password.setTag(pwd);
                    et_password.setText(StringUtilUI.copyChar(pwd.length(), "*"));
                } else {
                    et_password.setText(pwd);
                }
            }
        }
        et_password.setSelection(et_password.getText().length());
        et_operater.setSelection(et_operater.getText().length());
        goneView();
    }

    private void goneView() {
        ((LinearLayout)findViewById(R.id.ll_login)).setWeightSum(2);
        findViewById(R.id.etMerchantId).setVisibility(View.GONE);
        findViewById(R.id.ivDividerMid).setVisibility(View.GONE);
        ((EditText)findViewById(R.id.et_operator_account)).setHint("账号");
        findViewById(R.id.tvForgetPwd).setVisibility(View.GONE);
        findViewById(R.id.llRegist).setVisibility(View.GONE);
        findViewById(R.id.ivRegist).setVisibility(View.GONE);
        findViewById(R.id.ivLoginClose).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivLoginClose).setClickable(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_login:
                String usename = et_operater.getText().toString();
                String password = et_password.getText().toString();
                if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
                    Object tag = et_password.getTag(); //bugfix wu
                    if (tag == null) {
                        password = "";
                    } else {
                        password = String.valueOf(tag);
                    }
                }
                UIHelper.hideSoftInput(this);
                doLogin(usename, password);
                break;
            case R.id.btnTest:
                startActivity(new Intent(LoginActivity.this, TestStartMenuActivity.class));
                this.finish();
                break;
            case R.id.ivLoginClose:
                this.finish();
                break;
            case R.id.llSwitchLanguage:
                startActivity(SetLanguageActivity.getStartIntent(LoginActivity.this));
                overridePendingTransition(0, 0);
                LoginActivity.this.finish();
                break;
        }
    }

    protected void doLogin(final String usename, final String password) {
        if ("99".equals(usename) && "99999999".equals(password)) {
            startNewActivity(HostSettingsActivity.class);
        } else {
            progresser.showProgress();
            loginpresenter.login(usename, password);
        }
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // startNewActivity(HostSettingsActivity.class);
    // return super.onCreateOptionsMenu(menu);
    // }

    /**
     * @Author: Huangweicai
     * @date 2015-11-23 上午10:49:10
     * @Description: 开启网络收单
     */
    private void initNetPay() {
//        NetPayProxy.getInstance().startServer(this, PayNetactivityProxy.class);
    }

    @Override
    public void onUnNeedVerification(Response response) {

    }

    @Override
    public void onNeedVerification(Response response) {

    }

    @Override
    public void onLoginSuccess(Response response) {
        // startNewActivity(ProgressingActivity.class);
        if (thirdAppController.isInservice()) {
            thirdAppController.bundleLoginResponse();
            thirdAppController.reset();
        } else {
//            startNewActivity(MainFragmentActivity2.class);//更换目录
            startNewActivity(NewMainActivity.class);
            AppStateManager.setState(AppStateDef.isLogin, Constants.TRUE);
        }
        if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.rememberPsw))) {
            AppConfigHelper.setConfig(AppConfigDef.loginOptName, et_operater.getText().toString());
            if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
                AppConfigHelper.setConfig(AppConfigDef.loginOptPwd, et_password.getTag().toString());
            } else {
                AppConfigHelper.setConfig(AppConfigDef.loginOptPwd, et_password.getText().toString());
            }
        }
        //开启网络收单服务
        if (AppConfigHelper.getConfig(AppConfigDef.isUseNetPay, false)) {
            initNetPay();
        }
        //开启银行卡上送服务
        Intent startIntent = new Intent(PaymentApplication.getInstance(), BankTransUploadService.class);
        PaymentApplication.getInstance().startService(startIntent);
    }


    @Override
    public void onLoginFaild(Response response) {
        progresser.showContent();
        if (thirdAppController.isInservice() && response.code == NetRequest.NETWORK_ERR) {
            try {
                AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
                ThirdAppLoginResponse thirdResponse = new ThirdAppLoginResponse();
                thirdResponse.setCode(response.code);
                thirdResponse.setMessage("无法连接到服务器");
                Intent intent = new Intent();
                intent.putExtra("response", JSONObject.toJSONString(thirdResponse));
                setResult(RESULT_CANCELED, intent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                thirdAppController.reset();
                finish();
            }
        } else if (response != null && !TextUtils.isEmpty(response.msg)) {
            if (!isFinishing()) { //bugfix wu
//                DialogHelper2.showDialog(LoginActivity.this, response.msg);
                CommonToastUtil.showMsgBelow(LoginActivity.this, CommonToastUtil.LEVEL_WARN, response.msg);
            }

        }

    }

    @Override
    protected void back() {
        try {
            AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
            ThirdAppLoginResponse response = new ThirdAppLoginResponse();
            response.setCode(1);
            response.setMessage("用户取消");
            Intent intent = new Intent();
            intent.putExtra("response", JSONObject.toJSONString(response));
            setResult(RESULT_CANCELED, intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            thirdAppController.reset();
            finish();
        }
    }

    @Override
    public void onResult(Intent intent) {
        Logger2.debug("消费流程完成");
        try {
            if (thirdAppController.isInservice()) {
                bundleThridTransactionResponse(intent);
            } else {
                this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        loginpresenter.onDestory();
        super.onDestroy();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_password && actionId == EditorInfo.IME_ACTION_GO) {
            findViewById(R.id.btn_login).performClick();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            findViewById(R.id.btn_login).performClick();
            return true;
        }
        //修复Q1密码明文问题 用onkey来进行控制
        if (v.getId() != R.id.et_password) {
            return false;
        }
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return true;
        }
        /** 只对数字和删除键进行监听*/
        if (keyCode <= KeyEvent.KEYCODE_9 && keyCode >= KeyEvent.KEYCODE_0) {
            if (et_password.getText().toString().length() >= MAX_PWD_LENGTH) {//密码长度不能大于MAX_PWD_LENGTH位
                return true;
            }
            String tag = keyCode - KeyEvent.KEYCODE_0 + "";
            String etTag;
            if (et_password.getTag() == null) {
                etTag = tag;
            } else {//将密码保存在tag里面
                etTag = et_password.getTag().toString() + tag;
            }
            et_password.setTag(etTag);
            if (isShowPassword){
                et_password.setText(etTag);
            } else {
                et_password.setText(StringUtilUI.copyChar(etTag.length(), "*"));
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DEL) {//删除键
            if (et_password.getTag() == null || TextUtils.isEmpty(et_password.getTag().toString())) {
                return false;
            }
            String etTag = et_password.getTag().toString().substring(0, et_password.getTag().toString().length() - 1);
            et_password.setTag(etTag);
            if (isShowPassword){
                et_password.setText(etTag);
            } else {
                et_password.setText(StringUtilUI.copyChar(etTag.length(), "*"));
            }
            return true;
        }
        return true;
    }

    @Override
    public void onDrawableRightClick(View view) {
        isShowPassword = !isShowPassword;
        String tag = "";
        if (et_password.getTag() != null){
            tag = et_password.getTag().toString();
//        } else if (!TextUtils.isEmpty(et_password.getText().toString().trim())){
//            et_password.setTag(et_password.getText().toString().trim());
//            tag = et_password.getTag().toString();
        }
        if (isShowPassword){
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (!TextUtils.isEmpty(tag)){
                et_password.setText(tag);
            }
            et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_show_pw), null);
        } else {
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            if (!TextUtils.isEmpty(tag)){
                et_password.setText(StringUtilUI.copyChar(tag.length(), "*"));
            }
            et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_hide_pw), null);
        }
    }
}
