package com.wizarpos.pay.cashier.activity;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.alipay.inf.AlipayNativeTransaction;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.NativeTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.DialogHelper;
import com.wizarpos.pay.common.DialogHelper.DialogCallback;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.motionpay.pay2.lite.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class AlipayNativeActivity extends BATNativeActivity {
	private AlipayNativeTransaction transaction;
	private NativeTransaction batTransaction;
	private String ivUrl;
	String initAmount = "";

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
			batTransaction = TransactionFactory.newBatNativeTransaction(this);// bat@hong
			intent.putExtra("payTypeFlag", Constants.ALIPAY_BAT);
			batTransaction.handleIntent(intent);
		} else {
			transaction = TransactionFactory.newAlipayNativeTransaction(this);// 改为从工厂初始化@wu 20150720
			transaction.handleIntent(intent);
		}
		initView();
		initData();
	}

	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.alipay_title));
		// initAmount = getIntent().getStringExtra("initAmount");
		// Intent intent = new Intent();
		// intent.putExtra("initAmount", initAmount);// 初始金额
		// intent.putExtra("subject", "支付宝test");// 在付款人支付宝客户端显示的交易信息
	}

	@Override
	protected void toMicropay() {
		toAlipayMicroTransaction(this, getIntent());
		this.finish();
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
						Intent resultIntent = batTransaction.bundleResult();
						deliverResult(batTransaction.isMixTransaction(), resultIntent);
					} else {
						UIHelper.ToastMessage(AlipayNativeActivity.this, response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(AlipayNativeActivity.this, response.msg);
				}
			});

		} else {
			transaction.checkOrder(transaction.getTransactionInfo().getTranId(), new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					OrderDef def = (OrderDef) response.result;
					if (OrderDef.STATE_PAYED == def.getState()) {
						Intent resultIntent = transaction.bundleResult();
						deliverResult(transaction.isMixTransaction(), resultIntent);
					} else {
						UIHelper.ToastMessage(AlipayNativeActivity.this, response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(AlipayNativeActivity.this, response.msg);
				}
			});
		}
	}

	private void initData() {
		progresser.showProgress();
		// bat替换@hong
		if (Constants.BAT_FLAG) {
			batTransaction.batPay(Constants.ALIPAY_BAT, new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					ivUrl = response.getResult().toString();
					showImgFromString(ivUrl);
					batTransaction.listenResult(new ResultListener() {

						@Override
						public void onSuccess(Response response) {
							Intent resultIntent = batTransaction.bundleResult();
							deliverResult(batTransaction.isMixTransaction(), resultIntent);
						}

						@Override
						public void onFaild(Response response) {
							UIHelper.ToastMessage(AlipayNativeActivity.this, response.getMsg());
							// showPayFaildDialog(response.msg);
						}
					});

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(AlipayNativeActivity.this, response.msg);
				}
			});
		} else {
			transaction.getBarCode(new ResultListener() {

				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					String url = response.getResult().toString();
					showImgFromString(url);
					transaction.listenResult(new ResultListener() {

						@Override
						public void onSuccess(Response response) {
							Intent resultIntent = transaction.bundleResult();
							deliverResult(transaction.isMixTransaction(), resultIntent);
						}

						@Override
						public void onFaild(Response response) {
							 UIHelper.ToastMessage(AlipayNativeActivity.this, response.getMsg());
//							showPayFaildDialog(response.msg);
						}
					});
				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(AlipayNativeActivity.this, response.msg);
				}
			});

		}

	}

	private void showPayFaildDialog(String msg) {
		DialogHelper.showDialog(AlipayNativeActivity.this, msg, new DialogCallback() {

			@Override
			public void callback() {
				AlipayNativeActivity.this.finish();
			}
		});
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

}
