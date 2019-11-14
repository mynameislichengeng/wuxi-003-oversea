package com.wizarpos.pay.common.device;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * 客显
 * 
 * @author wu
 */
public class DefaultDisplayerImpl implements Displayer {
	private Context context;

	public DefaultDisplayerImpl(Context context) {
		this.context = context;
	}

	@Override
	public void display(Bitmap bitmap) {
	}

	public void display(HashMap<String, String> dismap) {
		Intent intent = new Intent();
		// 设置Action
		intent.setAction("com.wizarpos.pay.PayReceiver");
		// 我方程序用html模板已经固定在收银伴侣中，直接传参修改
		intent.putExtra("alltext", dismap);
		// 我方程序识别类型
		intent.putExtra("gotype", "1");
		// 发送广播
		context.sendBroadcast(intent);
	}

}
