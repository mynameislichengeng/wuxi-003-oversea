package com.wizarpos.pay.manage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-3 上午11:29:23
 * @Description:支付设置列表
 */
public class PaymentSettingListActivity extends BaseViewActivity implements OnClickListener {

    private TextView paymentTv;
    private String SHOWREMARK = "1";
    private String NONEREMARK = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("支付设置");
        showTitleBack();
        setMainView(R.layout.activity_payment_setting_list);
        findViewById(R.id.layout_bat_Setting).setOnClickListener(this);
        findViewById(R.id.layout_remark_Setting).setOnClickListener(this);
        findViewById(R.id.layout_round_Setting).setOnClickListener(this);
        findViewById(R.id.layout_pay_item_Setting).setOnClickListener(this);
        findViewById(R.id.rlCardLinkSetting).setOnClickListener(this);
        paymentTv = (TextView) findViewById(R.id.tv_payment_setting);
        if (com.wizarpos.pay.common.Constants.OTHER_PAY_REMARK_FLAG == false) { //是否显示替他支付备注
            findViewById(R.id.layout_remark_Setting).setVisibility(View.GONE);
            AppConfigHelper.setConfig(AppConfigDef.isRemark, NONEREMARK);
        }
        if (com.wizarpos.pay.common.Constants.ROUNDDING_FLAG == false) { //是否显示四舍五入备注
            findViewById(R.id.layout_round_Setting).setVisibility(View.GONE);
            AppConfigHelper.setConfig(AppConfigDef.fen_round, com.wizarpos.pay.common.Constants.FALSE);
            AppConfigHelper.setConfig(AppConfigDef.jiao_round, com.wizarpos.pay.common.Constants.FALSE);
            AppConfigHelper.setConfig(AppConfigDef.no_round, com.wizarpos.pay.common.Constants.FALSE);
        }
        if(!Constants.CONFIG_PAY_MODE){ //是否允许配置支付方式
            findViewById(R.id.layout_pay_item_Setting).setVisibility(View.GONE);
        }
        if (Constants.BAT_FLAG) {
            paymentTv.setText("终端验证码");
        } else {
            paymentTv.setText("支付参数设置");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.layout_bat_Setting: // BAT验证码设置
                if (Constants.BAT_FLAG) {
                    intent = new Intent(PaymentSettingListActivity.this, PaymentSecurityCodeActivity.class);// 进入验证码设置界面@hong2015915
                } else {
                    intent = new Intent(PaymentSettingListActivity.this, PaymentSettingActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.layout_remark_Setting: // 备注设置
                intent = new Intent(PaymentSettingListActivity.this, RemarkSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_round_Setting: // 四舍五入设置
                intent = new Intent(PaymentSettingListActivity.this, RoundSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_pay_item_Setting: //支付方式选择
                intent = new Intent(PaymentSettingListActivity.this, PaymentItemSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.rlCardLinkSetting://收单应用配置
                startActivity(CardLinkConfigActivity.getStartActivity(this));
                break;
            default:
                break;
        }
    }
}
