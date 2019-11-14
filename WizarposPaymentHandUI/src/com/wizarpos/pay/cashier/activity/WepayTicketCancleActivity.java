package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.TicketManager;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay2.lite.R;

public class WepayTicketCancleActivity extends TransactionActivity {
	private static final int REQUEST_CODE = 10001;
	private TicketManager ticketManager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setMainView(R.layout.ticket_use_activity);
		ticketManager = TicketManagerFactory.createCommonTicketManager(WepayTicketCancleActivity.this);
		setData();
	}

	private void setData() {
		setTitleText(getResources().getString(R.string.wepay_ticket_cancle));
		showTitleBack();
//		toInputView(this, getResources().getString(R.string.wepay_ticket_cancle), getIntent(), REQUEST_CODE);
		toInputView(this, getResources().getString(R.string.wepay_ticket_cancle), false, getIntent(), REQUEST_CODE);
		//替换界面@hong
//		toInputTicketView(this, getResources().getString(R.string.wepay_ticket_cancle), false, getIntent(), REQUEST_CODE);
		progresser.showProgress();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == REQUEST_CODE && arg1 == RESULT_OK) { //调用摄像头逻辑调整 wu@[20150827]
			// 券号
			try { 
				String ticketNum = arg2.getStringExtra(InputInfoActivity.content);
				if(TextUtils.isEmpty(ticketNum)){
					return;
				}
				boolean isScan = (arg2.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
				onScan(ticketNum, isScan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.finish();
		}
	}
	
	public void onScan(final String result, boolean isScan) {
		LogEx.d("微信卡券核销", "微信卡券核销开始,券号:" + result);
		ticketManager.passWepayTicket(result, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				LogEx.d("微信卡券核销","微信卡券核销成功");
				JSONObject jsonObject = JSONObject.parseObject(response.getResult().toString());
				String amount = jsonObject.getJSONObject("card").getJSONObject("cash").getString("reduce_cost");
				LogEx.d("卡券核销", "微信卡券核销返回金额" + amount);
				if ("1".equals(getIntent().getStringExtra(Constants.mixFlag))) {
					Intent intent = new Intent();
					intent.putExtra(Constants.initAmount, amount);
					intent.putExtra(Constants.realAmount, amount);
					intent.putExtra(Constants.shouldAmount, amount);
					intent.putExtra(Constants.TRANSACTION_TYPE, TransactionTemsController.TRANSACTION_TYPE_THIRD_TICKET_CANCEL);
					intent.putExtra(Constants.mixFlag, "1");
					intent.putExtra(Constants.cardNo, result);
					setResult(RESULT_OK, intent);
				} else {
					UIHelper.ToastMessage(WepayTicketCancleActivity.this, "核销成功");
				}
				WepayTicketCancleActivity.this.finish();
			}

			@Override
			public void onFaild(Response response) {
				LogEx.d("微信卡券核销","微信卡券核销失败");
				UIHelper.ToastMessage(WepayTicketCancleActivity.this, response.msg);
				WepayTicketCancleActivity.this.finish();
			}
		});
	}

}
