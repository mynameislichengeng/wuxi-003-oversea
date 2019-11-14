package com.wizarpos.pay.common;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

/**
 * 打印最后一笔帮助类
 * 
 * @author wu
 * 
 */
public class LastPrintHelper implements Serializable {

	private List<LastPrintBean> list;

	private static LastPrintHelper helper;

	private LastPrintHelper() {
		list = new ArrayList<LastPrintBean>();
	}

	public static LastPrintHelper beginTransaction() {
		helper = new LastPrintHelper();
		return helper;
	}

	public LastPrintHelper addString(String str) {
		if (helper == null) { return null;
//			throw new Exception("must call beginTransaction first!"); 
		}
		LastPrintBean bean = new LastPrintBean();
		bean.setType(LastPrintBean.TYPE_STRING);
		bean.setContent(str);
		helper.add(bean);
		return helper;
	}

	public LastPrintHelper addBitmap(Bitmap bitmap) {
		if (helper == null) { return null;
//			throw new Exception("must call beginTransaction first!");
		}
		String content = System.currentTimeMillis() + "";
		String _path = Tools.getSDPath() + File.separator + "common" + File.separator + "print_temp" + File.separator;
		File file = new File(_path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = _path + content + ".jpg";
		Tools.writePng(bitmap, new File(filePath));

		LastPrintBean bean = new LastPrintBean();
		bean.setType(LastPrintBean.TYPE_BITMAP);
		bean.setContent(filePath);
		helper.add(bean);
		return helper;
	}

	private void add(LastPrintBean bean) {
		list.add(bean);
	}

	public void commit() {
		if (helper == null) { return;
//		throw new Exception("must call beginTransaction first!");
		}
		LogEx.d("lastprint", JSONArray.toJSONString(helper.getList()));
		AppStateManager.setState(AppStateDef.LAST_PRINT, JSONArray.toJSONString(helper.getList()));
	}

	public static void printLast(Context context) {
		String lastPrintInfo = AppStateManager.getState(AppStateDef.LAST_PRINT);
		if (TextUtils.isEmpty(lastPrintInfo)) { return; }
		try {
			List<LastPrintBean> list = JSONArray.parseArray(lastPrintInfo, LastPrintBean.class);
			if (list != null) {
				PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
				for (LastPrintBean printBean : list) {
					if (LastPrintBean.TYPE_STRING == printBean.getType()) {
						controller.print(printBean.getContent());
					} else if (LastPrintBean.TYPE_BITMAP == printBean.getType()) {
						controller.printBitmap(printBean.getContent());
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public List<LastPrintBean> getList() {
		return list;
	}
}
