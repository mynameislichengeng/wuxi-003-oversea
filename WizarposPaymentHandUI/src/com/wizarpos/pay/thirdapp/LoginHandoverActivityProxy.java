package com.wizarpos.pay.thirdapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppLoginResponse;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.login.view.LoginActivity;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-20 上午11:02:37
 * @Description: 交接activity 跟普通登陆差不多{@link LoginActivityProxy} 需要传入操作员号 密码
 */
public class LoginHandoverActivityProxy extends LoginActivity{
	/** 操作员号*/
	private String operatorNo;
	/** 操作员密码*/
	private String operatorPwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		if(checkOperatorNo())
		{
			if(loginpresenter!=null)
			{
				doUploadTrans();
			}else {
				returnInfo("loginpresenter is null.");
			}
			
		}
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2016-1-5 上午11:44:21  
	 * @Description:上送离线数据 上送成功再进行登录 不成功则返回
	 */
	private void doUploadTrans() 
	{
		progresser.showProgress();
		loginpresenter.uploadOfflineTrans(new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				doLogin(operatorNo, operatorPwd);
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				returnInfo(response.msg);
			}
		});
	}

	private boolean checkOperatorNo() 
	{
		if(StringUtil.isSameString(operatorNo, AppConfigHelper.getConfig(AppConfigDef.operatorNo, "")))
		{
			if(StringUtil.isSameString(AppStateManager.getState(AppStateDef.isLogin),Constants.TRUE))
			{
				returnInfo("操作员号相同,不需要切换");
				return false;
			}
		}
		return true;
	}

	private void initData()
	{
//		thirdAppController.
		if(getIntent().hasExtra("operatorNo"))
		{
			operatorNo = getIntent().getStringExtra("operatorNo");
		}
		if(getIntent().hasExtra("operatorPwd"))
		{
			operatorPwd = getIntent().getStringExtra("operatorPwd");
		}
		if(TextUtils.isEmpty(operatorNo) || TextUtils.isEmpty(operatorPwd))
		{//若操作员号或密码为空
			returnInfo("操作员号或密码错误");
		}
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-20 上午11:27:16  
	 * @Description:消息返回
	 */
	private void returnInfo(String msg)
	{
		try {
			AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
			ThirdAppLoginResponse response = new ThirdAppLoginResponse();
			response.setCode(1);
			response.setMessage(msg);
			Intent intent = new Intent();
			intent.putExtra("response", JSONObject.toJSONString(response));
			setResult(RESULT_CANCELED, intent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			thirdAppController.reset();
			finish();
		}
	}
	
	@Override
	public void onLoginFaild(Response response) {
		progresser.showContent();
		returnInfo(response.msg);
	}
	
}
