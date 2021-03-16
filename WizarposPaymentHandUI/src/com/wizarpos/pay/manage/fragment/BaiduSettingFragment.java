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

public class BaiduSettingFragment extends Fragment {
	private Button btnPartner, btnAppKey, btnOk;
	private CheckBox tbState;
	private boolean state = true;// 开关状态
	private static String partner = "", key = "";
	private EditText etPartnerId, etAppKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hand_baudu_setting, null);
		setupView(view);
		addListener();
		return view;
	}

	private void setupView(View view) {
		btnPartner = (Button) view.findViewById(R.id.btn_hand_partner);
		btnAppKey = (Button) view.findViewById(R.id.btn_app_id);
		btnOk = (Button) view.findViewById(R.id.btn_hand_q_confirm);
		etPartnerId = (EditText) view.findViewById(R.id.et_partner_id);
		etAppKey = (EditText) view.findViewById(R.id.et_app_key);
		tbState = (CheckBox) view.findViewById(R.id.tb_baidu_state);
		if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_baidupay))) {
			tbState.setChecked(true);
		} else {
			tbState.setChecked(false);
		}
		partner = AppConfigHelper.getConfig(AppConfigDef.baidupay_id);
		key = AppConfigHelper.getConfig(AppConfigDef.baidupay_key);
		etPartnerId.setText(partner);
		etAppKey.setText(key);
		if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_baidupay))) {
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
					setWhite();
					//点击确定，保证必填参数完善后才开启@yaosong[20151104]
//					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.TRUE);
				} else {
					setGray();
					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.FALSE);
				}
			}
		});
	}

	private void setWhite() {
		etPartnerId.setBackgroundColor(getResources().getColor(R.color.white));
		etAppKey.setBackgroundColor(getResources().getColor(R.color.white));
		etPartnerId.setEnabled(true);
		etAppKey.setEnabled(true);
	}

	private void setGray() {
		etPartnerId.setBackgroundColor(getResources().getColor(R.color.gray));
		etAppKey.setBackgroundColor(getResources().getColor(R.color.gray));
		etPartnerId.setEnabled(false);
		etAppKey.setEnabled(false);
	}

	private void addListener() {
		btnPartner.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 13000);// AppId
			}
		});
		btnAppKey.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = toScan();
				startActivityForResult(intent, 13001);// key
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				partner = etPartnerId.getText().toString();
				key = etAppKey.getText().toString();
				if (tbState.isChecked()) {
					if (TextUtils.isEmpty(partner) || TextUtils.isEmpty(key)) {
						UIHelper.ToastMessage(getActivity(), "请将信息录入完整");
						return;
					}
					partner = partner.trim();
					key = key.trim();
					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.TRUE);//开启@yaosong[20151104]
					
					AppConfigHelper.setConfig(AppConfigDef.baidupay_id, partner);
					AppConfigHelper.setConfig(AppConfigDef.baidupay_key, key);
					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.TRUE);
				} else {
					AppConfigHelper.setConfig(AppConfigDef.baidupay_id, partner);
					AppConfigHelper.setConfig(AppConfigDef.baidupay_key, key);
					AppConfigHelper.setConfig(AppConfigDef.use_baidupay, Constants.FALSE);
				}
				getActivity().finish();
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 13000 && resultCode == FragmentActivity.RESULT_OK) {
			partner = data.getStringExtra(InputInfoActivity.content);
			etPartnerId.setText(partner);
		} else if (requestCode == 13001 && resultCode == FragmentActivity.RESULT_OK) {
			key = data.getStringExtra(InputInfoActivity.content);
			etAppKey.setText(key);
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
