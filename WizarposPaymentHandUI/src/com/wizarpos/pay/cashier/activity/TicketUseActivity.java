package com.wizarpos.pay.cashier.activity;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.view.adapter.TickeUsedAdapter;
import com.wizarpos.pay.view.adapter.TickeUsedAdapter.OnProcessListener;
import com.motionpay.pay2.lite.R;

public class TicketUseActivity extends BaseViewActivity implements OnProcessListener{
	private TickeUsedAdapter ticketUsedAdapter = null;
	private ListView listView;
	private String initAmount = "";
	private List<TicketInfo> allMemberTickets; // 所有券

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		showTitleBack();
	}

	private void initView() {
		setMainView(R.layout.ticket_used_activity);
		setTitleText(getResources().getString(R.string.ticket));
		showTitleBack();
		setTitleRight("确定");
		initAmount = getIntent().getStringExtra("initAmount");
		ticketUsedAdapter = new TickeUsedAdapter(this);
		ticketUsedAdapter.setProcessListener(this);
		ticketUsedAdapter.setInitAmount(initAmount);
		listView = (ListView) findViewById(R.id.lv_ticket_used);
		listView.setAdapter(ticketUsedAdapter);
	}

	private void initData() {
		allMemberTickets = (List<TicketInfo>) getIntent().getSerializableExtra("allTickets"); // 所有会员券
		ticketUsedAdapter.setDataChanged(allMemberTickets);
	}

	@Override
	protected void onTitleRightClicked() {
		Intent intent = getIntent();
		List<TicketInfo> ticketList = ticketUsedAdapter.getSelectedTicketInfos();
		intent.putExtra("addCommonTicket", (Serializable) ticketList);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void showProcess() {
		progresser.showProgress();
	}

	@Override
	public void showContent() {
		progresser.showContent();
	}

}
