package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.view.View;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-27 上午9:37:48
 * @Description:协议 
 */
public class AgreementActivity extends BaseViewActivity{

	public static final int ACTION_AGREEMENT = 10001;
//	public static final String EXTRA_FLAG = "com.wizarpos.pay.login.merchant.EXTRA_FLAG";
//	public static final String EXTRA_FLAG_NEWMERCHANT = "com.wizarpos.pay.login.merchant.EXTRA_FLAG_NEWMERCHANT";
//	public static final String EXTRA_FLAG_PLATFORM_AGENT = "com.wizarpos.pay.login.merchant.EXTRA_FLAG_PLATFORM_AGENT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setTitleText("协议");
		setMainView(R.layout.activity_agreement);
		setOnClickListenerById(R.id.btn_agreement, this);
		setOnClickListenerById(R.id.btn_cancle, this);
		//TODO 注册新商户协议
	}
	

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_agreement:
//			String flag = getIntent().getStringExtra(EXTRA_FLAG);
//			if (flag!=null && flag.equals(EXTRA_FLAG_PLATFORM_AGENT)){
//				setResult(RESULT_OK);
//				finish();
//			} else {
				startNewActivity(NewMerChantFillFormActivity.class);
//			}
			break;
		case R.id.btn_cancle:
			finish();
			break;
		}
	}
}
