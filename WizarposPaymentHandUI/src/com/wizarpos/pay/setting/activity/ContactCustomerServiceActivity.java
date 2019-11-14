package com.wizarpos.pay.setting.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay2.lite.R;

public class ContactCustomerServiceActivity extends BaseViewActivity {
	LinearLayout llContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView() {
		final String telNum = getResources().getString(R.string.tel_num);
		setMainView(R.layout.contact_customer_service_activity);
		showTitleBack();
		llContact = (LinearLayout) findViewById(R.id.ll_contact);
		llContact.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + telNum));
				startActivity(phoneIntent);
				finish();
			}
		});
		setTitleText(getResources().getString(R.string.notes_contact));
		int[] btnIds = { R.id.btn_contact };
		setOnClickListenerByIds(btnIds, this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_contact:
			break;
		}
	}
}
