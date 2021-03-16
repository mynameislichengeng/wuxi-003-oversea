package com.wizarpos.pay.manage.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wizarpos.pay.cashier.activity.CancelTransactionActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.MerchantLogoHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.manage.activity.CashierManagerActivity;
import com.wizarpos.pay.manage.activity.InputPassWordActivity;
import com.wizarpos.pay.manage.activity.ModifyPasswordActivity;
import com.wizarpos.pay.manage.activity.NetPaySettingActivity;
import com.wizarpos.pay.manage.activity.OfflineHelperActivity;
import com.wizarpos.pay.manage.activity.PaymentSecurityCodeActivity;
import com.wizarpos.pay.manage.activity.PaymentSettingActivity;
import com.wizarpos.pay.manage.activity.PaymentSettingListActivity;
import com.wizarpos.pay.view.fragment.BaseFragment;
import com.motionpay.pay2.lite.R;

public class ManagerFragment extends BaseFragment {
	private LinearLayout llCancleTrans, llPassword, llCahierManager, llPaySetting, llOffline,llNetpay;
	private View mainView;

	private static final int REQUEST_PAY_SETTING = 10001;
	private static final int REQUEST_PAY_CANCEL = 10002;

	@Override
	public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.manager_activity, container, false);
		setMainView(mainView);
		setTitleText(getResources().getString(R.string.manage));
//		showTitleLeftImg(MerchantLogoHelper.getMerchantLogoUrl());
		showToolbar();
		setupView(mainView);
		addListener();

	}

	private void setupView(View view) {
		llCancleTrans = (LinearLayout) view.findViewById(R.id.ll_cancle_trans);
		llPassword = (LinearLayout) view.findViewById(R.id.ll_password);
		llCahierManager = (LinearLayout) view.findViewById(R.id.ll_cashier_manager);
		if(Constants.UNIFIEDLOGIN_FLAG){		//商户终端体系改造屏蔽操作员管理 yaosong
			llCahierManager.setVisibility(View.GONE);
		}
		llPaySetting = (LinearLayout) view.findViewById(R.id.ll_paysetting);
		llOffline = (LinearLayout) view.findViewById(R.id.ll_offline);
		llNetpay = (LinearLayout) view.findViewById(R.id.llNetpay);
	}

	private void addListener() {
		llPassword.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ModifyPasswordActivity.class);
				getActivity().startActivity(intent);
			}
		});
		llCancleTrans.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Intent intent = new Intent(getActivity(), CancelTransactionActivity.class);
				// getActivity().startActivity(intent);
				toInputPasswordActivity(REQUEST_PAY_CANCEL);
			}
		});
		llCahierManager.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Intent intent = new Intent(getActivity(), CashierManagerActivity.class);
				Intent intent = new Intent(getActivity(), CashierManagerActivity.class);
				getActivity().startActivity(intent);
			}
		});
		llPaySetting.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				toInputPasswordActivity(REQUEST_PAY_SETTING);
			}
		});
		llOffline.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), OfflineHelperActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		if(AppConfigHelper.getConfig(AppConfigDef.isUseNetPay, false)) {
			//若开启了网络收单
			llNetpay.setVisibility(View.VISIBLE);
			llNetpay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//网络收单配置
					Intent intent = new Intent(getActivity(),NetPaySettingActivity.class);
					getActivity().startActivity(intent);
				}
			});
		}
	}

	public void toInputPasswordActivity(int requestCode) {
		Intent intent = new Intent(getActivity(), InputPassWordActivity.class);
		this.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_PAY_SETTING) {
				Intent intent;
				intent = new Intent(getActivity(), PaymentSettingListActivity.class);//添加设置列表界面@yaosong[20151103]
				startActivity(intent);
			} else if (requestCode == REQUEST_PAY_CANCEL) {
				Intent intent = new Intent(getActivity(), CancelTransactionActivity.class);
				startActivity(intent);
			}
		}
	}

}
