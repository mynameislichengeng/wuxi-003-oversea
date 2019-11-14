package com.wizarpos.pay.cashier.activity;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.NativeTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.inf.BaiduPayNativeTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.DialogHelper;
import com.wizarpos.pay.common.DialogHelper.DialogCallback;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.wizarpos.pay2.lite.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 百度扫码支付
 * 
 * @author wu
 * 
 */
public class BaiduNativePayActivity extends BATNativeActivity {
	private BaiduPayNativeTransaction transaction;
	private NativeTransaction batTransaction;

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
			intent.putExtra("payTypeFlag", Constants.BAIDUPAY_BAT);
			batTransaction = TransactionFactory.newBatNativeTransaction(this);
			batTransaction.handleIntent(intent);
		} else {
			transaction = TransactionFactory.newBaiduNativePayTransaction(this);
			transaction.handleIntent(intent);
		}
		initView();
		initData();
	}

	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.baidu_pay));
	}

	private void initData() {
		progresser.showProgress();
		if (Constants.BAT_FLAG) {
			batTransaction.batPay(Constants.BAIDUPAY_BAT, new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					String url = response.getResult().toString();
					showImgFromNet(url);
					batTransaction.listenResult(new ResultListener() {

						@Override
						public void onSuccess(Response response) {
							deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
						}

						@Override
						public void onFaild(Response response) {
							progresser.showContent();
							UIHelper.ToastMessage(BaiduNativePayActivity.this, response.getMsg());
							// showPayFaildDialog(response.msg);
						}
					});

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(BaiduNativePayActivity.this, response.getMsg());
				}
			});

		} else {
			transaction.getBarCode(new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					String url = response.getResult().toString();
					showImgFromNet(url);
					transaction.listenResult(new ResultListener() {

						@Override
						public void onSuccess(Response response) {
							deliverResult(transaction.isMixTransaction(), transaction.bundleResult());
						}

						@Override
						public void onFaild(Response response) {
							// UIHelper.ToastMessage(BaiduNativePayActivity.this, response.getMsg());
							progresser.showContent();
							showPayFaildDialog(response.msg);
						}
					});
				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(BaiduNativePayActivity.this, response.getMsg());
				}
			});

		}
	}

	private void showPayFaildDialog(String msg) {
		DialogHelper.showDialog(BaiduNativePayActivity.this, msg, new DialogCallback() {

			@Override
			public void callback() {
				BaiduNativePayActivity.this.finish();
			}
		});
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
						UIHelper.ToastMessage(BaiduNativePayActivity.this, response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(BaiduNativePayActivity.this, response.msg);
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
						UIHelper.ToastMessage(BaiduNativePayActivity.this, response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(BaiduNativePayActivity.this, response.msg);
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
		} else {
			if (transaction != null) {
				transaction.onDestory();
			}
		}
	}

	@Override
	protected void toMicropay() {
		toBaiduMicroTransaction(this, getIntent());
		this.finish();
	}

}
