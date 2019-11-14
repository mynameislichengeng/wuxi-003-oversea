package com.wizarpos.pay.cashier.activity;

import android.content.Intent;

import com.wizarpos.pay.cashier.presenter.MixTransactionFinishReceiver;
import com.wizarpos.pay2.lite.R;

public abstract class BATActivity extends TransactionActivity {

	/**
	 * 分发交易结果
	 * 
	 * @param isMixTransaction
	 * @param resultIntent
	 */
	protected void deliverResult(boolean isMixTransaction, Intent resultIntent) {
		if (isMixTransaction) {
			resultIntent.setAction(MixTransactionFinishReceiver.ACTION);
			BATActivity.this.sendBroadcast(resultIntent);
			BATActivity.this.finish();
		} else {
			toTransactionSuccess(BATActivity.this, resultIntent);
			BATActivity.this.finish();
		}
	}

	protected void setTitleTextIsMix(boolean isMixTransaction, String titleText) {
		setTitleText(isMixTransaction ? getResources().getString(R.string.mix_pay) + titleText : titleText);
	}

}
