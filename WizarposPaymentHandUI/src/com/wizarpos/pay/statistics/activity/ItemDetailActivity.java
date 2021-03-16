package com.wizarpos.pay.statistics.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.statistics.adapter.MixDetialAdapter;
import com.motionpay.pay2.lite.R;

public class ItemDetailActivity extends BaseViewActivity {

	private MixDetialAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		showTitleBack();
		setTitleText("交易详细");
		setMainView(R.layout.fargment_item_mix_detail);
		ListView lvDetial = (ListView) findViewById(R.id.lvDetail);
		adapter = new MixDetialAdapter(this);
		lvDetial.setAdapter(adapter);
		List<String[]> mixTrans = (List<String[]>) getIntent().getSerializableExtra("MIXTRANS");
		adapter.setDataChanged(mixTrans);
	}

}
