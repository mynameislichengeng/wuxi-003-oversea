package com.wizarpos.pay.common.net;

import android.os.AsyncTask;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetLineHelper extends AsyncTask<Void, Void, Boolean> {

	private ResultListener listener;

	public void setListener(ResultListener listener) {
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		String serviceUrl = "http://"
				+ AppConfigHelper.getConfig(AppConfigDef.ip)
				+ ":"
				+ AppConfigHelper.getConfig(AppConfigDef.port) + Constants.SUFFIX_URL;
		// String serviceUrl = "http://10.0.0.59:8090"+ Constants.SUFFIX_URL;
		boolean isConn = false;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(serviceUrl);
			Logger2.debug("服务器地址:" + serviceUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			if (conn.getResponseCode() == 200) {
				isConn = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return isConn;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			listener.onSuccess(new Response(0, "success"));
		} else {
			if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
			{
				listener.onFaild(new Response(1, "无法连接到服务器"));
			}else
			{
				listener.onFaild(new Response(1, "网络不通"));
			}
		}
	}

}
