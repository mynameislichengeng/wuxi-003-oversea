package com.wizarpos.pay.cashier.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.view.adapter.TransDataAdapter;
import com.wizarpos.pay2.lite.R;

public class ShowQueryDataActivity extends BaseViewActivity {
	private StatisticsPresenter presenter;
	private ListView listView;
	private TransDataAdapter transDataAdapter;
	private LayoutInflater inflater;
	private List<String[]> recordListShow;
	LinearLayout noData;
	private int tranCode = 0, timeRange = 0;
	String startTime = null, endTime = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		addListener();

	}

	private void initView() {
		setMainView(R.layout.show_query_data_activity);
		setTitleText(getResources().getString(R.string.shou_kuan_huizong));
		showTitleBack();
		presenter = new StatisticsPresenter(this);
		noData = (LinearLayout) findViewById(R.id.ll_null);
		Intent intent = getIntent();
		Bundle result = intent.getExtras();
		tranCode = Integer.parseInt((String) result.get("transType"));
		timeRange = Integer.parseInt((String) result.get("transTime"));
		startTime = (String) result.get("startDate");
		endTime = (String) result.get("endDate");
		listView = (ListView) findViewById(R.id.lv_listRecode);
		transDataAdapter = new TransDataAdapter(this);
		listView.setAdapter(transDataAdapter);
	}

	private void addListener() {
		recordListShow = new ArrayList<String[]>();
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		progresser.showProgress();
		presenter.transactionGroupQuery(tranCode, timeRange, startTime, endTime, new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				GroupQueryResp result = (GroupQueryResp) response.result;
				recordListShow = result.getRecordListShow();
				transDataAdapter.setDataChanged(recordListShow);
				noData.setVisibility(View.GONE);
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				noData.setVisibility(View.VISIBLE);
			}
		});

	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
	}

}
