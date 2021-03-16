package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.motionpay.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-26 下午2:03:05
 * @Description:	修改密码
 */
public class ResetPasswordActivity extends BaseViewActivity{

	private EditText et_new_password;
	private EditText et_new_password_confirm;
	private String newPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setTitleText("修改密码");
		setMainView(R.layout.activity_resetpwd_merchant);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_new_password_confirm = (EditText) findViewById(R.id.et_new_password_confirm);
		setOnClickListenerById(R.id.btn_cancle, this);
		setOnClickListenerById(R.id.btn_reset_pwd, this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_reset_pwd:
			if(matchInfo())
			{
				doChangePwd();
			}
			break;
		case R.id.btn_cancle:
			finish();
			break;
		}
	}
	
	private void doChangePwd()
	{
		MerchantApplyNetMgr.getInstants().getApplyChangePwdBean().setNewPassword(newPwd);
		progresser.showProgress();
		MerchantApplyNetMgr.getInstants().merchantChangePwd(new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				DialogHelper2.showDialog(ResetPasswordActivity.this, "修改成功", new DialogListener() {
					
					@Override
					public void onOK() {
						ResetPasswordActivity.this.finish();
					}
				});
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				DialogHelper2.showDialog(ResetPasswordActivity.this, response.msg, new DialogListener() {
					
					@Override
					public void onOK() {
						ResetPasswordActivity.this.finish();
					}
				});
			}
		});
	}
	
	private boolean matchInfo()
	{
		String pwd1 = et_new_password.getText().toString();
		String pwd2 = et_new_password_confirm.getText().toString();
		if(TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2))
		{
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!StringUtil.isSameString(pwd1, pwd2))
		{
			Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		newPwd = pwd1;
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MerchantApplyNetMgr.getInstants().onDestroy();
	}
	
}
