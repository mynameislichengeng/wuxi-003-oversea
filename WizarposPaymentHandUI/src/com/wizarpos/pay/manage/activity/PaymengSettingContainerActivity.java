package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.manage.fragment.AlipaySettingFragment;
import com.wizarpos.pay.manage.fragment.BaiduSettingFragment;
import com.wizarpos.pay.manage.fragment.TenpaySettingFragment;
import com.wizarpos.pay.manage.fragment.WepaySettingFragment;
import com.wizarpos.pay2.lite.R;

/**
 * 设置页面,加载fragment
 * @author wu
 *
 */
public class PaymengSettingContainerActivity extends BaseViewActivity {

	public static final String TITLE = "title";
	
	public static final String TYPE = "type";
	
	public static final int TYPE_ALIPAY = 1;
	public static final int TYPE_WEPAY = 2;
	public static final int TYPE_TENPAY = 3;
	public static final int TYPE_BAIDUPAY = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra(TITLE);
		setTitleText(TextUtils.isEmpty(title)?"设置":title);
		showTitleBack();
		setMainView(R.layout.activity_payment_setting_container);
		int type = getIntent().getIntExtra(TYPE, 1);
		switch (type) {
		case TYPE_ALIPAY:
			showFragment(new AlipaySettingFragment());
			break;
		case TYPE_WEPAY:
			showFragment(new WepaySettingFragment());
			break;
		case TYPE_TENPAY:
			showFragment(new TenpaySettingFragment());
			break;
		case TYPE_BAIDUPAY:
			showFragment(new BaiduSettingFragment());
			break;

		default:
			break;
		}
	}
	
	private void showFragment(Fragment fragment){
		getSupportFragmentManager().beginTransaction().add(R.id.llReplace, fragment).commit();
	}
	
}
