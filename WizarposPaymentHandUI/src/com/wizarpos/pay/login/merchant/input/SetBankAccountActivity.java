package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.cashier.merchant_apply.base.MerchantApplyConstants;
import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyRequest;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.motionpay.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-26 下午2:03:05
 * @Description:	提现账户设置
 * 废弃
 */
public class SetBankAccountActivity extends BaseViewActivity{

	private EditText et_account_name,et_account_bank,et_account_id;
	
	private String merchantRegisterName,openBankAccount,openBankNo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		checkNotify();
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-1 上午10:44:27  
	 * @Description:是否是修改数据,取决于服务端的 {@link MerchantApplyConstants #STATUS_NOTIFY} 状态
	 */
	private void checkNotify() 
	{
		if(MerchantApplyNetMgr.getInstants().isApplyBeanNull())
		{
			return;
		}
		MerchantApplyRequest applyBean = MerchantApplyNetMgr.getInstants().getApplyBean();
		et_account_name.setText(applyBean.getMerchantRegisterName());
		et_account_bank.setText(applyBean.getOpenBankAccount());
		et_account_id.setText(applyBean.getOpenBankNo());
	}

	private void initView() {
		setTitleText("提现账户设置");
		setMainView(R.layout.activity_set_bankaccount);
		setOnClickListenerById(R.id.btn_next, this);
		setOnClickListenerById(R.id.btn_cancle, this);
		et_account_name = (EditText) this.findViewById(R.id.et_account_name);
		et_account_bank = (EditText) this.findViewById(R.id.et_account_bank);
		et_account_id = (EditText) this.findViewById(R.id.et_account_id);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_next:
			if(matchInfo())
			{
				doSubmit();
			}
			break;
		case R.id.btn_cancle:
			finish();
			break;
		}
	}
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-1 下午7:56:38  
	 * @Description:提交数据
	 */
	private void doSubmit()
	{
		progresser.showProgress();
		MerchantApplyNetMgr.getInstants().merchantApplyRegister(new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				Toast.makeText(SetBankAccountActivity.this, "提交申请成功。请等候审核通过，审核结果会以短信的形式发送至填写的手机" + MerchantApplyNetMgr.getInstants().getApplyBean().getBizTel(), Toast.LENGTH_LONG).show();
				progresser.showContent();
				SetBankAccountActivity.this.finish();
			}
			
			@Override
			public void onFaild(Response response) {
				DialogHelper2.showDialog(SetBankAccountActivity.this, response.msg);
				progresser.showContent();
			}
		});
	}
	
	private boolean matchInfo()
	{
		merchantRegisterName = et_account_name.getText().toString();
		openBankAccount = et_account_bank.getText().toString();
		openBankNo = et_account_id.getText().toString();
		if(TextUtils.isEmpty(merchantRegisterName) || TextUtils.isEmpty(openBankAccount)
				|| TextUtils.isEmpty(openBankNo))
		{
			Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		MerchantApplyNetMgr.getInstants().getApplyBean().setMerchantRegisterName(merchantRegisterName);
		MerchantApplyNetMgr.getInstants().getApplyBean().setOpenBankAccount(openBankAccount);
		MerchantApplyNetMgr.getInstants().getApplyBean().setOpenBankNo(openBankNo);
		return true;
	}
}
