package com.wizarpos.pay.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.channel.ChannelUtil;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.login.presenter.StartupPresenter;
import com.wizarpos.pay.login.view.LoginActivity;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay2.lite.R;

public class HostSettingsActivity extends BaseViewActivity {
    private EditText etHostAddress, etHostPort;
    private TextView tvHostPortError, tvServerVersion, tvChannel;
    private static String url = "", port = "";
    private AppConfiger appConfiger;
//	private LinearLayout ll_main_menu;
//	private RelativeLayout re_loading;
//	private AnimationDrawable loadingAnimation;
//	private View scanView;

    private int clickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appConfiger = new AppConfiger(this);
        url = AppConfigHelper.getConfig(AppConfigDef.ip);
        port = AppConfigHelper.getConfig(AppConfigDef.port);
        initView();
        int[] btnIds = {R.id.btn_host_setting_confirm, R.id.tvServerVersion};
        setOnClickListenerByIds(btnIds, this);
    }

    private void initView() {
        setMainView(R.layout.host_setting_activity);
        setTitleText(getResources().getString(R.string.host_setting));
        etHostAddress = (EditText) findViewById(R.id.et_host_address);
        etHostAddress.setText(url);
        etHostAddress.setSelection(etHostAddress.getText().length());
        etHostPort = (EditText) findViewById(R.id.et_host_port);
        etHostPort.setText(port);
        etHostPort.setSelection(etHostPort.getText().length());
        tvServerVersion = (TextView) findViewById(R.id.tvServerVersion);
        tvServerVersion.setText(Constants.SUFFIX_URL.substring(Constants.SUFFIX_URL.lastIndexOf("/") + 1, Constants.SUFFIX_URL.length()) + "\n" + Constants.SERVER_VERSION);
        tvHostPortError = (TextView) findViewById(R.id.tv_host_port);
        tvChannel = (TextView) findViewById(R.id.tvChannel);
        tvChannel.setText(ChannelUtil.getAPKChannelConfig(this, ChannelUtil.CHANNEL_KEY_APP_CHANNEL));
//		ll_main_menu = (LinearLayout) findViewById(R.id.host_setting_main_meun);
//		re_loading = (RelativeLayout) findViewById(R.id.loading);
//		scanView = (View) findViewById(R.id.scancrd_loading);
//		loadingAnimation = (AnimationDrawable) scanView.getBackground();
    }

    public void onClick(View view) {
        port = etHostPort.getText().toString();
        // 服务端口不一定是80
        // if (!port.equals("80")) {
        // tvHostPortError.setText(getResources().getString(R.string.host_port_erro));
        // }
        switch (view.getId()) {
            case R.id.btn_host_setting_confirm:
                url = etHostAddress.getText().toString();
                if (url != "" && port != "") {
                    appConfiger.modifyServerUrl(url, port);
                    Handler mHandler = new Handler();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
//						loadingAnimation.start();
//						re_loading.setVisibility(View.VISIBLE);
//						ll_main_menu.setVisibility(View.GONE);
                            ping();
                        }
                    });
                    break;
                }
                break;
            case R.id.tvServerVersion:
                clickCount++;
                if (clickCount < 19) {
                    return;
                }
                Intent intent = new Intent(HostSettingsActivity.this, DebugActivity.class);
                startActivity(intent);
                clickCount = 0;
                this.finish();
                break;
        }


    }

    @Override
    protected void onTitleBackClikced() {
        startNewActivity(LoginActivity.class);
        finish();
    }

    private void ping() {
        StartupPresenter presenter = new StartupPresenter(this);
        presenter.ping(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                startNewActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onFaild(Response response) {
                startNewActivity(LoginActivity.class);
                finish();
            }
        });
    }
}
