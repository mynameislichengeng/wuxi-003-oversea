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

import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.motionpay.pay2.lite.R;

public class TenpaySettingFragment extends Fragment {
	private Button btnPartner, btnAppKey, btnOk;
	private CheckBox tbState;
	// private boolean state = true;// 开关状态
	private static String partner = "", key = "", optId = "", optPasswd = "";
	private EditText etPartnerId, etAppKey, etOptId, etOptPasswd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hand_q_setting, container, false);
		setupView(view);
		addListener();
		return view;
	}

	private void setupView(View view) {
		btnPartner = (Button) view.findViewById(R.id.btn_hand_partner);
		btnAppKey = (Button) view.findViewById(R.id.btn_app_id);
		view.findViewById(R.id.btn_operator_id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = toScan();
				startActivityForResult(intent, 12003);// key
			}
		});
		;
		view.findViewById(R.id.btn_operator_password).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = toScan();
				startActivityForResult(intent, 12004);// key
			}
		});
		;
		btnOk = (Button) view.findViewById(R.id.btn_hand_q_confirm);
		etPartnerId = (EditText) view.findViewById(R.id.et_partner_id);
		etAppKey = (EditText) view.findViewById(R.id.et_app_key);
		etOptId = (EditText) view.findViewById(R.id.et_opertor_id);
		etOptPasswd = (EditText) view.findViewById(R.id.et_operator_password);
		tbState = (CheckBox) view.findViewById(R.id.tb_shouq_state);
		if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_tenpay))) {
			tbState.setChecked(true);
		} else {
			tbState.setChecked(false);
		}
		partner = AppConfigHelper.getConfig(AppConfigDef.tenpay_bargainor_id);
		key = AppConfigHelper.getConfig(AppConfigDef.tenpay_key);
		optId = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_id);
		optPasswd = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_passwd);
		etPartnerId.setText(partner);
		etAppKey.setText(key);
		etOptId.setText(optId);
		etOptPasswd.setText(optPasswd);
		if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_tenpay))) {
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
//					AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.TRUE);
					setWhite();
				} else {
					AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.FALSE);
					setGray();
				}
			}
		});
	}

	private void setWhite() {
		etPartnerId.setBackgroundColor(getResources().getColor(R.color.white));
		etAppKey.setBackgroundColor(getResources().getColor(R.color.white));
		etOptId.setBackgroundColor(getResources().getColor(R.color.white));
		etOptPasswd.setBackgroundColor(getResources().getColor(R.color.white));
		etPartnerId.setEnabled(true);
		etOptId.setEnabled(true);
		etOptPasswd.setEnabled(true);
		etAppKey.setEnabled(true);
	}

	private void setGray() {
		etPartnerId.setBackgroundColor(getResources().getColor(R.color.gray));
		etAppKey.setBackgroundColor(getResources().getColor(R.color.gray));
		etOptId.setBackgroundColor(getResources().getColor(R.color.gray));
		etOptPasswd.setBackgroundColor(getResources().getColor(R.color.gray));
		etPartnerId.setEnabled(false);
		etOptId.setEnabled(false);
		etOptPasswd.setEnabled(false);
		etAppKey.setEnabled(false);
	}

	private void addListener() {
		btnPartner.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 12000);// AppId
			}
		});
		btnAppKey.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 12001);// key
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				partner = etPartnerId.getText().toString();
				key = etAppKey.getText().toString();
				optId = etOptId.getText().toString();
				optPasswd = etOptPasswd.getText().toString();
				if (tbState.isChecked()) {
					if (TextUtils.isEmpty(partner) || TextUtils.isEmpty(key) || TextUtils.isEmpty(optId) || TextUtils.isEmpty(optPasswd)) {
						UIHelper.ToastMessage(getActivity(), "请将信息录入完整");
						return;
					}
					partner = partner.trim();
					key = key.trim();
					optId = optId.trim();
					optPasswd = optPasswd.trim();
					AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.TRUE);//开启QQ钱包支付@yaosong[20151104]
					
					AppConfigHelper.setConfig(AppConfigDef.tenpay_bargainor_id, partner);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_key, key);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_id, optId);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_passwd, optPasswd);
					AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.TRUE);
				} else {
					AppConfigHelper.setConfig(AppConfigDef.tenpay_bargainor_id, partner);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_key, key);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_id, optId);
					AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_passwd, optPasswd);
					AppConfigHelper.setConfig(AppConfigDef.use_tenpay, Constants.FALSE);
				}
				getActivity().finish();
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 12000 && resultCode == FragmentActivity.RESULT_OK) {
			partner =   data.getStringExtra(InputInfoActivity.content); 
			etPartnerId.setText(partner);
		} else if (requestCode == 12001 && resultCode == FragmentActivity.RESULT_OK) {
			key =   data.getStringExtra(InputInfoActivity.content); 
			etAppKey.setText(key);
		} else if (requestCode == 12003 && resultCode == FragmentActivity.RESULT_OK) {
			key =   data.getStringExtra(InputInfoActivity.content); 
			etOptId.setText(key);
		} else if (requestCode == 12004 && resultCode == FragmentActivity.RESULT_OK) {
			key =   data.getStringExtra(InputInfoActivity.content); 
			etOptPasswd.setText(key);
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
