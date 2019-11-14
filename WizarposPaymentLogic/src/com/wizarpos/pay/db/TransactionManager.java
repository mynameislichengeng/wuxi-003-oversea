package com.wizarpos.pay.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.utils.Logger2;

/**
 * 交易管理类
 * 
 * @author wu
 */
public class TransactionManager {

	private DbUtils controller;

	private static TransactionManager manager;

	private TransactionManager() {
		controller = PaymentApplication.getInstance().getDbController();
	}

	public static TransactionManager getInstance() {
		if (manager == null) {
			manager = new TransactionManager();
		}
		return manager;
	}

	/**
	 * 添加交易
	 */
	public void addTransaction(CashPayRepair cashPayRepair) {
		try {
			Logger2.debug("添加一笔离线交易 id:" + cashPayRepair.getId());
			controller.save(cashPayRepair);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移除交易
	 */
	public void removeTransaction(String tranId) {
		try {
			Logger2.debug("删除离线交易 id:" + tranId);
			controller.delete(CashPayRepair.class,
					WhereBuilder.b("id", "=", tranId));
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上送交易
	 */
	public void uploadTransaction(ResultListener listener) {
		try {
			List<CashPayRepair> offlineTrans = controller
					.findAll(CashPayRepair.class);
			if (offlineTrans == null || offlineTrans.isEmpty()) {
				if (listener != null)
					listener.onSuccess(new Response(0, "上送成功"));
				return;
			}
			// XXX 需要调整接口,一次上送完所有离线交易数据
			UploadTransTask uploadTransTask = new UploadTransTask(offlineTrans,
					listener);
			uploadTransTask.execute("");
		} catch (DbException e) {
			e.printStackTrace();
			listener.onFaild(null);
		}
	}

	/**
	 * 获取离线数据
	 * 
	 * @return
	 */
	public List<CashPayRepair> getOfflineTransacton() {
		try {
			return controller.findAll(CashPayRepair.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	// /**
	// * 更新交易
	// */
	// public void updataTransaction() {
	//
	// }

	/**
	 * 上送离线交易的内部类
	 * 
	 * @author wu
	 */
	class UploadTransTask extends AsyncTask<String, String, String> {

		private List<CashPayRepair> offlineTrans;
		private int position = 0;
		private boolean isUploadFinish;
		private ResultListener listener;

		public UploadTransTask(List<CashPayRepair> offlineTrans,
				ResultListener listener) {
			this.offlineTrans = offlineTrans;
			this.listener = listener;
			position = -1;
			isUploadFinish = false;
		}

		@Override
		protected String doInBackground(String... params) {
			if (offlineTrans == null || offlineTrans.isEmpty()) {
				return "";
			}
			continueUpload();
			while (!isUploadFinish) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// uploadSignleTrans(offlineTrans.get(0));
			return "";
		}

		private void uploadSignleTrans(final CashPayRepair offlineTran) {
			Logger2.debug("开始上送离线交易 id:" + offlineTran.getId());
			JSONObject msg = JSON.parseObject(offlineTran.getMsg());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("offlineTranLogId", msg.getString("offlineTranLogId"));
			params.put("tranTime", msg.getString("tranTime"));
			params.put("tranMark", "离线现金交易");
			params.put("merchantId", msg.get("merchantId"));
			params.put("mid", msg.get("mid"));

			params.put("cardType", msg.get("cardType"));

			params.put("payAmount", msg.get("payAmount"));
			String extraAmount = msg.getString("extraAmount");
			params.put("extraAmount", TextUtils.isEmpty(extraAmount) ? "0"
					: extraAmount);
			String inputAmount = msg.getString("inputAmount");
			params.put("inputAmount", TextUtils.isEmpty(inputAmount) ? "0"
					: inputAmount);
			// m.put("disCount", disCountNeed);
			params.put("cardNo", msg.get("cardNo"));
			params.put("issueTicketMode", msg.get("issueTicketMode"));
			String amount = msg.getString("amount");
			params.put("amount", TextUtils.isEmpty(amount) ? "0" : amount);
			params.put("rechargeOn", msg.get("rechargeOn"));
			params.put("cardName", "现金");
			NetRequest.getInstance().addRequest(
					Constants.SC_402_CASH_OFFLINE_PAY, params,
					new ResponseListener() {

						@Override
						public void onSuccess(Response response) {
							removeTransaction(offlineTran.getId());
							continueUpload();
						}

						@Override
						public void onFaild(Response response) {
							continueUpload();
						}

					});

		}

		private void continueUpload() {
			position++;
			if (position < offlineTrans.size()) {
				uploadSignleTrans(offlineTrans.get(position));
			} else {
				isUploadFinish = true;
			}
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (listener != null)
				listener.onSuccess(new Response(0, "上送成功"));
		}
	}

}
