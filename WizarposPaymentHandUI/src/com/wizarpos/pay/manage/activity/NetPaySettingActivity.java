package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.motionpay.pay2.lite.R;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-9 上午9:56:30
 * @Description:网络收单配置界面
 */
public class NetPaySettingActivity extends BaseViewActivity {

	private ToggleButton tbVoice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		setMainView(R.layout.activity_netpay_setting_list);
		setTitleText("网络收单设置");
		showTitleBack();
		
		tbVoice = (ToggleButton) this.findViewById(R.id.tbVoice);
		tbVoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateDb(isChecked);
			}
		});
	}
	
	private void initData()
	{//默认打开语音选项
		tbVoice.setChecked(AppConfigHelper.getConfig(AppConfigDef.isNeedVoice, true));
		
	}
	
	private void updateDb(boolean isChecked)
	{
		AppConfigHelper.setConfig(AppConfigDef.isNeedVoice, isChecked);
	}


}
