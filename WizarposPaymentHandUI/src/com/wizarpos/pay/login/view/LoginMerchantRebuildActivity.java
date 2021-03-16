package com.wizarpos.pay.login.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.PreferenceHelper;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.broadcastreceiver.Alarmreceiver;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyRequest;
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
import com.wizarpos.pay.login.merchant.input.AgreementActivity;
import com.wizarpos.pay.login.merchant.input.ForgetPasswordActivity;
import com.wizarpos.pay.login.merchant.input.NewMerChantFillFormActivity;
import com.wizarpos.pay.login.presenter.LoginPresenter2;
import com.wizarpos.pay.model.LoginedMerchant;
import com.wizarpos.pay.push.presenter.PushPresenter;
import com.wizarpos.pay.setting.activity.HostSettingsActivity;
import com.wizarpos.pay.setting.activity.SetLanguageActivity;
import com.wizarpos.pay.test.TestStartMenuActivity;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver.ThirdAppListener;
import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.ActionSheetDialog;
import com.wizarpos.pay.view.ArrayItem;
import com.wizarpos.pay.view.util.ChooseItemDialogHelper;
import com.wizarpos.pay.view.util.CommonDigistKeyListener;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.wizarpos.pay.view.util.VcodeDialogFragment;
import com.motionpay.pay2.lite.R;
import com.wizarpos.recode.util.PackageAndroidManager;

import java.util.ArrayList;
import java.util.List;

//import com.wizarpos.netpay.server.NetPayProxy;

/**
 * 商户终端体系改造后登陆,界面改造后
 */
public class LoginMerchantRebuildActivity extends TransactionActivity implements LoginPresenter2.LoginPresenterListener, ThirdAppListener, OnEditorActionListener, XEditText.DrawableRightListener {

    /**
     * 密码最大长度
     */
    private static final int MAX_PWD_LENGTH = 18;
    protected LoginPresenter2 loginpresenter;

    private EditText et_operater, etMerchantId;
    private XEditText et_password;
    private TextView tvMerchantId;
    private CheckBox cb_remember_psw;
    private TextView tvForgetPwd, tv_version;
    private ImageView ivLoginClose;
    private Button btn_login;
    private boolean isShowPassword = false;

