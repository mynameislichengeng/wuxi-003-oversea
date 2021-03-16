package com.wizarpos.pay.cashier.activity;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.NativeTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.inf.WepayNativeTransaction;
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

public class WepayNativeActivity extends BATNativeActivity {
	private WepayNativeTransaction transaction;
	private NativeTransaction batTransaction;
	String initAmount = "";
	/** 二维码URL地址*/
	private String ivUrl;

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
			intent.putExtra("payTypeFlag", Constants.WEPAY__BAT);
			batTransaction = TransactionFactory.newBatNativeTransaction(this);
			batTransaction.handleIntent(intent);
		}else {
			transaction = TransactionFactory.newWepayNativeTransaction(this);// 改为从工厂初始化@wu 20150720
			transaction.handleIntent(intent);
		}
		initView();
		initData();
	}

	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.wepay));
	}

	private void initData() {
		progresser.showProgress();
		if (Constants.BAT_FLAG) {
			batTransaction.batPay(Constants.WEPAY__BAT, new ResultListener() {
				
				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					ivUrl = response.getResult().toString();
					showImgFromString(ivUrl);
					batTransaction.listenResult(new ResultListener() {
						@Override
						public void onSuccess(Response response) {
							deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
						}
						
						@Override
						public void onFaild(Response response) {
//						showPayFaildDialog(response.msg);
							UIHelper.ToastMessage(WepayNativeActivity.this, response.getMsg());
						}
					});
					
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(WepayNativeActivity.this, response.getMsg());
					
				}
			});
			
		}else {
		transaction.getBarCode(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				String url = response.getResult().toString();
				showImgFromString(url);
				transaction.listenResult(new ResultListener() {
					@Override
					public void onSuccess(Response response) {
						deliverResult(transaction.isMixTransaction(),transaction.bundleResult());
					}

					@Override
					public void onFaild(Response response) {
//						showPayFaildDialog(response.msg);
						 UIHelper.ToastMessage(WepayNativeActivity.this, response.getMsg());
					}
				});
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(WepayNativeActivity.this, response.getMsg());
			}
		});
			
		}
	}

	private void showPayFaildDialog(String msg) {
		DialogHelper.showDialog(WepayNativeActivity.this, msg, new DialogCallback() {

			@Override
			public void callback() {
				WepayNativeActivity.this.finish();
			}
		});
	}
	
	protected void checkOrder() {
		if (Constants.BAT_FLAG) {
			batTransaction.checkOrder(batTransaction.getTransactionInfo().getTranId(), new ResultListener() {
				
				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					OrderDef def = (OrderDef) response.result;
					if (OrderDef.STATE_PAYED == def.getState()) {
						deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
					} else {
						UIHelper.ToastMessage(WepayNativeActivity.this, response.msg);
					}
					
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(WepayNativeActivity.this, response.msg);
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
						UIHelper.ToastMessage(WepayNativeActivity.this, response.msg);
					}
					
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(WepayNativeActivity.this, response.msg);
				}
			});
			
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (transaction!=null) {
			transaction.onDestory();
		}
		if (batTransaction!=null) {
			batTransaction.onDestory();
		}
	}

	@Override
	protected void toMicropay() {
		toWepayMicroTransaction(this, getIntent());
		this.finish();		
	}

}
