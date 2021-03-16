package com.wizarpos.pay.login.merchant.input;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.cashier.merchant_apply.base.MerchantApplyConstants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yaosong
 * @date 2015-11-27 上午9:42:24
 * @Description:选择要开通的第三方支付
 * 废弃
 */
public class SettingsActivity extends BaseViewActivity{
	
	private RadioGroup rg_payment_way;
	private CheckBox cb_alipay,cb_wechatpay,cb_qqpay,cb_baidupay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setTitleText("选择要开通的第三方支付");
		setMainView(R.layout.activity_settings_merchant);
		setOnClickListenerById(R.id.btn_next, this);
		setOnClickListenerById(R.id.btn_cancle, this);
		rg_payment_way = (RadioGroup) this.findViewById(R.id.rg_payment_way);
		cb_alipay = (CheckBox) this.findViewById(R.id.cb_alipay);
		cb_wechatpay = (CheckBox) this.findViewById(R.id.cb_wechatpay);
		cb_qqpay = (CheckBox) this.findViewById(R.id.cb_qqpay);
		cb_baidupay = (CheckBox) this.findViewById(R.id.cb_baidupay);
		
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_next:
			checkInfo();
			startActivity(new Intent(this, SetBankAccountActivity.class));
			this.finish();
			break;
		case R.id.btn_cancle:
			finish();
			break;
		}
	}
	
	private void checkInfo()
	{
		List<String> thirdPayRange = new ArrayList<String>();
		String thirdPayType = "";
		int checkedId = rg_payment_way.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.id.rb_my_param://自有参数
			thirdPayType = "1";
			break;
		case R.id.rb_apply_third_pay://正常申请第三方支付
			thirdPayType = "2";
			break;
		case R.id.rb_platform_agent://平台代收模式
			thirdPayType = "3";
			break;

		default:
			break;
		}
		MerchantApplyNetMgr.getInstants().getApplyBean().setThirdPayType(thirdPayType);
		if(cb_alipay.isChecked())
		{
			thirdPayRange.add(MerchantApplyConstants.PAY_RANGE_ALI);
		}
		if(cb_wechatpay.isChecked())
		{
			thirdPayRange.add(MerchantApplyConstants.PAY_RANGE_WEPAY);
		}
		if(cb_baidupay.isChecked())
		{
			thirdPayRange.add(MerchantApplyConstants.PAY_RANGE_BAIDU);
		}
		if(cb_qqpay.isChecked())
		{
			thirdPayRange.add(MerchantApplyConstants.PAY_RANGE_QQ);
		}
		MerchantApplyNetMgr.getInstants().getApplyBean().setThirdPayRange(thirdPayRange.toString());
	}
	
}
