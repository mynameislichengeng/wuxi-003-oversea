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
import android.widget.TextView;

import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay2.lite.R;

public class AlipaySettingFragment extends Fragment {
	Button btnConfirm, btnAppId, btnKey, btnAgentId;
	TextView tvAppId, tvKey, tvAgent;
	CheckBox tbState;
	AppConfiger settingPresent;
	boolean state = true;// 开关状态
	static String appId = "", key = "", agnetId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onCreate(savedInstanceState);
		settingPresent = new AppConfiger(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.alipay_setting, null);
		setupView(view);
		addListener();
		return view;
	}

	private void setupView(View view) {
		btnAgentId = (Button) view.findViewById(R.id.btn_agentId);
		btnAppId = (Button) view.findViewById(R.id.btn_alipay_appid);
		btnKey = (Button) view.findViewById(R.id.btn_alipay_key);
		btnConfirm = (Button) view.findViewById(R.id.btn_alipay_confirm);
		tvAppId = (EditText) view.findViewById(R.id.alipay_appId);
		tvKey = (EditText) view.findViewById(R.id.alipay_key);
		tvAgent = (EditText) view.findViewById(R.id.alipay_agentId);
		appId = AppConfigHelper.getConfig(AppConfigDef.alipay_pattern);
		key = AppConfigHelper.getConfig(AppConfigDef.alipay_key);
		agnetId = AppConfigHelper.getConfig(AppConfigDef.alipay_agent_id);
		tvAppId.setText(appId);
		tvKey.setText(key);
		tvAgent.setText(agnetId);
		tbState = (CheckBox) view.findViewById(R.id.tb_alipay_state);
		if (AppConfigHelper.getConfig(AppConfigDef.use_alipay).equals("true")) {
			tbState.setChecked(true);
			setWhite();
		} else {
			tbState.setChecked(false);
			setGray();
		}
		tbState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (tbState.isChecked()) {
					//点击确定，保证必填参数完善后才开启@yaosong[20151104]
//					settingPresent.modifyAlipayUseable(true);
					setWhite();
				} else {
					settingPresent.modifyAlipayUseable(false);
					setGray();
				}
			}
		});
	}

	private void setWhite() {
		tvAppId.setBackgroundColor(getResources().getColor(R.color.white));
		tvAppId.setEnabled(true);
		tvKey.setBackgroundColor(getResources().getColor(R.color.white));
		tvKey.setEnabled(true);
		tvAgent.setBackgroundColor(getResources().getColor(R.color.white));
		tvAgent.setEnabled(true);
	}

	private void setGray() {
		tvAppId.setBackgroundColor(getResources().getColor(R.color.gray));
		tvAppId.setEnabled(false);
		tvKey.setBackgroundColor(getResources().getColor(R.color.gray));
		tvKey.setEnabled(false);
		tvAgent.setBackgroundColor(getResources().getColor(R.color.gray));
		tvAgent.setEnabled(false);
	}

	private void addListener() {
		btnAppId.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 12000);// AppId //调用摄像头逻辑调整 wu@[20150827]
			}
		});
		btnKey.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 12001);// key //调用摄像头逻辑调整 wu@[20150827]
			}
		});
		btnAgentId.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 12002);// agentId //调用摄像头逻辑调整 wu@[20150827]
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String appId = tvAppId.getText().toString(), 
					   key = tvKey.getText().toString(), 
					   agnetId = tvAgent.getText().toString();
				if (tbState.isChecked()) {
					if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(key)) {
						UIHelper.ToastMessage(getActivity(), "请将信息录入完整");
						return;
					}
					appId = appId.trim();
					key = key.trim();
					agnetId = agnetId.trim();
					settingPresent.modifyAlipayUseable(true);//开启@yaosong[20151104]
					settingPresent.modifyAlipayConfig(appId, key, agnetId);
				}else {
					settingPresent.modifyAlipayConfig(appId, key, agnetId);
				}
				getActivity().finish();
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 12000 && resultCode == FragmentActivity.RESULT_OK) {
			appId = data.getStringExtra(InputInfoActivity.content); //调用摄像头逻辑调整 wu@[20150827]
			tvAppId.setText(appId);
		} else if (requestCode == 12001 && resultCode == FragmentActivity.RESULT_OK) {
			key = data.getStringExtra(InputInfoActivity.content); //调用摄像头逻辑调整 wu@[20150827]
			tvKey.setText(key);
		} else {
			if (requestCode == 12002 && resultCode == FragmentActivity.RESULT_OK) {
				agnetId = data.getStringExtra(InputInfoActivity.content); //调用摄像头逻辑调整 wu@[20150827]
				tvAgent.setText(agnetId);
			}
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
