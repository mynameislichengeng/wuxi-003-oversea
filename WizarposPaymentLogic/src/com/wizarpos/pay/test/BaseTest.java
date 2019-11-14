package com.wizarpos.pay.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.wizarpos.wizarpospaymentlogic.R;

public class BaseTest extends FragmentActivity {

	protected TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("cashier_payment_activity", this.getClass().getName());
//		PaymentApplication.getInstance().addActivity(this);

		setContentView(R.layout.layout_test_base);
		tvTitle = (TextView) findViewById(R.id.title);
	}

	@Override
	protected void onDestroy() {
//		PaymentApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}

}
