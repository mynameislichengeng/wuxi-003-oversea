package com.wizarpos.pay.login.merchant.input;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2016-1-5 下午1:46:10
 * @Description:新建商户填表流程Activity
 */
public class NewMerChantFillFormActivity extends AppCompatActivity{
	private NewMerChantFillFormActivity instance;
	private NewMerchantFragment newMerchantFragment;
	private UploadInformationFragment uploadInformationFragment;
	private SettingsFragment settingsFragment;
	private SetBankAccountFragment setBankAccountFragment;
	public static final String NEWMERCHANT_FRAGMENT_FLAG = "1";
	public static final String UPLOADINFORMATION_FRAGMENT_FLAG = "2";
	public static final String SETTINGS_FRAGMENT_FLAG = "3";
	public static final String SETBANKACCOUNT_FRAGMENT_FLAG = "4";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_fragment);
		showUploadInformation();
	}

	public void showNewMerchant() {
		if (null == newMerchantFragment) {
			newMerchantFragment = new NewMerchantFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, newMerchantFragment).commit();
	}

	public void showUploadInformation() {
		if (null == uploadInformationFragment) {
			uploadInformationFragment = new UploadInformationFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, uploadInformationFragment).commit();
	}

	public void showSettingsFragment() {
		if (null == settingsFragment) {
			settingsFragment = new SettingsFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, settingsFragment).commit();
	}

	public void showSetBankAccountFragment() {
		if (null == setBankAccountFragment) {
			setBankAccountFragment = new SetBankAccountFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, setBankAccountFragment).commit();
	}
	
	public void toFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, fragment).commit();
	}
	
	public void toFragmentAndSave(Fragment fragment,String flag) {
		getSupportFragmentManager().beginTransaction().addToBackStack(flag).replace(R.id.layout_fragment, fragment).commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
				getSupportFragmentManager().popBackStack();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public NewMerChantFillFormActivity getInstance() {
		return instance;
	}
}
