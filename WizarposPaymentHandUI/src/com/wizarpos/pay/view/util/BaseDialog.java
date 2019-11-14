package com.wizarpos.pay.view.util;

import android.app.AlertDialog;
import android.content.Context;

public class BaseDialog extends AlertDialog {
	// 上下文
	public Context context = null;

	/**
	 * 说明：创建
	 * 
	 * @param context
	 *            上下文
	 */
	public BaseDialog(Context context) {
		super(context);
		this.context = context;
	}

}
