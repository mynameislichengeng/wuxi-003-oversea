package com.wizarpos.pay.cashier.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.pay_tems.bat.BatTransation;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.ticket.util.TicketManagerUtil;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.view.TransactionFlowController;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.motionpay.pay2.lite.R;

public class CommonThirdPayActivity extends TransactionFlowController implements ResultListener{
	private int TICKET_INFO = 10002;

	private BatTransation batTransaction;
	private TextView tvInitAmount, tvReduceAmount ,etRealAmount;
	private TextView btn_useTicket, btn_confirm;
	private boolean isScan = false;// 是否是扫描
	private List<String> ticketIds = new ArrayList<String>();	//仅用于支付时上传的用券id
	String ticketId = null;
	
	/** 券的扣减金额*/
	private int ticketTotalAomount;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		batTransaction = TransactionFactory.newBatTransaction(this);
		batTransaction.handleIntent(getIntent());

		initView();
//		int[] btnIds = { R.id.key_ok };
//		setOnClickListenerByIds(btnIds, this);

	}

	private void initView() {
		setMainView(R.layout.activity_before_third_pay);
		Intent intent = getIntent();
		int tranCode = intent.getIntExtra("tranCode", -1);
		showTitleBack();
		String realAmount;
		if(TextUtils.isEmpty(batTransaction.getTransactionInfo().getRealAmount())||"0".equals(batTransaction.getTransactionInfo().getRealAmount())){
			realAmount = batTransaction.getTransactionInfo().getShouldAmount();
			batTransaction.getTransactionInfo().setRealAmount(realAmount);
		}else {
			realAmount = batTransaction.getTransactionInfo().getRealAmount();
		}
		tvInitAmount = (TextView) findViewById(R.id.tv_thirdpay_init_amount);
		tvReduceAmount = (TextView) findViewById(R.id.tv_third_pay_reduce);
		etRealAmount = (TextView) findViewById(R.id.et_third_pay_real_amount);
		tvInitAmount.setText(Calculater.formotFen(batTransaction.getTransactionInfo().getInitAmount()));
		tvReduceAmount.setText(Calculater.formotFen(batTransaction.getTransactionInfo().getReduceAmount()));
		etRealAmount.setText(Calculater.formotFen(realAmount));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		btn_useTicket = (TextView) findViewById(R.id.btn_useTicket);
		btn_useTicket.setOnClickListener(this);
		btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		switch (tranCode) {
		case TransactionTemsController.TRANSACTION_TYPE_ALIPAY:
			intent.setClass(this, AlipayMicroActivity.class);
			setTitleText(getResources().getString(R.string.alipay));
			break;
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY:
			intent.setClass(this, WepayMicroActivity.class);
			setTitleText(getResources().getString(R.string.wepay));
			break;
		case TransactionTemsController.TRANSACTION_TYPE_TEN_PAY:
			intent.setClass(this, QwalletMicroActivity.class);
			setTitleText(getResources().getString(R.string.q_wallet_title));
			break;
		case TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY:
			intent.setClass(this, BaiduMicroPayActivity.class);
			setTitleText(getResources().getString(R.string.bai_du_pay));
			break;
		case TransactionTemsController.TRANSACTION_TYPE_UNION_PAY:
			intent.setClass(this, UnionPayMicroActivity.class);
			btn_useTicket.setVisibility(View.GONE);
			setTitleText("移动支付");
			break;
		default:
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_useTicket:
			useTicket();
			break;
		case R.id.btn_confirm:
			toTrans();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == TICKET_INFO && arg1 == RESULT_OK) { //调用摄像头逻辑调整 wu@[20150827]
			// 券号
			try { 
				String ticketNum = arg2.getStringExtra(InputInfoActivity.content);
				if(TextUtils.isEmpty(ticketNum)){
					return;
				}
				isScan = (arg2.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
//				ticketNum = arg2.getStringExtra("ticketNo");
//				scanTicketNo = arg2.getStringExtra("scanTicketNo");
				getTicketInfo(ticketNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private void getTicketInfo(String ticketNum) {
		progresser.showProgress();
		batTransaction.getTicketInfo(ticketNum, new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(CommonThirdPayActivity.this, response.getMsg());
				final TicketInfo ticket = (TicketInfo) response.result;
				String amount;   
				if(Calculater.compare(batTransaction.getTransactionInfo().getRealAmount(), Tools.formatFen(Long.valueOf(ticket.getTicketDef().getBalance()))) <= 0)
				{
					amount = Tools.toIntMoney(batTransaction.getTransactionInfo().getRealAmount()) + "";
				}else
				{
					amount = ticket.getTicketDef().getBalance() + "";
				}
				ticketId = ticket.getId() + "|" + (isScan?"1":"0") + "|" + amount;
				String msg = ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance()+"");
				DialogHelper2.showChoiseDialog(CommonThirdPayActivity.this, msg, new DialogChoiseListener() {

					public void onNo() {
						ticketId = null;
					}
					
					@Override
					public void onOK() {
						Response mResponse = TicketManagerUtil.verifyAddTicket(ticket, batTransaction.getUsedTicketlist());
						if(mResponse.code != 0) {
							Toast.makeText(CommonThirdPayActivity.this, mResponse.msg, Toast.LENGTH_SHORT).show();
							return;
						}
						Response addCommonTicketResponse = batTransaction.addTicket(ticket, isScan);
						if (addCommonTicketResponse.code == 0) {
							tvInitAmount.setText(Calculater.formotFen(batTransaction.getTransactionInfo().getInitAmount()));
							tvReduceAmount.setText(Calculater.formotFen(batTransaction.getTransactionInfo().getReduceAmount()));
							etRealAmount.setText(Calculater.formotFen(batTransaction.getTransactionInfo().getRealAmount()));
							if (!TextUtils.isEmpty(ticketId)) {
								ticketIds.add(ticketId);
							}
						}else{
							UIHelper.ToastMessage(CommonThirdPayActivity.this, addCommonTicketResponse.msg);
						}
						ticketId = null;
					}
				});
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(CommonThirdPayActivity.this, response.getMsg());
			}
		});

	}

	private void toTrans() {
		String realAmount = etRealAmount.getText().toString();
		realAmount = Calculater.formotYuan(realAmount);
		progresser.showProgress();
		batTransaction.getTransactionInfo().setRealAmount(realAmount);
		batTransaction.getTransactionInfo().setReduceAmount(Calculater.subtract(batTransaction.getTransactionInfo().getInitAmount(), realAmount));
		Intent intent = getIntent();
		intent.putExtra(Constants.realAmount,realAmount);
		intent.putExtra(Constants.initAmount,batTransaction.getTransactionInfo().getInitAmount());
		intent.putExtra(Constants.reduceAmount,batTransaction.getTransactionInfo().getReduceAmount());
		intent.putExtra("ticketTotalAmount", batTransaction.getTransactionInfo().getTicketTotalAomount());
		intent.putExtra("usedTicketlist",(Serializable)batTransaction.getUsedTicketlist());
		intent.putExtra("weiMobTicket", batTransaction.getTransactionInfo().getBatTicket());
		intent.putStringArrayListExtra("ticketIds",(ArrayList<String>) ticketIds);
		startActivity(intent);
		finish();
	}
	
	public void useTicket(){
		toInputView(this, "券使用", false, true, true, new Intent(), TICKET_INFO); //用券界面修改 wu
	}
	
	@Override
	protected void onDestroy() {
		batTransaction.onDestory();
		super.onDestroy();
	}

	@Override
	public void onSuccess(Response response) {
		progresser.showContent();
		UIHelper.ToastMessage(this, response.getMsg());
	}

	@Override
	public void onFaild(Response response) {
		progresser.showContent();
		UIHelper.ToastMessage(this, response.getMsg());
	}
}
