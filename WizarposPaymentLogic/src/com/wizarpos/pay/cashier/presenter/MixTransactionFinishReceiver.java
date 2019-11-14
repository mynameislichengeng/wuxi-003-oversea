package com.wizarpos.pay.cashier.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wizarpos.log.util.LogEx;

/**
 * 混合支付交易结束广播<br>
 * 适用于不能onActivityResult情况
 * @author wu
 *
 */
public class MixTransactionFinishReceiver extends BroadcastReceiver {

	public static final String ACTION = "com.wizarpos.pay.cashier.presenter.MixTransactionFinishListener";
	
	public interface MixTransactionFinishListener{
		void onMixTransacitonFinish(Intent intent);
	}
	
	private MixTransactionFinishListener listener;
	
	public void setListener(MixTransactionFinishListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LogEx.d("MixTransactionFinishReceiver", "组合支付子交易结束");
		if(listener!= null){
			listener.onMixTransacitonFinish(intent);
		}
	}
	
	

}
