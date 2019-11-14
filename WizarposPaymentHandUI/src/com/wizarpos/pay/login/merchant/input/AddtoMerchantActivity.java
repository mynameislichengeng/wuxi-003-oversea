package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.cashier.merchant_apply.entity.MobileMerchantBindRequest;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.login.util.RegexUtil;
import com.wizarpos.pay.ui.TimeTextViewEx;
import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-26 下午2:03:05
 * @Description:	加入已有商户
 */
public class AddtoMerchantActivity extends BaseViewActivity{

	private EditText etPassword;
	private EditText etMerchantId;
	private EditText etSecureCode;
	private TimeTextViewEx timeTvEx;
	private String mid,phoneNum,sercureCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}


	private void initData() {
		
	}

	private void initView() {
		setTitleText("加入已有商户");
		setMainView(R.layout.activity_add_to_merchant);
		
		setOnClickListenerById(R.id.btn_addto_merchant, this);
		setOnClickListenerById(R.id.btn_cancle, this);
//		setOnClickListenerById(R.id.tv_secure_code_send, this);
		
		etMerchantId = (EditText) findViewById(R.id.et_merchant_id);
		etPassword = (EditText) findViewById(R.id.et_telephone);
		etSecureCode = (EditText) findViewById(R.id.et_secure_code);
		timeTvEx = (TimeTextViewEx) findViewById(R.id.tv_secure_code_send);
		timeTvEx.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_addto_merchant:
			if(matchInfo(true))
			{
				doSubmit();
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
			this.finish();
			break;
		}
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
				Toast.makeText(AddtoMerchantActivity.this, "发送成功", 0).show();
			}
			
			@Override
			public void onFaild(Response response) {
				Toast.makeText(AddtoMerchantActivity.this, response.msg, 0).show();
				timeTvEx.init();
			}
		});
	}
	

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-1 下午3:25:10 
	 * @param needCheckCode 是否需要检查验证码
	 * @return 
	 * @Description:验证格式
	 */
	private boolean matchInfo(boolean needCheckCode) {
		mid = etMerchantId.getText().toString();
		phoneNum = etPassword.getText().toString();
		sercureCode = etSecureCode.getText().toString();
		if(TextUtils.isEmpty(mid))
		{
			Toast.makeText(this, "商户号不能为空", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(phoneNum))
		{
			Toast.makeText(this, "手机号不能为空", 0).show();
			return false;
		}
		if(needCheckCode)
		{
			if(TextUtils.isEmpty(sercureCode))
			{
				Toast.makeText(this, "验证码不能为空", 0).show();
				return false;
			}
		}
		if(!RegexUtil.isMobile(phoneNum))
		{
			Toast.makeText(this, "手机号格式不正确", 0).show();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午3:06:51  
	 * @Description:测试提交
	 */
	private void doSubmit() 
	{
		progresser.showProgress();
		MobileMerchantBindRequest bean = new MobileMerchantBindRequest();
		bean.setBizTel(phoneNum);
		bean.setImei(Pay2Application.getInstance().getImei());
		bean.setMid(mid);
		bean.setVcode(sercureCode);
		MerchantApplyNetMgr.getInstants().merchantApplyDeviceBind(bean, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				AddtoMerchantActivity.this.finish();
			}
			
			@Override
			public void onFaild(Response response) {
				Toast.makeText(AddtoMerchantActivity.this, response.msg, 0).show();
				progresser.showContent();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		timeTvEx.onDestroy();
		super.onDestroy();
	}
	
	
}
