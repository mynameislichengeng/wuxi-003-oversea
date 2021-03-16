package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.presenter.transaction.inf.TenpayNativeTransaction;
import com.wizarpos.pay.cashier.view.WebViewController.WebViewStateListener;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.UIHelper;
import com.motionpay.pay2.lite.R;

/**
 * 手Q扫码支付 (废弃状态)
 * 
 * @author wu
 * 
 */
public class QwalletNativeActivity extends TransactionActivity implements WebViewStateListener {
	private Button btnMicroPay, btnCheckOrder;

	public static final String EXTRA_TITLE = "EXTRA_TITLE";
	public static final String EXTRA_URL = "EXTRA_URL";
	private String mOriginalUrl = "";
	private String mTitle = "手Q扫码支付";
	private TenpayNativeTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	protected void showWebView() {
//		BrowseFragment2 fragment = new BrowseFragment2();
//		Bundle bundle = new Bundle();
//		bundle.putString(EXTRA_URL, mOriginalUrl);
//		bundle.putString(EXTRA_TITLE, mTitle);
//		fragment.setArguments(bundle);
//		fragment.setListener(this);
//		getSupportFragmentManager().beginTransaction().add(R.id.webTenpayNative, fragment).commitAllowingStateLoss();
	}

	private void initData() {
		// transaction = TransactionFactory.newTenpayNativeTransaction(this);
		transaction.onCreate();
		transaction.handleIntent(getIntent());
		transaction.createCode(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				mOriginalUrl = response.result.toString();
				Logger2.debug("手Q扫码支付: 生成请求地址:" + mOriginalUrl);
				showWebView();
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(QwalletNativeActivity.this, response.getMsg());
			}
		});
	}

	protected void initView() {
		setMainView(R.layout.activity_qwallet_native);
		setTitleText(getResources().getString(R.string.q_wallet_title));
		showTitleBack();
		btnMicroPay = (Button) findViewById(R.id.btnMicroPay);
		btnCheckOrder = (Button) findViewById(R.id.btnCheckOrder);
		btnMicroPay.setOnClickListener(this);
		btnCheckOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btnMicroPay:
			toTenpayMicroTransaction(this, getIntent());
			this.finish();
			break;
		case R.id.btnCheckOrder:
			query();
			break;
		default:
			break;
		}
	}

	protected void query() {
		transaction.query(transaction.getTranLogId(), new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				if (response.code == 0) {
					toTransactionSuccess(QwalletNativeActivity.this, (Intent) response.result);
					QwalletNativeActivity.this.finish();
				} else {
					UIHelper.ToastMessage(QwalletNativeActivity.this, response.msg);
				}
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(QwalletNativeActivity.this, response.msg);
			}
		});
	}

	@Override
	public void onPageStarted() {

	}

	@Override
	public void onPageFinished() {
		transaction.trans(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				toTransactionSuccess(QwalletNativeActivity.this, (Intent) response.result);
				QwalletNativeActivity.this.finish();
			}

			@Override
			public void onFaild(Response response) {
				// UIHelper.ToastMessage(QwalletNativeActivity.this, response.msg);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		transaction.onDestory();
		transaction = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		transaction.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		transaction.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		transaction.onStop();
	}

}
