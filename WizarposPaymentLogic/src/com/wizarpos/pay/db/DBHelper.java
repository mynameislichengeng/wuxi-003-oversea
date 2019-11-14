package com.wizarpos.pay.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.pay.app.PaymentApplication;

public class DBHelper {

	public static void init() throws DbException, IllegalArgumentException,
			IllegalAccessException {
		DbUtils dbController = PaymentApplication.getInstance()
				.getDbController();
	}

	public static String querySelfAuto(Context context) {
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(
					TicketCardDefColumns.CONTENT_URI, null, null, null, null);
			if (cursor == null) { return "self"; }
			String value = "";
			if (cursor.moveToNext()) {
				value = cursor.getString(0);
			}
			cursor.close();
			return value;
		} catch (Exception ex) {
			Log.e("DBHelper", "无法获取会员易配置");
		}
		return "self";
	}

}
