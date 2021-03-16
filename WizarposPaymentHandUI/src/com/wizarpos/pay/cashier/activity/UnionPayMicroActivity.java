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
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.model.OrderDef;
import com.motionpay.pay2.lite.R;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-4 下午3:45:24
 * @Description:移动支付支付(copy from {@link QwalletMicroActivity})
 */
public class UnionPayMicroActivity extends ThirdpayScanActivity {
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
			intent.putExtra("payTypeFlag", Constants.UNION_BAT);
			batTransaction=TransactionFactory.newBatMicroTransaction(this);
			batTransaction.handleIntent(intent);
		}else{
			
		}
		initView();
	}

	private void initView() {
		setTitleText("移动支付");
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
						UIHelper.ToastMessage(UnionPayMicroActivity.this, response.msg);
					}
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(UnionPayMicroActivity.this, response.msg);
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
						UIHelper.ToastMessage(UnionPayMicroActivity.this, response.msg);
					}
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					UIHelper.ToastMessage(UnionPayMicroActivity.this, response.msg);
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
		if (Constants.BAT_FLAG) {
			progresser.showProgress();
			batTransaction.batUnionPay(Constants.TENPAY_BAT, authCode, new ResultListener() {
				
				@Override
				public void onSuccess(Response response) {
					UIHelper.ToastMessage(UnionPayMicroActivity.this, "成功消费");
					deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
				}
				
				@Override
				public void onFaild(Response response) {
					progresser.showContent();
					if (response.code == 1) {
						showRemindInfo(response.msg);
					} else {
						UIHelper.ToastMessage(UnionPayMicroActivity.this, getResources().getString(R.string.consumption_failure) + response.msg);
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
