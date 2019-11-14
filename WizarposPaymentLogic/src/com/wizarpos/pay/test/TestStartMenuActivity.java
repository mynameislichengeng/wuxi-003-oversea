package com.wizarpos.pay.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.wizarpospaymentlogic.R;

public class TestStartMenuActivity extends BaseTest implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_test);

		findViewById(R.id.tvInfo).setOnClickListener(this);
		findViewById(R.id.btnModelSetting).setOnClickListener(this);




		getInfo();
	}

	private void getInfo() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
		int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;

		int screenWidthDip = dm.widthPixels; // 屏幕宽（dip，如：320dip）
		int screenHeightDip = dm.heightPixels; // 屏幕高（dip，如：533dip）

		int screenWidth = (int) (dm.widthPixels * density + 0.5f); // 屏幕宽（px，如：480px）
		int screenHeight = (int) (dm.heightPixels * density + 0.5f); // 屏幕高（px，如：800px）

		StringBuilder sb = new StringBuilder();
		sb.append("屏幕密度(像素比例：0.75/1.0/1.5/2.0):").append(density).append("\n");
		sb.append("屏幕密度（每寸像素：120/160/240/320):").append(densityDPI)
				.append("\n");
		sb.append("xdpi:").append(xdpi).append("\n");
		sb.append("ydpi:").append(ydpi).append("\n");
		sb.append("屏幕宽(dip):").append(screenWidthDip).append("\n");
		sb.append("屏幕高(dip):").append(screenHeightDip).append("\n");
		sb.append("屏幕宽(px):").append(screenWidth).append("\n");
		sb.append("屏幕高(px):").append(screenHeight).append("\n");
		sb.append(getDeviceInfo(this));
		((TextView) findViewById(R.id.tvInfo)).setText(sb.toString());
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		if (v.getId() == R.id.btnModelSetting) {
			intent = new Intent(this, TestModeConfigActivity.class);
			startActivity(intent);
		}
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);
			String str = json.toString();
			LogEx.d("deviceinfo", str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
