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
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-26 下午2:03:05
 * @Description:	提现账户设置
 */
public class SetBankAccountFragment extends BaseViewFragment{

	private EditText et_account_name,et_account_bank,et_account_id;
	
	private String merchantRegisterName,openBankAccount,openBankNo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
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
		if(MerchantApplyNetMgr.getInstants().isApplyBeanNull() || mainView == null)
		{
			return;
		}
		MerchantApplyRequest applyBean = MerchantApplyNetMgr.getInstants().getApplyBean();
		et_account_name.setText(applyBean.getMerchantRegisterName());
		et_account_bank.setText(applyBean.getOpenBankAccount());
		et_account_id.setText(applyBean.getOpenBankNo());
	}

	public void initView() {
		setTitleText("提现账户设置");
		setMainView(R.layout.activity_set_bankaccount);
		mainView.findViewById(R.id.btn_next).setOnClickListener(this);
		mainView.findViewById(R.id.btn_cancle).setOnClickListener(this);
		et_account_name = (EditText) mainView.findViewById(R.id.et_account_name);
		et_account_bank = (EditText) mainView.findViewById(R.id.et_account_bank);
		et_account_id = (EditText) mainView.findViewById(R.id.et_account_id);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_next:
			if(matchInfo())
			{
				doSubmit();
//				getActivity().finish();
			}
			break;
		case R.id.btn_cancle:
			getActivity().finish();
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
				Toast.makeText(getActivity(), "提交申请成功", Toast.LENGTH_SHORT).show();
				progresser.showContent();
				getActivity().finish();
			}
			
			@Override
			public void onFaild(Response response) {
				DialogHelper2.showDialog(getActivity(), (response.msg != null)?response.msg:"未知异常");
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
			Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		MerchantApplyNetMgr.getInstants().getApplyBean().setMerchantRegisterName(merchantRegisterName);
		MerchantApplyNetMgr.getInstants().getApplyBean().setOpenBankAccount(openBankAccount);
		MerchantApplyNetMgr.getInstants().getApplyBean().setOpenBankNo(openBankNo);
		return true;
	}
}
