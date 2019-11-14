package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.cardlink.model.CardLinkConfigBean;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay2.lite.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wu on 16/3/25.
 */
public class NewCardLinkSettingActivity extends BaseViewActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, NewCardLinkSettingActivity.class);
    }

    private TextView tvMid, tvTid, tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("收单参数下载");
        showTitleBack();
        setMainView(R.layout.activity_new_cardlink_setting);
        tvMid = (TextView) findViewById(R.id.tvMid);
        tvTid = (TextView) findViewById(R.id.tvTid);
        tvCode = (TextView) findViewById(R.id.tvCode);
        findViewById(R.id.btnDownload).setOnClickListener(this);

        updateView();
    }

    private void updateView() {
        tvMid.setText(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_MECHANTID));
        tvTid.setText(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_TERMINALID));
        tvCode.setText(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_AUTHCODE));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnDownload:
                download();
                break;
        }
    }

    private void download() {
        progresser.showProgress();
        Map<String, Object> params = new HashMap<String, Object>();
        NetRequest.getInstance().addRequest(Constants.SC_1000_CARD_LINK, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                CardLinkConfigBean cardLinkConfigBean = JSONObject.parseObject(response.getResult().toString(), CardLinkConfigBean.class);
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_AUTHCODE, cardLinkConfigBean.getAuthCode());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_MECHANTID, cardLinkConfigBean.getMechantId());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_TERMINALID, cardLinkConfigBean.getTerminalId());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_SERVERIP, cardLinkConfigBean.getServerIP());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_SERVERPORT, cardLinkConfigBean.getServerPort());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_TPDU, cardLinkConfigBean.getTpdu());
                updateView();
//                AppMsg appMsg = AppMsg.makeText(NewCardLinkSettingActivity.this, "配置发生变更后请重新下载收单秘钥并签到", AppMsg.STYLE_INFO);
//                appMsg.setParent(R.id.rlMain);
//                appMsg.show();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                Toast.makeText(NewCardLinkSettingActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
//                AppMsg appMsg = AppMsg.makeText(NewCardLinkSettingActivity.this, "配置发生变更后请重新下载收单秘钥并签到", AppMsg.STYLE_INFO);
//                appMsg.setParent(R.id.rlMain);
//                appMsg.show();
                updateView();
            }
        });
    }
}
