package com.wizarpos.pay.manage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay2.lite.R;

public class WepaySettingFragment extends Fragment {
	Button btnConfirm;
	Button btnappId, btnAppKey, btnMchidId;
	// RadioButton wechatPay,bussineeModle;//受理商模式[暂时不使用微商]
	private CheckBox wechatPay;
	RadioGroup radioGroup;
	EditText etappId, etAppKey, etMchidId;
	AppConfiger settingPresent;
	static String appId = "", appKey = "", mchid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onCreate(savedInstanceState);
		settingPresent = new AppConfiger(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.wechat_setting, null);
		setupView(view);
		addListener();
		return view;
	}

	private void setupView(View view) {
		btnConfirm = (Button) view.findViewById(R.id.btn_wechatpay_confirm);
		btnappId = (Button) view.findViewById(R.id.btn_wechatpay_appid);
		btnAppKey = (Button) view.findViewById(R.id.btn_wechatpay_appkey);
		btnMchidId = (Button) view.findViewById(R.id.btn_mcdhid);
		wechatPay = (CheckBox) view.findViewById(R.id.wechat_state);
		etappId = (EditText) view.findViewById(R.id.et_wechatpay_appid);
		etAppKey = (EditText) view.findViewById(R.id.wechatpay_appkey);
		etMchidId = (EditText) view.findViewById(R.id.et_mchid_id);
		appId = AppConfigHelper.getConfig(AppConfigDef.weixin_app_id);
		appKey = AppConfigHelper.getConfig(AppConfigDef.weixin_app_key);
		mchid = AppConfigHelper.getConfig(AppConfigDef.weixin_mchid_id);
		etappId.setText(appId);
		etAppKey.setText(appKey);
		etMchidId.setText(mchid);
		// bussineeModle=(RadioButton)view.findViewById(R.id.wechat_bussiness_modle);[微商暂时不用]
		// radioGroup=(RadioGroup) view.findViewById(R.id.radio_group);
		/**
		 * 微信支付
		 */
		if (AppConfigHelper.getConfig(AppConfigDef.use_weixin_pay).equals("true")) {
			wechatPay.setChecked(true);
			setWhite();
			// bussineeModle.setChecked(false);
		} else if (AppConfigHelper.getConfig(AppConfigDef.xinpay_agent_pay).equals("true")) {
			// wechatPay.setChecked(false);
			// bussineeModle.setChecked(true);
		} else {
			setGray();
			wechatPay.setChecked(false);
			// bussineeModle.setChecked(false);
		}
		wechatPay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (wechatPay.isChecked()) {
					//点击确定，保证必填参数完善后才开启@yaosong[20151104]
//					AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "true");
					setWhite();
				} else {
					setGray();
					AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "false");
				}
			}
		});
		// radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		// if (checkedId==R.id.wechat_state) {
		// wechatPay.setChecked(true);
		// bussineeModle.setChecked(false);
		// settingPresent.modifyWepayUseable(true);
		// AppConfigHelper.setConfig(AppConfigDef.xinpay_agent_pay, "false");
		// AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "true");
		// }else if(checkedId==R.id.wechat_bussiness_modle) {
		// settingPresent.modifyWepayUseable(false);
		// bussineeModle.setChecked(true);
		// wechatPay.setChecked(false);
		// AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "false");
		// AppConfigHelper.setConfig(AppConfigDef.xinpay_agent_pay, "true");
		// }else {
		// settingPresent.modifyWepayUseable(false);
		// bussineeModle.setChecked(false);
		// wechatPay.setChecked(false);
		// AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "false");
		// AppConfigHelper.setConfig(AppConfigDef.xinpay_agent_pay, "false");
		// }
		//
		// }
		// });
	}
	private void setWhite() {
		etappId.setBackgroundColor(getResources().getColor(R.color.white));
		etAppKey.setBackgroundColor(getResources().getColor(R.color.white));
		etMchidId.setBackgroundColor(getResources().getColor(R.color.white));
		etappId.setEnabled(true);
		etAppKey.setEnabled(true);
		etMchidId.setEnabled(true);
	}
	private void setGray() {
		etappId.setBackgroundColor(getResources().getColor(R.color.gray));
		etAppKey.setBackgroundColor(getResources().getColor(R.color.gray));
		etMchidId.setBackgroundColor(getResources().getColor(R.color.gray));
		etappId.setEnabled(false);
		etAppKey.setEnabled(false);
		etMchidId.setEnabled(false);
	}

	private void addListener() {
		btnappId.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 13000);// appId
			}
		});
		btnAppKey.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 13001);// key
			}
		});
		btnMchidId.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 13002);// mchid
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String appId = etappId.getText().toString(), 
					   appKey = etAppKey.getText().toString(), 
					   mchid = etMchidId.getText().toString();
				if (wechatPay.isChecked()) {
					if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(mchid)) {
						UIHelper.ToastMessage(getActivity(), "请将信息录入完整");
						return;
					}
					appId = appId.trim();
					appKey = appKey.trim();
					mchid = mchid.trim();
					AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, "true");//开启@yaosong[20151104]
					settingPresent.modifyWepayConfig(appId, appKey, mchid);
				}else {
					settingPresent.modifyWepayConfig(appId, appKey, mchid);
				}
					getActivity().finish();
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 13000 && resultCode == FragmentActivity.RESULT_OK) {
			appId = data.getStringExtra(InputInfoActivity.content); 
			etappId.setText(appId);
		} else if (requestCode == 13001 && resultCode == FragmentActivity.RESULT_OK) {
			appKey = data.getStringExtra(InputInfoActivity.content); 
			etAppKey.setText(appKey);
		} else if (requestCode == 13002 && resultCode == FragmentActivity.RESULT_OK) {
			mchid = data.getStringExtra(InputInfoActivity.content);
			etMchidId.setText(mchid);
		}
	}
	
	private Intent toScan() {
		Intent intent = new Intent(getActivity(), InputInfoActivity.class);
		intent.putExtra(InputInfoActivity.TITLE, "扫描");
		intent.putExtra(InputInfoActivity.IS_USE_SWIPE, false);
		intent.putExtra(InputInfoActivity.IS_USE_TEXT, false);
		return intent;
	}
}