    //由于登录前无法获取数据库信息，从Sp中读取相关数据
    private PreferenceHelper spHelper;
    /**
     * Map
     * key:商户名 value:mid
     */
    private List<LoginedMerchant> loginedMerchants = new ArrayList<>();
    private String lastLoginId, lastPasswd, lastLoginMid;
    private boolean isRemember;
    private ActionSheetDialog actionSheetDialog;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginMerchantRebuildActivity.class);
        return intent;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginMerchantRebuildActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity) context).startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initThirdAppController();
        loginpresenter = new LoginPresenter2(this);
        loginpresenter.handleIntent(null);
        loadLoginedMerchant();
        initView();
        if (thirdAppController.isInservice()) {
            getPubCertificate();
        } else {
            autoLogin();
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
                DialogHelper2.showDialog(LoginMerchantRebuildActivity.this, response.msg, new DialogListener() {

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

    /**
     * 读取登陆过的商户信息（只有商户号和商户名信息）
     */
    private void loadLoginedMerchant() {
        try {
            spHelper = new PreferenceHelper(this, AppConfigDef.SP_loginedInfo);
            lastLoginId = spHelper.getString(AppConfigDef.SP_lastLoginId, null);
            lastPasswd = spHelper.getString(AppConfigDef.SP_lastPasswd, null);
            lastLoginMid = spHelper.getString(AppConfigDef.SP_lastLoginMid, null);
            isRemember = spHelper.getBoolean(AppConfigDef.SP_isRemember, false);
            String oldMerchants = spHelper.getString(AppConfigDef.SP_loginedMerchant, "[]");
            loginedMerchants = JSONArray.parseArray(oldMerchants, LoginedMerchant.class);
            loginpresenter.setLocalMerchants(loginedMerchants);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {
//        setTitleText("登录");
        setMainView(R.layout.activity_login_new_ui);
        cb_remember_psw = findViewById(R.id.cb_remember_psw);
        cb_remember_psw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_remember_psw.setTextColor(0xff0074e1);
                    spHelper.putBoolean(AppConfigDef.SP_isRemember, true);
                } else {
                    cb_remember_psw.setTextColor(0xff888888);
                    spHelper.putBoolean(AppConfigDef.SP_isRemember, false);
                }
            }
        });
        cb_remember_psw.setChecked(isRemember);
        cb_remember_psw.setTextColor(isRemember ? 0xff0074e1 : 0xff888888);
        setOnClickListenerById(R.id.tvForgetPwd, this);
        setOnClickListenerById(R.id.ivLoginClose, this);
        setOnClickListenerById(R.id.tvMerchantId, this);
        //登陆
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
//        setOnClickListenerById(R.id.btn_login, this);
        setOnClickListenerById(R.id.btnRegist, this);
        setOnClickListenerById(R.id.btnTest, this);
        setOnClickListenerById(R.id.btnSwitchLanguage, this);
        //版本号
        tv_version = findViewById(R.id.tv_version);
        tv_version.setText(PackageAndroidManager.getVersionName(this));
        //
        initUserEt();
    }

    private void initUserEt() {
        et_password = (XEditText) findViewById(R.id.et_password);
        et_password.setDrawableRightListener(this);
        et_password.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                findViewById(R.id.llPwdInput).setPressed(hasFocus);
            }
        });
        et_operater = (EditText) findViewById(R.id.et_operator_account);
        et_operater.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                findViewById(R.id.llUserNameInput).setPressed(hasFocus);
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_operater.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
        //商户终端体系改造
        etMerchantId = (EditText) findViewById(R.id.etMerchantId);
        etMerchantId.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                findViewById(R.id.llMidInput).setPressed(hasFocus);
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etMerchantId.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
        tvMerchantId = (TextView) findViewById(R.id.tvMerchantId);
        if (loginedMerchants.size() == 0) {
            etMerchantId.setVisibility(View.VISIBLE);
            tvMerchantId.setVisibility(View.GONE);
        } else {
            etMerchantId.setVisibility(View.GONE);
            tvMerchantId.setVisibility(View.VISIBLE);
//				String lastMid = AppConfigHelper.getConfig(AppConfigDef.loginMid);
            if (TextUtils.isEmpty(lastLoginMid)) {
                tvMerchantId.setText(loginedMerchants.get(0).getMerchantName());
            } else {
                for (LoginedMerchant bean : loginedMerchants) {
                    if (bean.getMid().equals(lastLoginMid)) {
                        tvMerchantId.setText(bean.getMerchantName());
                    }
                }
            }
        }
        //MO不弹出软键盘
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            etMerchantId.setInputType(InputType.TYPE_NULL);
            et_operater.setInputType(InputType.TYPE_NULL);
