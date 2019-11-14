package com.wizarpos.pay.cashier.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.inf.MemberTransaction;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.adapter.TickeUsedAdapter;
import com.wizarpos.pay.view.adapter.TickeUsedAdapter.OnProcessListener;
import com.wizarpos.pay2.lite.R;

/**
 * 会员券核销
 * @author wu
 *
 */
public class MixMembertTicketActivity extends TransactionActivity implements MemberTransaction.MemberTransactionListener,OnProcessListener {
	private MemberTransaction transcation;

	private TickeUsedAdapter ticketUsedAdapter = null;
	private ListView listView;
	String initAmount = "";
	private List<TicketInfo> allMemberTickets = new ArrayList<TicketInfo>(); // 所有券
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		transcation = TransactionFactory.newMemberTransaction(this);
		initView();
		progresser.showProgress();
		transcation.handleIntent(getIntent());
		ticketUsedAdapter.setInitAmount(transcation.getInitAmount());
		ticketUsedAdapter.setAddTicketList(transcation.getAddTicketList());
	}

	private void initView() {
		setMainView(R.layout.ticket_used_activity);
		setTitleText("组合支付|会员券");
		showTitleBack();
		setTitleRight("确定");
		ticketUsedAdapter = new TickeUsedAdapter(this);
		ticketUsedAdapter.setProcessListener(this);
		listView = (ListView) findViewById(R.id.lv_ticket_used);
		listView.setAdapter(ticketUsedAdapter);
	}
	

	@Override
	protected void onTitleRightClicked() {
		progresser.showProgress();
		LogEx.d("组合支付普通券核销", "start");
		List<TicketInfo> selectedTickets = ticketUsedAdapter.getSelectedTicketInfos();
		transcation.passMixMemberTickets(selectedTickets, new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				setResult(RESULT_OK, (Intent)response.result);
				MixMembertTicketActivity.this.finish();
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(MixMembertTicketActivity.this, response.msg);
			}
		});
	}

	@Override
	public void onInit(Response response) {
		progresser.showContent();
		if (response.code == 0) {
			if (response.result != null) {
				allMemberTickets.addAll((List<TicketInfo>) response.getResult());
				ticketUsedAdapter.setDataChanged(allMemberTickets);
//				ticketUsedAdapter.ticketList = allMemberTickets;
//				listView.setAdapter(ticketUsedAdapter);
//				ticketUsedAdapter.notifyDataSetChanged();
			}
			
		} else {
			UIHelper.ToastMessage(this, response.msg);
			MixMembertTicketActivity.this.finish();
		}

	}

	@Override
	public void showProcess() {
		/**TicketUsedAdapter 回调显示进度 */
		progresser.showProgress();
	}

	@Override
	public void showContent() {
		/**TicketUsedAdapter 回调显示内容 */
		progresser.showContent();
	}
}
