package com.wizarpos.pay.cashier.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.impl.PublishTicketPresenter;
import com.wizarpos.pay.cashier.presenter.ticket.impl.WemengTicketPublisher;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.adapter.TicketAdapter;
import com.wizarpos.pay.view.adapter.WememgTicketPublishAdapter;
import com.wizarpos.pay2.lite.R;

public class TicketPublishActivity extends TransactionActivity {
	private TicketAdapter commonTicketAdapter = null;

	private PublishTicketPresenter presenter;
	private List<TicketDef> publishableTicketDefs;
	/** 跳转tag 券信息*/
	public static final String TAG_TICKET_INFO = "ticketInfoTag";

	private TicketInfo wemengTicketInfo;
	private WememgTicketPublishAdapter wememgTicketPublishAdapter = null;
	private WemengTicketPublisher wemengTicketPublisher;

	private SparseArray<Bitmap> weChatQRCodes = new SparseArray<Bitmap>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		if(PaymentApplication.getInstance().isWemengMerchant()){//判断是不是微盟的商户
			wemengTicketInfo = (TicketInfo) getIntent().getSerializableExtra(Constants.wemengTicketInfo);
			if(wemengTicketInfo != null && !wemengTicketInfo.getWeiMengType().equals(TicketInfo.WEIMENT_TYPE_URL)){//如果是友盟券.直接显示券列表  友盟券只有一张wu@[20150820]
				wemengTicketInfo.setSelectedCount(1);//默认使用一张券
				wemengTicketPublisher = new WemengTicketPublisher(this,wemengTicketInfo);
				wemengTicketPublisher.handleIntent(getIntent());
				wememgTicketPublishAdapter = new WememgTicketPublishAdapter(this);
				List<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();
				ticketInfos.add(wemengTicketInfo);
				wememgTicketPublishAdapter.setDataChanged(ticketInfos);
				((ListView)findViewById(R.id.lv_ticket_publish)).setAdapter(wememgTicketPublishAdapter);
			} else {
				back();
			}
		} else{
			commonTicketAdapter = new TicketAdapter(this);
			commonTicketAdapter.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			publishableTicketDefs = (ArrayList<TicketDef>) getIntent().getSerializableExtra(TAG_TICKET_INFO);
			updateList();
			
			/** 原有逻辑 现在 在PaySuccessActivity中查询是否有券 有的话再跳转*/
//			presenter = new PublishTicketPresenter(this);
//			presenter.handleIntent(getIntent());
//			getPublishableTicket();
		}
	}

	/**
	 * 获取可发行的券定义列表
	 */
	private void getPublishableTicket() {
		progresser.showProgress();
		presenter.getPublishableTicket(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				publishableTicketDefs = (List<TicketDef>) response.result;
				updateList();
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(TicketPublishActivity.this, response.msg);
			}
		});
	}

	private void initView() {
		setMainView(R.layout.ticket_publish_activity);
		setTitleText(getResources().getString(R.string.ticket_publish));
		showTitleBack();
		setTitleRight("确定");
	}

	private void updateList() {
		if(publishableTicketDefs == null) {
			publishableTicketDefs = new ArrayList<TicketDef>();
		}
		commonTicketAdapter.ticketDefList = publishableTicketDefs;
		commonTicketAdapter.initCountArray();
		((ListView)findViewById(R.id.lv_ticket_publish)).setAdapter(commonTicketAdapter);
	}

	/**
	 * 发布券
	 */
	private void publishTicket() {
		progresser.showProgress();
		if(presenter == null)
		{
			presenter = new PublishTicketPresenter(this);
			presenter.handleIntent(getIntent());
		}
		presenter.setPublishableTicketDefs(publishableTicketDefs);
		presenter.publishTicket(commonTicketAdapter.getCountArray(), new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(TicketPublishActivity.this, "发券成功");
				back();
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(TicketPublishActivity.this, response.msg);
			}

		});
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		/*
		 * case R.id.btn_ticket_confirm:// 发布按钮 boolean selected = false; int[] amount = ticketAdapter.getCountArray(); if (amount == null) {
		 * UIHelper.ToastMessage(this, getResources().getString(R.string.no_ticket)); return; } for (int i = 0; i < amount.length; i++) { int count = amount[i];
		 * if (count > 0) { selected = true; } } if (selected == false) { UIHelper.ToastMessage(this, getResources().getString(R.string.no_publish_ticket));
		 * return; } publishTicket(); break;
		 */
		}
	}

	@Override
	protected void onTitleBackClikced() {
		if(PaymentApplication.getInstance().isWemengMerchant()){ //增加微盟逻辑.通知微盟不发券 wu@[20150925]
			noteCloseTicket();
		}else{
			super.onTitleBackClikced();
		}
	}

	@Override
	protected void onTitleRightClicked() {
		if(wemengTicketInfo == null){
			if(commonTicketAdapter == null){return; } //除bug,解决崩溃问题 wu
			boolean selected = false;
			int[] amount = commonTicketAdapter.getCountArray();
			if (amount == null) {
				UIHelper.ToastMessage(this, getResources().getString(R.string.no_ticket));
				return;
			}
			for (int i = 0; i < amount.length; i++) {
				int count = amount[i];
				if (count > 0) {
					selected = true;
				}
			}
			if (selected == false) {
				UIHelper.ToastMessage(this, getResources().getString(R.string.no_publish_ticket));
				return;
			}
			publishTicket();
		}else{//发行微盟券
			publishWemengTicket();
		}
	}

	protected void publishWemengTicket() {
		TicketInfo ticektInfo = (TicketInfo) wememgTicketPublishAdapter.getItem(0);
		if(ticektInfo == null){
			return;
		}
		if(ticektInfo.getSelectedCount() == 0){
			return;
		}
		progresser.showProgress();
		boolean isUsedWemngTicket = getIntent().getBooleanExtra(Constants.isUsedWemngTicket, false);///**先前的消费有没有用过友盟券标示*/
		wemengTicketPublisher.publishTicket(isUsedWemngTicket, new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(TicketPublishActivity.this, "发券成功");
				back();
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(TicketPublishActivity.this, response.msg);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(PaymentApplication.getInstance().isWemengMerchant()){
			noteCloseTicket();
		}else{
			super.onBackPressed();
		}
	}
	
	@Override
	protected void back() {
		serviceSuccess(getIntent());
	}
	
	
	protected void noteCloseTicket() {
		progresser.showProgress();
		if (wemengTicketPublisher==null) {//添加非空判断
			back();
			return;
		}
		wemengTicketPublisher.noteCloseTicket(new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				back();
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
//				UIHelper.ToastMessage(TicketPublishActivity.this, response.msg);
				back();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && commonTicketAdapter.isShowingPop()) {
			if (null != commonTicketAdapter) {
				commonTicketAdapter.dismissPop();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
