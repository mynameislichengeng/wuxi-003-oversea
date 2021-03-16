package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.cashier.merchant_apply.base.MerchantApplyConstants;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay.view.util.AgreementDialogFragment;
import com.motionpay.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yaosong
 * @date 2015-11-27 上午9:42:24
 * @Description:选择要开通的第三方支付
 */
public class SettingsFragment extends BaseViewFragment{

	private RadioGroup rg_payment_way;
	private CheckBox cb_alipay,cb_wechatpay,cb_qqpay,cb_baidupay;
	private RadioButton rb_my_param,rb_apply_third_pay,rb_platform_agent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		checkNotify();
	}

	@Override
	public void onResume() {
		super.onResume();
		checkNotify();
	}

	public void initView() {
		setTitleText("选择要开通的第三方支付");
		setMainView(R.layout.activity_settings_merchant);
		mainView.findViewById(R.id.btn_next).setOnClickListener(this);
		mainView.findViewById(R.id.rb_platform_agent).setOnClickListener(this);
//		mainView.findViewById(R.id.btn_cancle).setOnClickListener(this);
		rg_payment_way = (RadioGroup) mainView.findViewById(R.id.rg_payment_way);
		cb_alipay = (CheckBox) mainView.findViewById(R.id.cb_alipay);
		cb_wechatpay = (CheckBox) mainView.findViewById(R.id.cb_wechatpay);
		cb_qqpay = (CheckBox) mainView.findViewById(R.id.cb_qqpay);
		cb_baidupay = (CheckBox) mainView.findViewById(R.id.cb_baidupay);
		rb_my_param = (RadioButton) mainView.findViewById(R.id.rb_my_param);
		rb_apply_third_pay = (RadioButton) mainView.findViewById(R.id.rb_apply_third_pay);
		rb_platform_agent = (RadioButton) mainView.findViewById(R.id.rb_platform_agent);
	}

	private void checkNotify()
	{
		if(MerchantApplyNetMgr.getInstants().isApplyBeanNull() || mainView == null)
		{
			return;
		}
		//第三方支付范围
		String thirdPayRange = MerchantApplyNetMgr.getInstants().getApplyBean().getThirdPayRange();
		if(!TextUtils.isEmpty(thirdPayRange))
		{
			List<String> thirdPayRangeList = JSON.parseArray(thirdPayRange, String.class);
			for(String thirdPay:thirdPayRangeList)
			{
				if(StringUtil.isSameString(thirdPay, MerchantApplyConstants.PAY_RANGE_ALI))
				{
					cb_alipay.setChecked(true);
				} else if(StringUtil.isSameString(thirdPay, MerchantApplyConstants.PAY_RANGE_WEPAY))
				{
					cb_wechatpay.setChecked(true);
				} else if(StringUtil.isSameString(thirdPay, MerchantApplyConstants.PAY_RANGE_BAIDU))
				{
					cb_baidupay.setChecked(true);
				}else if(StringUtil.isSameString(thirdPay, MerchantApplyConstants.PAY_RANGE_QQ))
				{
					cb_qqpay.setChecked(true);
				}
			}

		}

		//第三方支付商户类型 1 自有参数 2正常申请第三方支付 3 平台代收
		String thirdPayType = MerchantApplyNetMgr.getInstants().getApplyBean().getThirdPayType();
		if (thirdPayType != null){
			if(thirdPayType.equals("1"))
			{
				rg_payment_way.check(R.id.rb_my_param);
			} else if(thirdPayType.equals("2"))
			{
				rg_payment_way.check(R.id.rb_apply_third_pay);
			} else if(thirdPayType.equals("3"))
			{
				rg_payment_way.check(R.id.rb_platform_agent);
			}
		}
	}


	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
            case R.id.btn_next:
                checkInfo();
                ((NewMerChantFillFormActivity)getActivity()).toFragmentAndSave(new SetBankAccountFragment(),NewMerChantFillFormActivity.SETTINGS_FRAGMENT_FLAG);
                break;
			case R.id.rb_platform_agent:
				AgreementDialogFragment agreementDialogFragment = AgreementDialogFragment.newInstance();
				agreementDialogFragment.setmAgreemnetListener(new AgreementDialogFragment.AgreemnetListener() {
					@Override
					public void onAgree() {
						rg_payment_way.check(R.id.rb_platform_agent);
					}

					@Override
					public void onCancle() {
						rg_payment_way.clearCheck();
					}
				});
				agreementDialogFragment.show(getActivity().getSupportFragmentManager(), "showAgreement");
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
		MerchantApplyNetMgr.getInstants().getApplyBean().setThirdPayRange(JSONObject.toJSONString(thirdPayRange));
	}

}
