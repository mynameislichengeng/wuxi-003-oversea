package com.wizarpos.pay.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.wizarpos.pay.common.Constants;

public class BaseActivity extends AppCompatActivity {



	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if(!Constants.DEBUGING){
			MobclickAgent.onResume(this);
		}
//		PaymentApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!Constants.DEBUGING){
			MobclickAgent.onPause(this);
		}
	}

	@Override
	protected void onDestroy() {
//		PaymentApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}

}
