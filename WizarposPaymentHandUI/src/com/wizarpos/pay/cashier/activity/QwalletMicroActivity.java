package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.MicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.tenpay.inf.TenpayMicroTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.wizarpos.pay2.lite.R;

public class QwalletMicroActivity extends ThirdpayScanActivity {
	private TenpayMicroTransaction transaction;
//	private TenpayMicroTransaction transaction;// 调用新接口替换 2015-7-20 09:23:16
	private MicroTransaction batTransaction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		String realAmount = intent.getStringExtra(Constants.realAmount); //增加realAmount验证,如果没有realAmount,则将initAmount设为realAmount wu
		if(TextUtils.isEmpty(realAmount)){
			realAmount = getIntent().getStringExtra(Constants.initAmount);
			intent.putExtra(Constants.realAmount, realAmount);
		}
		if (Constants.BAT_FLAG) {
			intent.putExtra("payTypeFlag", Constants.TENPAY_BAT);
			batTransaction=TransactionFactory.newBatMicroTransaction(this);
			batTransaction.handleIntent(intent);
		}else {
			transaction = TransactionFactory.newTenpayMicroTransaction(this);// 初始化 //改为从工厂初始化@wu 20150720
			transaction.handleIntent(intent);
		}
		initView();
	}

	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.q_wallet_title));
		if(DeviceManager.getInstance().isWizarDevice() == false){ //隐藏扫一扫按钮
			findViewById(R.id.rlMicroPay).setVisibility(View.GONE);
			findViewById(R.id.llBtm).requestLayout();
		}else{
			findViewById(R.id.btnMicroPay).setVisibility(View.GONE);
		}
	}

	@Override
	protected void toMicroPay() {
//		toTenpayNativeTransaction(this, getIntent());
//		this.finish();		
	}
	
	protected void checkOrder() {
		progresser.showProgress();
		if (Constants.BAT_FLAG) {
			batTransaction.checkOrder(batTransaction.getTransactionInfo().getTranId(), new ResultListener() {
				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					OrderDef def = (OrderDef) response.result;
					if (OrderDef.STATE_PAYED == def.getState()) {
						deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
					} else {
						UIHelper.ToastMessage(QwalletMicroActivity.this, response.msg);
					}
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(QwalletMicroActivity.this, response.msg);
//				showPayFaildDialog(response.msg);
				}
			});
			
		}else {
			transaction.checkOrder(transaction.getTransactionInfo().getTranId(), new ResultListener() {
				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					OrderDef def = (OrderDef) response.result;
					if (OrderDef.STATE_PAYED == def.getState()) {
						deliverResult(transaction.isMixTransaction(), transaction.bundleResult());
					} else {
						UIHelper.ToastMessage(QwalletMicroActivity.this, response.msg);
					}
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(QwalletMicroActivity.this, response.msg);
//				showPayFaildDialog(response.msg);
				}
			});
		}

	}

	@Override
	public void display(Response response) {
		String authCode = response.getResult().toString();
		trans(authCode);
	}

	/**
	 * 获取授权码后，进行交易
	 * 
	 * @param authCode
	 */
	private void trans(String authCode) {
		progresser.showProgress();
		if (Constants.BAT_FLAG) {
			batTransaction.batPay(Constants.TENPAY_BAT, authCode, new ResultListener() {
				
				@Override
				public void onSuccess(Response response) {
					UIHelper.ToastMessage(QwalletMicroActivity.this, "成功消费");
					deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					if (response.code == 1) {
						showRemindInfo(response.msg);
					} else {
						UIHelper.ToastMessage(QwalletMicroActivity.this, "消费失败" + response.msg);
					}
				}
			});
			
		}else {
		transaction.pay(authCode, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				UIHelper.ToastMessage(QwalletMicroActivity.this, "成功消费");
				deliverResult(transaction.isMixTransaction(),transaction.bundleResult());
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				if (response.code == 1) {
					showRemindInfo(response.msg);
				} else {
					UIHelper.ToastMessage(QwalletMicroActivity.this, "消费失败" + response.msg);
				}
			}
		});
			
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (batTransaction!=null) {
			batTransaction.onDestory();
		}
		if (transaction!=null) {
			transaction.onDestory();
		}
	}

}
