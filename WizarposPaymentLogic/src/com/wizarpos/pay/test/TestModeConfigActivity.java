package com.wizarpos.pay.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.wizarpos.pay.cashier.pay_tems.tenpay.TenpayConstants;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * 测试配置
 * 
 * @author wu
 */
public class TestModeConfigActivity extends Activity {

	private TextView tv;

	private static final String _test_set_load_default_merchant_data = "_test_set_load_default_merchant_data";

	private static final String _test_set_load_default_bat = "_test_set_load_default_bat";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setContentView(layout, params);

		tv = new TextView(this);
		layout.addView(tv);

		final CheckBox ckNotWizarPos = new CheckBox(this);
		ckNotWizarPos.setText("是否使用默认的商户配置");
		ckNotWizarPos.setChecked(Constants.TRUE.equals(AppConfigHelper.getConfig(_test_set_load_default_merchant_data)));
		ckNotWizarPos.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ckNotWizarPos.setChecked(isChecked);
				AppConfigHelper.setConfig(_test_set_load_default_merchant_data, isChecked ? Constants.TRUE
						: Constants.FALSE);
				if (isChecked) {
					AppConfigHelper.setConfig(AppConfigDef.merchantName, "无锡慧银微信小店");
					AppConfigHelper.setConfig(AppConfigDef.mid, "100105100000001");// 单独测试
					AppConfigHelper.setConfig(AppConfigDef.fid, "100105100000001");// 单独测试
					AppConfigHelper.setConfig(AppConfigDef.operatorNo, "00");
					AppConfigHelper.setConfig(AppConfigDef.sn, "WP13432000012345");// 单独测试
				} else {
					AppConfigHelper.setConfig(AppConfigDef.merchantId, "");
					AppConfigHelper.setConfig(AppConfigDef.merchantName, "");
					AppConfigHelper.setConfig(AppConfigDef.mid, "");
					AppConfigHelper.setConfig(AppConfigDef.fid, "");
					AppConfigHelper.setConfig(AppConfigDef.operatorNo, "");
					AppConfigHelper.setConfig(AppConfigDef.sn, android.os.Build.SERIAL);
					AppConfigHelper.setConfig(AppConfigDef.terminalId, "");
				}
				updateTv();
			}
		});
		layout.addView(ckNotWizarPos);

		final CheckBox ckInitBAT = new CheckBox(this);
		ckInitBAT.setText("BAT配置参数注入");
		ckInitBAT.setChecked(Constants.TRUE.equals(AppConfigHelper.getConfig(_test_set_load_default_bat)));
		ckInitBAT.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ckInitBAT.setChecked(isChecked);
				if (isChecked) {
					// 支付宝
					AppConfigHelper.setConfig(AppConfigDef.alipay_pattern, "2088811093409397");// 无锡慧银
					AppConfigHelper.setConfig(AppConfigDef.alipay_key, "3qexfy21kauhr1tvdjgi6y4tw8lsu0um");
					AppConfigHelper.setConfig(AppConfigDef.alipay_agent_id, "11864042a1");
					AppConfigHelper.setConfig(AppConfigDef.use_alipay, "true");

					// 微信
					AppConfigHelper.setConfig(AppConfigDef.weixin_app_id, "wx1e8ca93f0efd399d");
					AppConfigHelper.setConfig(AppConfigDef.weixin_app_key, "huiyinshanghai51571526wizarpos01");
					AppConfigHelper.setConfig(AppConfigDef.weixin_mchid_id, "1227383102");
					AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "true");

					// 手Q
					AppConfigHelper.setConfig(AppConfigDef.tenpay_bargainor_id, TenpayConstants.BARGAINOR_ID);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_key, TenpayConstants.MD5_SECRET_KEY);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_id, TenpayConstants.BARGAINOR_ID);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_passwd, "111111");

					// 百度
					AppConfigHelper.setConfig(AppConfigDef.baidupay_id, "9000100005");
					AppConfigHelper.setConfig(AppConfigDef.baidupay_key, "pSAw3bzfMKYAXML53dgQ3R4LsKp758Ss");
					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.TRUE);
				} else {
					// 支付宝
					AppConfigHelper.setConfig(AppConfigDef.alipay_pattern, "");// 无锡慧银
					AppConfigHelper.setConfig(AppConfigDef.alipay_key, "");
					AppConfigHelper.setConfig(AppConfigDef.alipay_agent_id, "");
					AppConfigHelper.setConfig(AppConfigDef.use_alipay, Constants.FALSE);

					// 微信
					AppConfigHelper.setConfig(AppConfigDef.weixin_app_id, "");
					AppConfigHelper.setConfig(AppConfigDef.weixin_app_key, "");
					AppConfigHelper.setConfig(AppConfigDef.weixin_mchid_id, "");
					AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, Constants.FALSE);

					// 手Q
					AppConfigHelper.setConfig(AppConfigDef.tenpay_bargainor_id, "");
					AppConfigHelper.setConfig(AppConfigDef.tenpay_key, "");
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_id, "");
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_passwd, "");
					AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.FALSE);

					// 百度
					AppConfigHelper.setConfig(AppConfigDef.baidupay_id, "");
					AppConfigHelper.setConfig(AppConfigDef.baidupay_key, "");
					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.FALSE);
				}
			}
		});
		layout.addView(ckInitBAT);

