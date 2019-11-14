package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.alipay.inf.AlipayMicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.MicroTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.ScanFragment;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.wizarpos.pay2.lite.R;

public class AlipayMicroActivity extends ThirdpayScanActivity {
	private AlipayMicroTransaction transaction;// 直连
	private MicroTransaction batTransaction;// bat@hong
	private String initAmount = "";
	private ScanFragment scanFragment;
	private TextView remindInfoTv;
	
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
			batTransaction = TransactionFactory.newBatMicroTransaction(this);
			intent.putExtra("payTypeFlag", Constants.ALIPAY_BAT);
			batTransaction.handleIntent(intent);
		}else{
			transaction = TransactionFactory.newAlipayMicroTransaction(this);// 改为从工厂初始化@wu 20150720
			transaction.handleIntent(intent);
		}
		initView();
		
		// Intent intent = new Intent();
		// intent.putExtra("initAmount", initAmount);
		// intent.putExtra("body", "支付宝支付test");
	}
	private void initView() {
		boolean isMix = Constants.BAT_FLAG ? batTransaction.isMixTransaction() : transaction.isMixTransaction();
		setTitleTextIsMix(isMix, getResources().getString(R.string.alipay_title));
		// initAmount = getIntent().getStringExtra("initAmount");
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
						deliverResult(batTransaction.isMixTransaction(), resultIntent);// 增加组合支付逻辑处理 wu@[20150729]
					} else {
						// showPayFaildDialog(response.msg);
						UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
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
						deliverResult(transaction.isMixTransaction(), resultIntent);// 增加组合支付逻辑处理 wu@[20150729]
					} else {
						// showPayFaildDialog(response.msg);
						UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
					}

				}

				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
				}
			});

		}

	}

	@Override
	public void display(Response response) {
		if (response.getResult() == null) { return; }
		String authCode = response.getResult().toString();
		progresser.showProgress();
		pay(authCode);
	}

	private void pay(String authCode) {
		if (Constants.BAT_FLAG) {
			// bat替换@hong
			batTransaction.batPay(Constants.ALIPAY_BAT, authCode, new ResultListener() {
				
				@Override
				public void onSuccess(Response response) {
					progresser.showContent();
					Intent responseIntent = batTransaction.bundleResult();
					deliverResult(batTransaction.isMixTransaction(), responseIntent);
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					if (response.code == 1) {
						showRemindInfo(response.msg);
					} else {
						UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
					}
				}
			});
		}else {
			 transaction.pay(authCode, new BasePresenter.ResultListener() {
			 @Override
			 public void onSuccess(Response response) {
			 Intent responseIntent = transaction.bundleResult();
			 deliverResult(transaction.isMixTransaction(),responseIntent);
			 }
			
			 @Override
			 public void onFaild(Response response) {
			 progresser.showContent();
			 UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
			 if (response.code == 1) {
						showRemindInfo(response.msg);
				} else {
					UIHelper.ToastMessage(AlipayMicroActivity.this, response.msg);
				}
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
	protected void toMicroPay() {
		toAlipayNativeTransaction(this, getIntent());
		this.finish();
	}

}
