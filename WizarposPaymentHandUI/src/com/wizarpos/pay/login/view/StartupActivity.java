package com.wizarpos.pay.login.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;

import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.login.presenter.StartupPresenter;

import com.wizarpos.pay.recode.zusao.connect.ZsConnectManager;
import com.wizarpos.pay.recode.zusao.constants.ZsConstants;
import com.wizarpos.pay.recode.zusao.sp.ZSSettingManager;
import com.wizarpos.pay.setting.util.LanguageUtils;
import com.wizarpos.pay.test.TestStartMenuActivity;
import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.motionpay.pay2.lite.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * 入口activity 闪屏
 *
 * @author wu
 */
public class StartupActivity extends BaseViewActivity {

    private final static String TAG = StartupActivity.class.getSimpleName();

    private static final int REQUEST_CODE_SETTING = 1;
    private StartupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        doRequestPermissions();
        LanguageUtils.init(this);
        super.onCreate(savedInstanceState);
        LogEx.d("START APP");
        setContentView(R.layout.layout_startup);
        DeviceManager.getInstance().getDeviceType();
        findViewById(R.id.btnTestModel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartupActivity.this, TestStartMenuActivity.class));
                StartupActivity.this.finish();
            }
        });
        ZsConnectManager.init(getIntent());
        presenter = new StartupPresenter(this);
        progresser.showProgress();
        init();

    }

    private void init() {
        Log.d("sn", AppConfigHelper.getConfig(AppConfigDef.sn));//显示sn wu
        presenter.getPubCertificate(new ResultListener() {// 取证书

            @Override
            public void onSuccess(Response response) {
                go();

            }

            @Override
            public void onFaild(Response response) {

                DialogHelper2.showDialog(StartupActivity.this, response.msg, new DialogListener() {

                    @Override
                    public void onOK() {
                        StartupActivity.this.finish();
                    }
                });

            }
        });

    }

    private void ping() {
        presenter.ping(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
                go();
            }

            @Override
            public void onFaild(Response response) {
                // UIHelper.ToastMessage(StartupActivity.this, response.msg);
                if (response.code == NetRequest.NETWORK_ERR) {
                    AppStateManager.setState(AppStateDef.isOffline, Constants.TRUE);
                    UIHelper.ToastMessage(StartupActivity.this, response.msg);
                } else {
                    AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
                }
                go();
            }
        });
    }

    private void go() {
//       String loginState =  AppStateManager.getState(AppStateDef.isLogin, Constants.FALSE);
//        Log.d("tagtagtag", loginState+"  go()");
//        if (Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin, Constants.FALSE))) {
//            Log.d("tagtagtag", "进入home界面");
//            startActivity(new Intent(this, NewMainActivity.class));
//        } else {
//            Log.d("tagtagtag", "进入登陆界面");
//            startActivity(new Intent(this, com.wizarpos.pay.login.view.LoginMerchantRebuildActivity.class));
//        }
        operateIntentConnect();

    }

    private void doRequestPermissions() {
        // 先判断是否有权限。
        if (AndPermission.hasPermission(this, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WAKE_LOCK)) {
            // 有权限，直接do anything.
        } else {
            // 申请权限。
            AndPermission.with(this)
                    .requestCode(100)
                    .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WAKE_LOCK)
                    .send();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(StartupActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
//                AndPermission.defaultSettingDialog(LoginActivity.this, REQUEST_CODE_SETTING).showFromDialog();
                // 第二种：用自定义的提示语。
                AndPermission.defaultSettingDialog(StartupActivity.this, REQUEST_CODE_SETTING)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZsConstants.INTENT_MYSELF_REQUEST_CODE && requestCode == ZsConstants.INTENT_MYSELF_RESULT_CODE) {
            Log.d("tagtagtag", TAG + "--onActivityResult()--");
//            ZsConnectManager.onActivityMyselfIntentResult(this,data);
            ZsConnectManager.connectIntentResult(this);
        }
    }

    private void operateIntentConnect() {
        if (ZsConnectManager.isZsPayType()) {
            ZsConnectManager.requestIntent(this, LoginMerchantRebuildActivity.class);
        } else {
            startActivity(new Intent(this, com.wizarpos.pay.login.view.LoginMerchantRebuildActivity.class));
            finish();
        }

    }
}
