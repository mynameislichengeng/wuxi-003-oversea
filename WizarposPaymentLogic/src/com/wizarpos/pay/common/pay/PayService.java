package com.wizarpos.pay.common.pay;

import android.content.ServiceConnection;

public abstract class PayService implements ServiceConnection {

	protected ServiceHandle handle = null;
	// protected Response response = null;
	protected String action = null;
	protected String arg = null;

	public PayService(String action, String arg, ServiceHandle handle) {
		this.action = action;
		this.arg = arg;
		this.handle = handle;
	}

	// public void setHandle(ServiceHandle handle) {
	// this.handle = handle;
	// }

	// public PayService(String action, String arg) {
	// this.action = action;
	// this.arg = arg;
	// }

	// public synchronized Response getResponse() {
	// return response;
	// }

	public interface ServiceHandle {

		/**
		 * @param code
		 * @param msg
		 * @param result
		 */
		void onSuccess(int code, String msg, Object result);

		/**
		 * @param code
		 * @param msg
		 * @param result
		 */
		void onFaild(int code, String msg, Object result);
	}

}
