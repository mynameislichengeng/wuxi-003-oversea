package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.login.util.RegexUtil;
import com.wizarpos.pay.ui.TimeTextViewEx;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.motionpay.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-26 下午2:03:05
 * @Description:	找回密码
 */
public class ForgetPasswordActivity extends BaseViewActivity{

	private EditText et_merchant_id,et_telephone,et_secure_code;
	private String mid,phoneNum,secureCode;
	private TimeTextViewEx timeTvEx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setTitleText("找回密码");
		setMainView(R.layout.activity_forgetpwd_merchant);
		setOnClickListenerById(R.id.btn_forget_password_sercure, this);
		setOnClickListenerById(R.id.btn_cancle, this);
//		setOnClickListenerById(R.id.tv_secure_code_send, this);
		et_merchant_id = (EditText) this.findViewById(R.id.et_merchant_id);
		et_telephone = (EditText) this.findViewById(R.id.et_telephone);
		et_secure_code = (EditText) this.findViewById(R.id.et_secure_code);
		timeTvEx = (TimeTextViewEx) findViewById(R.id.tv_secure_code_send);
		timeTvEx.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_forget_password_sercure:
			if(matchInfo(true))
			{
				doVerify();
			}
			break;
		case R.id.tv_secure_code_send:
			if(matchInfo(false))
			{
				timeTvEx.onClick(view);
				doSendMsgCode();
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
	 * @date 2015-11-30 下午6:19:47  
	 * @Description:验证
	 */
	private void doVerify()
	{
		progresser.showProgress();
		MerchantApplyNetMgr.getInstants().merchantVerify(mid, phoneNum, secureCode, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				MerchantApplyNetMgr.getInstants().getApplyChangePwdBean().setBizTel(phoneNum);
				MerchantApplyNetMgr.getInstants().getApplyChangePwdBean().setMid(mid);
				startNewActivity(ResetPasswordActivity.class);
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				DialogHelper2.showDialog(ForgetPasswordActivity.this, response.msg, new DialogListener() {
					
					@Override
					public void onOK() {
						
					}
				});
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 下午5:50:12  
	 * @Description:发送短信验证
	 */
	private void doSendMsgCode()
	{
		MerchantApplyNetMgr.getInstants().sendMsgCode(mid, phoneNum, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				Toast.makeText(ForgetPasswordActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFaild(Response response) {
				timeTvEx.init();
				Toast.makeText(ForgetPasswordActivity.this, response.msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 下午5:52:06 
	 * @param needCheckCode 是否需要检查验证码
	 * @return 
	 * @Description:验证格式
	 */
	private boolean matchInfo(boolean needCheckCode)
	{
		mid = et_merchant_id.getText().toString();
		phoneNum = et_telephone.getText().toString();
		secureCode = et_secure_code.getText().toString();
		if(TextUtils.isEmpty(mid) || TextUtils.isEmpty(phoneNum) )
		{
			Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(needCheckCode)
		{
			if(TextUtils.isEmpty(secureCode))
			{
				Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(!RegexUtil.isMobile(phoneNum))
		{
			Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		timeTvEx.onDestroy();
		super.onDestroy();
	}
	
}
