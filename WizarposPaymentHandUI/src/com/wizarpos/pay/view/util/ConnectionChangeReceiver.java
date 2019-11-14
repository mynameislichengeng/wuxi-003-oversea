package com.wizarpos.pay.view.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static final String TAG = ConnectionChangeReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// !mobNetInfo.isConnected() && !wifiNetInfo.isConnected()
		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			AppStateManager.setState(AppStateDef.isOffline, Constants.TRUE);
			Log.i(TAG, "unconnect");
			intent.setAction(Constents.PAYACTION);
			intent.putExtra("value", "0");
			LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		} else {
			Log.i(TAG, "connect");
			AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
			intent.setAction(Constents.PAYACTION);
			intent.putExtra("value", "1");
			LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		}
	}

}