//		final CheckBox ckLoadPrint = new CheckBox(this);
//		ckLoadPrint.setText("当前是否使用打印模块");
//		ckLoadPrint.setChecked(Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.test_use_printer)));
//		ckLoadPrint.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				ckLoadPrint.setChecked(isChecked);
//				AppConfigHelper.setConfig(AppConfigDef.test_use_printer, isChecked ? Constants.TRUE : Constants.FALSE);
//			}
//		});
//		layout.addView(ckLoadPrint);

//		final CheckBox ckSafeMode = new CheckBox(this);
//		ckSafeMode.setText("当前是否是使用安全模块");
//		ckSafeMode.setChecked(Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.test_load_safe_mode)));
//		ckSafeMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				ckSafeMode.setChecked(isChecked);
//				AppConfigHelper.setConfig(AppConfigDef.test_load_safe_mode, isChecked ? Constants.TRUE
//						: Constants.FALSE);
//				if (isChecked) {
//					DeviceManager.getInstance().getPubCertificate(new BasePresenter.ResultListener() {
//						@Override
//						public void onSuccess(Response response) {
//
//						}
//
//						@Override
//						public void onFaild(Response response) {
//
//						}
//					});
//				} else {
//					AppConfigHelper.setConfig(AppConfigDef.test_load_safe_mode, "");
//				}
//			}
//		});
		// layout.addView(ckSafeMode);

//		final CheckBox ckBankMode = new CheckBox(this);
//		ckBankMode.setText("当前是否支持刷卡");
//		ckBankMode.setChecked(Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.test_load_bank_mode)));
//		ckBankMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				ckBankMode.setChecked(isChecked);
//				AppConfigHelper.setConfig(AppConfigDef.test_load_bank_mode, isChecked ? Constants.TRUE
//						: Constants.FALSE);
//			}
//		});
		// layout.addView(ckBankMode);

		updateTv();
	}

	private void updateTv() {
		StringBuilder sb = new StringBuilder();
		sb.append("收单商户号:" + AppConfigHelper.getConfig(AppConfigDef.merchantId)).append("\n");
		sb.append("商户名称:" + AppConfigHelper.getConfig(AppConfigDef.merchantName)).append("\n");
		sb.append("慧商户号:" + AppConfigHelper.getConfig(AppConfigDef.mid)).append("\n");
		sb.append("sn:" + AppConfigHelper.getConfig(AppConfigDef.sn)).append("\n");
		sb.append("终端号:" + AppConfigHelper.getConfig(AppConfigDef.terminalId)).append("\n");
		tv.setText(sb.toString());
	}
}
