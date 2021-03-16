package com.wizarpos.pay.cashier.activity;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.MicroTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.inf.BaiduPayMicroTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.motionpay.pay2.lite.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 百度被扫支付
 * 
 * @author wu
 */
public class BaiduMicroPayActivity extends ThirdpayScanActivity {

	private BaiduPayMicroTransaction transaction;
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
		// bat替换@hong
		if (Constants.BAT_FLAG) {
			intent.putExtra("payTypeFlag", Constants.BAIDUPAY_BAT);
			batTransaction = TransactionFactory.newBatMicroTransaction(this);
			batTransaction.handleIntent(intent);
		}else {
			transaction = TransactionFactory.newBaiduMicroPayTransaction(this);
			transaction.handleIntent(intent);
		}
		initView();
	}

	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.baidu_pay));
	}

	@Override
	public void display(Response response) {
		if (response.getResult() == null) { return; }
		LogEx.e("TAG", "display--------");
		String authCode = response.getResult().toString();
		pay(authCode);
	}

	private void pay(String authCode) {
		progresser.showProgress();
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			batTransaction.batPay(Constants.BAIDUPAY_BAT, authCode, new ResultListener() {

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
						UIHelper.ToastMessage(BaiduMicroPayActivity.this, response.msg);
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
						UIHelper.ToastMessage(BaiduMicroPayActivity.this, response.msg);
					}
				}
			});

		}
	}

	// private void showPayFaildDialog(String msg) {
	// DialogHelper.showDialog(BaiduMicroPayActivity.this, msg, new DialogCallback() {
	//
	// @Override
	// public void callback() {
	// BaiduMicroPayActivity.this.finish();
	// }
	// });
	// }

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
						UIHelper.ToastMessage(BaiduMicroPayActivity.this, response.getMsg());
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(BaiduMicroPayActivity.this, response.msg);
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
						UIHelper.ToastMessage(BaiduMicroPayActivity.this, response.getMsg());
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(BaiduMicroPayActivity.this, response.msg);
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Constants.BAT_FLAG) {
			if (batTransaction != null) {
				batTransaction.onDestory();
			}
		}else {
			if (transaction != null) {
				transaction.onDestory();
			}
		}
	}

	@Override
	protected void toMicroPay() {
		toBaiduNativeTransaction(this, getIntent());
		this.finish();
	}

}
