package com.wizarpos.pay.setting.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.setting.activity.ContactCustomerServiceActivity;
import com.wizarpos.pay.setting.activity.SuggestionFeedbackActivity;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.view.fragment.BaseFragment;
import com.motionpay.pay2.lite.R;

public class SettingFragment extends BaseFragment {
	private TextView tvVersionNum, tvSNum;
	private View llFeedBack, llNoteContact,llPrintDay;
	private View view;
	
	private StatisticsPresenter presenter;
	
	private final int TRAN_TYPE_TODAY = 0;
	private int transType = TRAN_TYPE_TODAY;

	@Override
	public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.setting_activity, container, false);
		setMainView(view);
		setTitleText("设置");
		showToolbar();
//		showTitleLeftImg(MerchantLogoHelper.getMerchantLogoUrl());
		setupView(view);
		addListener();

	}

	private void setupView(View view) {
		llFeedBack = view.findViewById(R.id.ll_feedback);
		llFeedBack.setVisibility(View.GONE);
		llNoteContact = view.findViewById(R.id.ll_notescontact);
		llPrintDay = view.findViewById(R.id.ll_print_day);
		tvVersionNum = (TextView) view.findViewById(R.id.tv_version_num);
		tvSNum = (TextView) view.findViewById(R.id.tv_sn_num);

		tvVersionNum.setText(getVersion());
		tvSNum.setText(AppConfigHelper.getConfig(AppConfigDef.sn, ""));

		if(Constants.SHOW_DAY_PRITN){
			if(DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0
					|| DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_OTHER)
			{
				llPrintDay.setVisibility(View.GONE);
				view.findViewById(R.id.ll_printday_line).setVisibility(View.GONE);
			}
		}else{
			llPrintDay.setVisibility(View.GONE);
			view.findViewById(R.id.ll_printday_line).setVisibility(View.GONE);
		}

	}

	private void addListener() {
		llFeedBack.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), SuggestionFeedbackActivity.class);
				getActivity().startActivity(intent);

			}
		});
		llNoteContact.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ContactCustomerServiceActivity.class);
				getActivity().startActivity(intent);

			}
		});
		llPrintDay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				printDay();
			}
		});
		// llVesionNum.setOnClickListener(new OnClickListener() {
		// public void onClick(View arg0) {
		// getVersion();
		// }
		// });

	}
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-4 下午6:59:33  
	 * @Description:打印交易汇总(日结单)
	 */
	private void printDay()
	{
		progresser.showProgress();
		presenter=new StatisticsPresenter(getActivity());
		presenter.transactionGroupQuery(-1, "", "", new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				Log.i("tag", "请求成功");
				presenter.printGroupQuery();
				progresser.showContent();
			}
			
			@Override
			public void onFaild(Response response) {
				Log.i("tag", "请求失败");
				progresser.showContent();
				Toast.makeText(SettingFragment.this.getActivity(), response.msg, 0).show();
			}
		});
	}
	

	private String getVersion() {
		try {
			PackageInfo pkg = getActivity().getPackageManager().getPackageInfo(getActivity().getApplication().getPackageName(), 0);
			return pkg.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

}
