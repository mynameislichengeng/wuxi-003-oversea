package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.manage.fragment.AlipaySettingFragment;
import com.wizarpos.pay.manage.fragment.BaiduSettingFragment;
import com.wizarpos.pay.manage.fragment.TenpaySettingFragment;
import com.wizarpos.pay.manage.fragment.WepaySettingFragment;
import com.wizarpos.pay.setting.presenter.AppConfiger;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay2.lite.R;

/**
 * 输入验证码界面
 * @author hong
 *
 */
public class PaymentSecurityCodeActivity extends BaseViewActivity implements OnMumberClickListener{
	private EditText etPassword;
	private InputPadFragment inputPadFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setTitleText("请输入验证码");
		showTitleBack();
		setMainView(R.layout.activity_payment_security_code);		
		showTitleBack();
		etPassword = (EditText) findViewById(R.id.input_password);
		etPassword.setSelection(etPassword.getText().length());
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
		getSupportFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
		inputPadFragment.setOnMumberClickListener(this);
		inputPadFragment.setEditView(etPassword, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_NORMAL);
		String auth_code = AppConfigHelper.getConfig(AppConfigDef.auth_code);
		if (!TextUtils.isEmpty(auth_code)){
			etPassword.setText(auth_code);
		}
	}

	@Override
	public void onSubmit() {
		String secrurityCode=etPassword.getText().toString();
		if (TextUtils.isEmpty(secrurityCode)) {

			UIHelper.ToastMessage(PaymentSecurityCodeActivity.this,"请输入验证码");
			return;
		}
		AppConfigHelper.setConfig(AppConfigDef.auth_code, secrurityCode);
		PaymentSecurityCodeActivity.this.finish();
	}
}