//            et_password.setInputType(InputType.TYPE_NULL);
//            et_password.setOnKeyListener(this);
        }
        et_password.setOnEditorActionListener(this);
        et_password.setKeyListener(new CommonDigistKeyListener(false, R.string.english_only_can_input));
        et_operater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_operater.setSelection(et_operater.getText().toString().length());
            }
        });
        if (isRemember) {
            String name = lastLoginId;
            String pwd = lastPasswd;
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                et_operater.setText(name);
                if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0 || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
//                    et_password.setTag(pwd);
                    et_password.setText(pwd);
                } else {
                    et_password.setText(pwd);
                }
            }
        }
        et_password.setSelection(et_password.getText().length());
        et_operater.setSelection(et_operater.getText().length());
    }

    /**
     * 自动登陆
     */
    private void autoLogin() {
        //如果是记住密码
        if (isRemember) {
            //是否是通过点击sign-out方式退出，如果是sign-out方式退出则不要自动登陆，默认为FALSE
            String isFlag = AppStateManager.getState(AppStateDef.IS_SIGN_OUT_EXIT, Constants.FALSE);
            if (!Constants.TRUE.equals(isFlag)) {
                this.btn_login.performClick();
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_login:
                final String usename = et_operater.getText().toString();
                String password = et_password.getText().toString();
                UIHelper.hideSoftInput(this);
                Log.d("TAG", "username is :" + usename + " password is :" + password);
                //商户终端体系改造
                String mid = "";
                if (etMerchantId.getVisibility() == View.VISIBLE) {
                    mid = etMerchantId.getText().toString().trim();
                } else {
                    for (LoginedMerchant bean : loginedMerchants) {
                        if (bean.getMerchantName().equals(tvMerchantId.getText().toString().trim())) {
                            mid = bean.getMid();
                        }
                    }
                }
                if (TextUtils.isEmpty(mid)) {
                    CommonToastUtil.showMsgAbove(this, CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.Enter_merchan_num));
                    return;
                }
                Log.d("商户号", "mid:" + mid);
                lastLoginMid = mid;
                doLogin(usename, password, mid);
                break;
            case R.id.btnTest:
                startActivity(new Intent(LoginMerchantRebuildActivity.this, TestStartMenuActivity.class));
                this.finish();
                break;
            case R.id.btnRegist:
                MerchantApplyNetMgr.getInstants().initData();//单例模式 初始化
                doAddMerchantQuery();
                break;
            case R.id.tvMerchantId:
                showLoginedMid();
                break;
            case R.id.tvForgetPwd:
                //忘记密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.ivLoginClose:
                //退出登录
                this.finish();
                break;
            case R.id.btnSwitchLanguage:
                //语言切换
                startActivity(SetLanguageActivity.getStartIntent(LoginMerchantRebuildActivity.this));
                overridePendingTransition(0, 0);
                LoginMerchantRebuildActivity.this.finish();
                break;

        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent(LoginMerchantRebuildActivity.this, Alarmreceiver.class);
        intent.setAction(Constants.UPDATE);
        PendingIntent sender = PendingIntent.getBroadcast(LoginMerchantRebuildActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.cancel(sender);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 24 * 60 * 60 * 1000, sender);
    }

    private void showLoginedMid() {

        if (loginedMerchants.size() > 0) {
            //显示供选择的登陆过的商户 yaosong
            List<ArrayItem> items = new ArrayList<>(loginedMerchants.size());
            for (int i = 0; i < loginedMerchants.size(); i++) {
                ArrayItem item = new ArrayItem();
                item.setShowValue(loginedMerchants.get(i).getMerchantName());
                item.setRealValue(i);
                items.add(item);
            }
            ArrayItem item = new ArrayItem();
            item.setShowValue(getResources().getString(R.string.Enter_other_merchant_num));
            item.setRealValue(-1);
            items.add(item);
            if (null == actionSheetDialog || !actionSheetDialog.isShowing()) {
                actionSheetDialog = new ChooseItemDialogHelper().createArrayItemSheetDialog(this, items, getResources().getString(R.string.Please_select_merchan), new ActionSheetDialog.ActionSheetListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onClick(ArrayItem item) {
                        if (item.getRealValue() < loginedMerchants.size() && item.getRealValue() != -1) {
                            tvMerchantId.setText(loginedMerchants.get(item.getRealValue()).getMerchantName());
                        } else {
                            etMerchantId.setVisibility(View.VISIBLE);
                            tvMerchantId.setVisibility(View.GONE);
                        }
                    }
                });
                actionSheetDialog.setCurrentValue(0 + "");
                actionSheetDialog.show();
            }
        }
    }

    protected void doLogin(final String usename, final String password, final String mid) {
        if ("99".equals(usename) && "99999999".equals(password) && Constants.DEBUGING) {
            startNewActivity(HostSettingsActivity.class);
        } else {
            progresser.showProgress();
//            loginpresenter.setUnNeedVerification(true);//TODO 外文版设置不请求119接口 日后可能去除此逻辑
            loginpresenter.login(usename, password, mid);
        }
    }

    @Override
    public void onUnNeedVerification(Response response) {
        progresser.showContent();
        if (!TextUtils.isEmpty(response.getMsg()) && response.getCode() != 0) {
            CommonToastUtil.showMsgAbove(this, CommonToastUtil.LEVEL_INFO, response.getMsg());
        }
        LoginMerchantRebuildActivity.this.findViewById(R.id.btn_login).performClick();
    }

    @Override
    public void onNeedVerification(Response response) {
        progresser.showContent();
        //验证码弹窗  yaosong [20160301]
        final VcodeDialogFragment fragment = VcodeDialogFragment.newInstance(response.getMsg(), loginpresenter.isNeedResetPassword());
        fragment.setmVcodeListener(new VcodeDialogFragment.VcodeListener() {

            @Override
            public void onVcodeInput(String vcode, String newPwd, String newPwdConfirm) {
                if (TextUtils.isEmpty(vcode)) {
                    CommonToastUtil.showMsgAbove(LoginMerchantRebuildActivity.this, CommonToastUtil.LEVEL_WARN, "输入信息不能为空");
//                    fragment.dismiss();
//                    fragment.showFromDialog(LoginMerchantRebuildActivity.this.getSupportFragmentManager(), "showVcodeDialogFragment");
                    return;
                }
                loginpresenter.setVerifeCode(vcode);
                if (!loginpresenter.isNeedResetPassword()) {
                    LoginMerchantRebuildActivity.this.findViewById(R.id.btn_login).performClick();
                } else {
                    if (!TextUtils.isEmpty(newPwd) && newPwd.equals(newPwdConfirm)) {
                        if (loginpresenter.isNeedResetPassword() && !TextUtils.isEmpty(newPwd) && newPwd.length() < 6) {
                            CommonToastUtil.showMsgAbove(LoginMerchantRebuildActivity.this, CommonToastUtil.LEVEL_WARN, "新密码不得小于6位");
//                            fragment.dismiss();
//                            fragment.showFromDialog(LoginMerchantRebuildActivity.this.getSupportFragmentManager(), "showVcodeDialogFragment");
                            return;
                        } else {
                            loginpresenter.setNewPassword2Reset(newPwd);
                            LoginMerchantRebuildActivity.this.findViewById(R.id.btn_login).performClick();
                        }
                    } else {
                        UIHelper.ToastMessage(LoginMerchantRebuildActivity.this, TextUtils.isEmpty(newPwd) ? "输入信息不能为空" : "两次密码输入不一致");
//                        fragment.dismiss();
//                        fragment.showFromDialog(LoginMerchantRebuildActivity.this.getSupportFragmentManager(), "showVcodeDialogFragment");
                    }
                }
            }
        });
        fragment.show(this.getSupportFragmentManager(), "showVcodeDialogFragment");
    }

    @Override
    public void onLoginSuccess(Response response) {
        // startNewActivity(ProgressingActivity.class);
        AppStateManager.setState(AppStateDef.isLogin, Constants.TRUE);
        pushTerminal();
        sendBroadcast();
        if (thirdAppController.isInservice()) {
            thirdAppController.bundleLoginResponse();
            thirdAppController.reset();
        } else {
//            startNewActivity(MainFragmentActivity2.class);
            startNewActivity(NewMainActivity.class);
        }
        spHelper.putString(AppConfigDef.SP_lastLoginMid, lastLoginMid);
        if (spHelper.getBoolean(AppConfigDef.SP_isRemember, false)) {

        }
        spHelper.putString(AppConfigDef.SP_lastLoginId, et_operater.getText().toString());
        spHelper.putString(AppConfigDef.SP_lastPasswd, et_password.getText().toString());
        //开启网络收单服务
        if (AppConfigHelper.getConfig(AppConfigDef.isUseNetPay, false)) {
            initNetPay();
        }
    }

    private void pushTerminal() {
        final String clientId = Pay2Application.CLIENT_ID;
//        final String clientId = "c3c51b2bc1fd66d0fea55b75db76565e";
        if (!TextUtils.isEmpty(clientId)) {
            progresser.showProgress();
            new PushPresenter().registerTerminal(clientId, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    progresser.showContent();
                    Logger.debug("register success:" + clientId);
                    AppConfigHelper.setConfig(AppStateDef.isRegisterTerminal, Constants.TRUE);
                }

                @Override
                public void onFaild(Response response) {
                    Logger.debug("register failed:" + response.getMsg());
                    progresser.showContent();
                }
            });
        }
    }

    /**
     * @Author: Huangweicai
     * @date 2015-11-23 上午10:49:10
     * @Description: 开启网络收单
     */
    private void initNetPay() {
//        NetPayProxy.getInstance().startServer(this, PayNetactivityProxy.class);
    }

    @Override
    public void onLoginFaild(Response response) {
        progresser.showContent();
        //登陆失败 将再次访问119短信验证码接口
        loginpresenter.setUnNeedVerification(false);
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
            CommonToastUtil.showMsgAbove(LoginMerchantRebuildActivity.this, CommonToastUtil.LEVEL_WARN, response.msg);
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
        super.onDestroy();
        loginpresenter.onDestory();
        thirdAppController.destory();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_password && actionId == EditorInfo.IME_ACTION_GO) {
            findViewById(R.id.btn_login).performClick();
            return true;
        }
        return false;
    }

