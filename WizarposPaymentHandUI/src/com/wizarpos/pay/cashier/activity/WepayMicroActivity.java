package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.MicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.inf.WepayMicroTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.motionpay.pay2.lite.R;

public class WepayMicroActivity extends ThirdpayScanActivity {
	private WepayMicroTransaction transaction;
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

		String discount = intent.getStringExtra("discount");
		if (!TextUtils.isEmpty(discount) && Constants.DISCOUNTABLE){
			intent.putExtra(Constants.initAmount, Calculater.plus(getIntent().getStringExtra(Constants.initAmount),discount));
		}

		if (Constants.BAT_FLAG) {
			batTransaction = TransactionFactory.newBatMicroTransaction(this);
			intent.putExtra("payTypeFlag", Constants.WEPAY__BAT);
			batTransaction.handleIntent(intent);
		} else {
			transaction = TransactionFactory.newWepayMicroTransaction(this);// 改为从工厂初始化@wu 20150720
			transaction.handleIntent(intent);
		}
		initView();
	}

	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.wepay));
	}

	public void checkOrder() {
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
						UIHelper.ToastMessage(WepayMicroActivity.this, response.getMsg());
						// showPayFaildDialog(response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(WepayMicroActivity.this, response.msg);
				}
			});

		} else {
			transaction.checkOrder(transaction.getTransactionInfo().getTranId(), new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					OrderDef def = (OrderDef) response.result;
					if (OrderDef.STATE_PAYED == def.getState()) {
						deliverResult(transaction.isMixTransaction(), transaction.bundleResult());
					} else {
						UIHelper.ToastMessage(WepayMicroActivity.this, response.getMsg());
						// showPayFaildDialog(response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(WepayMicroActivity.this, response.msg);
				}
			});

		}

	}

	@Override
	public void display(Response response) {
		if (response.getResult() == null) { return; }
		String authCode = response.getResult().toString();
		pay(authCode);

	}

	private void pay(String authCode) {
		progresser.showProgress();
		// bat替换
		if (Constants.BAT_FLAG) {
			batTransaction.batPay(Constants.WEPAY__BAT, authCode, new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					if (response.code == 1) {
						showRemindInfo(response.msg);
					} else {
						UIHelper.ToastMessage(WepayMicroActivity.this, response.msg);
					}

				}
			});

		} else {
			transaction.pay(authCode, new BasePresenter.ResultListener() {
				@Override
				public void onSuccess(Response response) {
					deliverResult(transaction.isMixTransaction(), transaction.bundleResult());
				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					if (response.code == 1) {
						showRemindInfo(response.msg);
					} else {
						UIHelper.ToastMessage(WepayMicroActivity.this, response.msg);
					}
				}
			});

		}

	}

	// private void showPayFaildDialog(String msg) {
	// DialogHelper.showDialog(WepayMicroActivity.this, msg, new DialogCallback() {
	//
	// @Override
	// public void callback() {
	// WepayMicroActivity.this.finish();
	// }
	// });
	// }

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

	@Override
	protected void toMicroPay() {
		toWepayNativeTransaction(this, getIntent());
		this.finish();		
	}

}
