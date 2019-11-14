package com.wizarpos.pay.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.test.TestStartMenuActivity;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-27 下午1:48:41
 * @Description:商户进件登陆
 */
public class MerchantLoginPresenter extends LoginPresenter2{
	/** 是否是商户进件登录*/
	private boolean isMerchantLogin = false;

	public MerchantLoginPresenter(Context context) {
		super(context);
	}
	
	@Override
	protected void bundleResult() {
		super.bundleResult();
		if(isMerchantLogin)
		{
			//设置当前权限为超级管理员,不把当前账号加入本地数据库
			AppConfigHelper.setConfig(AppConfigDef.permission, "2");
		}
	}
	
	@Override
	public void login(String username, String passwd) {
		this.operatorNo = username;
		this.passwd = passwd;
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)) {
			loginPresenterListener.onLoginFaild(new Response(1, "请输入用户名密码"));
			return;
		}
		if(Constants.DEBUGING){
			if ("99".equals(username) && "122112".equals(passwd)) {
				context.startActivity(new Intent(context, TestStartMenuActivity.class));
				((Activity) context).finish();
				return;
			}
		}
		if (!cashierController.isUser(username, passwd)) { 
			if(cashierController.isUser(username))
			{
				loginPresenterListener.onLoginFaild(new Response(1, "用户密码错误"));
				return;
			}
			isMerchantLogin = true;
			// 首先验证用户名和密码,如果用户名和密码验证失败,就请求服务端
			merchantLogin(username,passwd);
			return;
		}else
		{
			isMerchantLogin = false;
			updateMerchantInfo();
			return;
		}
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午1:54:01  
	 * @Description:商户进件登陆
	 */
	private void merchantLogin(String name,String pwd)
	{
		updateMerchantInfo();
//		Map<String, Object> params = new HashMap<String,Object>();
//		params.put("bizTel", name);
//		params.put("passwd", pwd);
//		params.put("imei", PaymentApplication.getInstance().getImei());
//		NetRequest.getInstance().addRequest(Constants.SC_113_MOBILE_LOGIN, params, new ResponseListener() {
//			
//			@Override
//			public void onSuccess(Response response) {
//				//登陆成功后请求100接口获得商户信息
//				updataMerchantInfo();
//			}
//			
//			@Override
//			public void onFaild(Response response) {
//				loginPresenterListener.onLoginFaild(response);
//			}
//		});
	}

}