/*    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //修复Q1密码明文问题 用onkey来进行控制
        if(v.getId() != R.id.et_password)
        {
            return false;
        }
        if(event.getAction() != KeyEvent.ACTION_UP)
        {
            return true;
        }
        *//** 只对数字和删除键进行监听*//*
        if(keyCode <= KeyEvent.KEYCODE_9 && keyCode >= KeyEvent.KEYCODE_0)
        {
            if(et_password.getText().toString().length() >= MAX_PWD_LENGTH)
            {//密码长度不能大于MAX_PWD_LENGTH位
                return true;
            }
            String tag = keyCode - KeyEvent.KEYCODE_0 + "";
            String etTag ;
            if(et_password.getTag() == null)
            {
                etTag = tag;
            }else
            {//将密码保存在tag里面
                etTag = et_password.getTag().toString() + tag;
            }
            et_password.setTag(etTag);
            et_password.setText(StringUtilUI.copyChar(etTag.length(), "*"));
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_DEL)
        {//删除键
            if(et_password.getTag() == null || TextUtils.isEmpty(et_password.getTag().toString()))
            {
                return false;
            }
            String etTag = et_password.getTag().toString().substring(0, et_password.getTag().toString().length()-1) ;
            et_password.setTag(etTag);
            et_password.setText(StringUtilUI.copyChar(etTag.length(), "*"));
            return true;
        }
        return true;
    }*/

    /**
     * @Author: Huangweicai
     * @date 2015-11-30 下午6:09:03
     * @Description:增加新商户前验证
     */
    private void doAddMerchantQuery() {
        progresser.showProgress();
        MerchantApplyNetMgr.getInstants().merchantApplyQuery(Pay2Application.getInstance().getImei(), new ResponseListener() {
            @Override
            public void onSuccess(final Response response) {
                progresser.showContent();
                if (response.getResult() == null) {
                    startActivity(new Intent(LoginMerchantRebuildActivity.this, AgreementActivity.class));
                } else {
                    DialogHelper2.showChoiseDialog(LoginMerchantRebuildActivity.this, response.msg, new DialogHelper2.DialogChoiseListener() {

                        @Override
                        public void onOK() {
                            MerchantApplyRequest bean = (MerchantApplyRequest) response.getResult();
                            MerchantApplyNetMgr.getInstants().setApplyBean(bean);
                            startActivity(new Intent(LoginMerchantRebuildActivity.this, NewMerChantFillFormActivity.class));
                        }

                        @Override
                        public void onNo() {

                        }
                    });

                }

            }

            @Override
            public void onFaild(Response response) {
                int errorCode = response.code;
                switch (errorCode) {
                    case 150://该终端已经绑定过商户,是否确认申请新商户
                        DialogHelper2.showChoiseDialog(LoginMerchantRebuildActivity.this, response.msg, new DialogHelper2.DialogChoiseListener() {

                            @Override
                            public void onOK() {
                                startActivity(new Intent(LoginMerchantRebuildActivity.this, AgreementActivity.class));
                            }

                            @Override
                            public void onNo() {

                            }
                        });
                        break;
                    default:
                        DialogHelper2.showDialog(LoginMerchantRebuildActivity.this, response.msg);
                        break;
                }
                progresser.showContent();
            }
        });

    }

    @Override
    public void onDrawableRightClick(View view) {
        isShowPassword = !isShowPassword;
        String tag = "";
        if (et_password.getText().toString() != null) {
            tag = et_password.getText().toString();
//        } else if (!TextUtils.isEmpty(et_password.getText().toString().trim())){
//            et_password.setTag(et_password.getText().toString().trim());
//            tag = et_password.getTag().toString();
        }
        if (isShowPassword) {
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (!TextUtils.isEmpty(tag)) {
                et_password.setText(tag);
            }
            et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_show_pw), null);
        } else {
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            if (!TextUtils.isEmpty(tag)) {
//                et_password.setText(StringUtilUI.copyChar(tag.length(), "*"));
//            }
            et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_hide_pw), null);
        }
    }
}
