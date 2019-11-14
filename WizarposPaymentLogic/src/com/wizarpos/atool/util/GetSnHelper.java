package com.wizarpos.atool.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class GetSnHelper {
	public GetSnHelper (Context context) {
	}
	public static String getMacAndSn (Context context) {
		String sn = "";
		try{
			if (Constants.WIZARPOS_FLAG) {
				sn = android.os.Build.SERIAL;
			} else if (Constants.DZXPAD_FLAG){
				WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = wifi.getConnectionInfo();
				sn = info.getMacAddress().replace(":", "");
			}
		}catch(Exception ex){}
		return sn;
	}
}
