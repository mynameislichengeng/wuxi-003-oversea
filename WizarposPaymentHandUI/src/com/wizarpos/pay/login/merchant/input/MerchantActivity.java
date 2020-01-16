//package com.wizarpos.pay.login.merchant.input;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wizarpos.base.net.BaseRequest.ResponseListener;
//import com.wizarpos.base.net.Response;
//import com.wizarpos.pay.MainFragmentActivity2;
//import com.wizarpos.pay.app.Pay2Application;
//import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
//import com.wizarpos.pay.cashier.merchant_apply.base.MerchantApplyConstants;
//import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyRequest;
//import com.wizarpos.pay.common.Constants;
//import com.wizarpos.pay.common.base.BaseViewActivity;
//import com.wizarpos.pay.db.AppConfigDef;
//import com.wizarpos.pay.db.AppConfigHelper;
//import com.wizarpos.pay.db.AppStateDef;
//import com.wizarpos.pay.db.AppStateManager;
//import com.wizarpos.pay.login.presenter.LoginPresenter2.LoginPresenterListener;
//import com.wizarpos.pay.login.presenter.MerchantLoginPresenter;
//import com.wizarpos.pay.view.util.DialogHelper2;
//import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
//import com.wizarpos.pay2.lite.R;
//
///**
// * @Author: yaosong
// * @date 2015-11-26 下午2:03:05
// * @Description:	慧商收款
// */
//public class MerchantActivity extends BaseViewActivity implements LoginPresenterListener{
//
//	private EditText et_password;
//	private EditText et_merchantId;
//	private TextView tv_merchant_id;
//	private String username,passwd;
//	private MerchantLoginPresenter loginPresenter;
//	private CheckBox cb_issavepwd,cb_auto_login;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		initView();
//		loginPresenter = new MerchantLoginPresenter(this);
//		loginPresenter.handleIntent(null);
//		checkLable();
//	}
//
//	private void initView() {
//		setTitleText("慧商收款");
//		setMainView(R.layout.activity_merchant_login);
//		et_password = (EditText) findViewById(R.id.et_account_password);
//		et_password.setSelection(et_password.getText().length());
//		et_merchantId = (EditText) findViewById(R.id.et_account_login_name);
//		tv_merchant_id = (TextView) findViewById(R.id.tv_merchant_id);
//		et_merchantId.setSelection(et_merchantId.getText().length());
//		cb_issavepwd = (CheckBox) this.findViewById(R.id.cb_issavepwd);
//		cb_auto_login = (CheckBox) this.findViewById(R.id.cb_auto_login);
//		setOnClickListenerById(R.id.tv_forget_pwd, this);
//		setOnClickListenerById(R.id.btn_login, this);
//		setOnClickListenerById(R.id.btn_addto_merchant, this);
//		setOnClickListenerById(R.id.btn_new_merchant, this);
//	}
//
//	private void checkLable()
//	{
//		cb_auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(isChecked)
//				{//若点击自动登录,保存密码自动勾选上
//					cb_issavepwd.setChecked(true);
//				}
//			}
//		});
//
//		username = AppConfigHelper.getConfig(AppConfigDef.loginName,"");
//		if(AppConfigHelper.getConfig(AppConfigDef.isSavePwd,false))
//		{//保存密码
//			cb_issavepwd.setChecked(true);
//			passwd = AppConfigHelper.getConfig(AppConfigDef.loginPwd,"");
//		}
//		et_merchantId.setText(username);
//		et_password.setText(passwd);
//
//		/** 检查mid,若有mid则显示mid*/
//		String mid = AppConfigHelper.getConfig(AppConfigDef.mid, "");
//		if(TextUtils.isEmpty(mid))
//		{
//			this.findViewById(R.id.llMenuMid).setVisibility(View.INVISIBLE);
//		}else
//		{
//			tv_merchant_id.setText(mid);
//		}
//
//		/** 检查是否自动登录*/
//		if(AppConfigHelper.getConfig(AppConfigDef.isAutoLogin,false))
//		{//自动登录
//			cb_auto_login.setChecked(true);
//			doLogin();
//		}
//	}
//
//
//
//	@Override
//	public void onClick(View view) {
//		super.onClick(view);
//		switch (view.getId()) {
//		case R.id.tv_forget_pwd://忘记密码
//			startActivity(new Intent(this, ForgetPasswordActivity.class));
//			break;
//		case R.id.btn_login://登陆
//			doLogin();
//			break;
//		case R.id.btn_addto_merchant://绑定
//			doMerchantQuery();
//			break;
//		case R.id.btn_new_merchant://注册新商户
//			MerchantApplyNetMgr.getInstants().initData();//单例模式 初始化
//			doAddMerchantQuery();
//			break;
//		}
//	}
//
//	/**
//	 *
//	 * @Author: Huangweicai
//	 * @date 2015-11-30 下午6:09:03
//	 * @Description:增加新商户前验证
//	 */
//	private void doAddMerchantQuery()
//	{
//		progresser.showProgress();
//		MerchantApplyNetMgr.getInstants().merchantApplyQuery(Pay2Application.getInstance().getImei(), new ResponseListener() {
//			@Override
//			public void onSuccess(final Response response) {
//				progresser.showContent();
//				if(response.getResult() == null)
//				{
//					startActivity(new Intent(MerchantActivity.this, AgreementActivity.class));
//				}else
//				{
//					DialogHelper2.showChoiseDialog(MerchantActivity.this, response.msg, new DialogChoiseListener() {
//
//						@Override
//						public void onOK() {
//							MerchantApplyRequest bean = (MerchantApplyRequest) response.getResult();
//							MerchantApplyNetMgr.getInstants().setApplyBean(bean);
//							startActivity(new Intent(MerchantActivity.this, NewMerchantActivity.class));
//						}
//
//						@Override
//						public void onNo() {
//
//						}
//					});
//
//				}
//
//			}
//
//			@Override
//			public void onFaild(Response response) {
//				int errorCode = response.code;
//				switch (errorCode) {
//				case 150://该终端已经绑定过商户,是否确认申请新商户
//					DialogHelper2.showChoiseDialog(MerchantActivity.this, response.msg, new DialogChoiseListener() {
//
//						@Override
//						public void onOK() {
//							startActivity(new Intent(MerchantActivity.this, AgreementActivity.class));
//						}
//
//						@Override
//						public void onNo() {
//
//						}
//					});
//					break;
//				default:
//					DialogHelper2.showDialog(MerchantActivity.this, response.msg);
//					break;
//				}
//				progresser.showContent();
//			}
//		});
//
//	}
//
//	/**
//	 *
//	 * @Author: Huangweicai
//	 * @date 2015-11-30 下午3:45:53
//	 * @Description:绑定前验证
//	 */
//	private void doMerchantQuery() {
//		progresser.showProgress();
//		MerchantApplyNetMgr.getInstants().merchantApplyBindQuery(new ResponseListener() {
//
//			@Override
//			public void onSuccess(Response response) {
//				startActivity(new Intent(MerchantActivity.this, AddtoMerchantActivity.class));
//				progresser.showContent();
//			}
//
//			@Override
//			public void onFaild(Response response) {
//				if(response.getCode() == MerchantApplyConstants.ERROR_CODE_HAS_BIND)
//				{
//					DialogHelper2.showChoiseDialog(MerchantActivity.this, response.msg, new DialogChoiseListener() {
//
//						@Override
//						public void onOK() {
//							startActivity(new Intent(MerchantActivity.this, AddtoMerchantActivity.class));
//						}
//
//						@Override
//						public void onNo() {
//
//						}
//					});
//				}else
//				{
//					DialogHelper2.showDialog(MerchantActivity.this, response.msg);
//				}
//				progresser.showContent();
//			}
//		});
//	}
//
//	/**
//	 *
//	 * @Author: Huangweicai
//	 * @date 2015-11-27 下午1:44:40
//	 * @Description:登陆
//	 */
//	private void doLogin()
//	{
//		username = et_merchantId.getText().toString();
//		passwd = et_password.getText().toString();
//		progresser.showProgress();
//		loginPresenter.login(username, passwd);
//	}
//
//	@Override
//	public void onLoginSuccess(Response response) {
//		progresser.showContent();
//		AppConfigHelper.setConfig(AppConfigDef.isSavePwd, cb_issavepwd.isChecked());
//		AppConfigHelper.setConfig(AppConfigDef.isAutoLogin, cb_auto_login.isChecked());
//		AppConfigHelper.setConfig(AppConfigDef.loginName, username);
//		AppConfigHelper.setConfig(AppConfigDef.loginPwd, passwd);
//		startNewActivity(MainFragmentActivity2.class);
//		AppStateManager.setState(AppStateDef.isLogin, Constants.TRUE);
//
//	}
//
//	@Override
//	public void onLoginFaild(Response response) {
//		Toast.makeText(this, response.msg, 0).showFromDialog();
//		progresser.showContent();
//	}
//
//}
