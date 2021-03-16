package com.wizarpos.pay.manage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

/**
 * 支付设置
 * @author wu
 *
 */
public class PaymentSettingActivity extends BaseViewActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("支付设置");
		showTitleBack();
		setMainView(R.layout.activity_payment_setting);
		findViewById(R.id.llAlipaySetting).setOnClickListener(this);
		findViewById(R.id.llWepaySetting).setOnClickListener(this);
		findViewById(R.id.llTenpaySetting).setOnClickListener(this);
		findViewById(R.id.llBaiduPaySetting).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.llAlipaySetting: // 支付宝
			toSettingContainer(PaymengSettingContainerActivity.TYPE_ALIPAY, "支付宝参数配置");
			break;
		case R.id.llWepaySetting: // 微信
			toSettingContainer(PaymengSettingContainerActivity.TYPE_WEPAY, "微信参数配置");
			break;
		case R.id.llTenpaySetting: // QQ钱包
			toSettingContainer(PaymengSettingContainerActivity.TYPE_TENPAY, "QQ钱包参数配置");
			break;
		case R.id.llBaiduPaySetting: // 百度
			toSettingContainer(PaymengSettingContainerActivity.TYPE_BAIDUPAY, "百度钱包参数配置");
			break;
		default:
			break;
		}
	}

	public void toSettingContainer(int type, String title) {
		Intent intent = new Intent(this, PaymengSettingContainerActivity.class);
		intent.putExtra(PaymengSettingContainerActivity.TYPE, type);
		intent.putExtra(PaymengSettingContainerActivity.TITLE, title);
		startActivity(intent);
	}
}
