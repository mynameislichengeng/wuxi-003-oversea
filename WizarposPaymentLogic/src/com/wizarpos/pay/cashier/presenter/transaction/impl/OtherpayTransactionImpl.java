package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.inf.OtherPayTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.LastPrintHelper;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class OtherpayTransactionImpl extends TransactionImpl implements OtherPayTransaction {
	private ResultListener resultListener;
	private String serviceName = "";
	private String serviceId = "";
	private String mark;
	public void setInitListener(ResultListener listener) {
		this.resultListener = listener;
		cashierTypeQuery();
	}

	public OtherpayTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void handleIntent(Intent intent) {
		super.handleIntent(intent);
		defTransactionType(TransactionTemsController.TRANSACTION_TYPE_OTHER);
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@Override
	public void setRound(String round) {
		this.round = round;
	}
	
	@Override
	public void trans(final ResultListener listener) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("payAmount", realAmount);
		if(!TextUtils.isEmpty(saleInputAmount)&&!saleInputAmount.equals("0")){ 
			params.put("inputAmount", saleInputAmount);		//当有销售金额传入时,使用 saleInputAmout wu@[20151120]	
		}else{
			params.put("inputAmount", initAmount);			
		}
		params.put("serviceId", serviceId);
		params.put("isPayComingForm", isPayComingForm);
		params.put("round", round);
		params.put("rechargeOn", rechargeOn);
		if(isMixTransaction()){ //增加传入混合支付的总金额 wu
			params.put("masterPayAmount", mixInitAmount);//组合支付总金额
			params.put("masterTranLogId", mixTranLogId);
			params.put("mixFlag", 1);
		}
		NetRequest.getInstance().addRequest(Constants.SC_401_OTHER_PAY, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject jTranLog = robj.getJSONObject("masterTranLog");
				tranLogId = jTranLog.getString("id");
				print();
				response.code = 0;
				response.msg = "success";
				response.result = bundleResult();
				listener.onSuccess(response);
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	
	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

	public void cashierTypeQuery() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("merchantId", AppConfigHelper.getConfig(AppConfigDef.merchantId));
		params.put("payId", AppConfigHelper.getConfig(AppConfigDef.app_id));
		params.put("mid", AppConfigHelper.getConfig(AppConfigDef.mid));
		NetRequest.getInstance().addRequest(Constants.SC_101_SERVICE_TYPE_QUERY, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				resultListener.onSuccess(response);
			}

			@Override
			public void onFaild(Response response) {
				resultListener.onFaild(response);
			}
		});
	}

	private void print() {
		if (isNeedPrint == false) { return; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		printString += builder.center(builder.bold("其它支付" + "-" + serviceName));
		printString += builder.branch();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
		printString += "流水号：" + tranLogId + builder.br();
		if (!TextUtils.isEmpty(round) && !"0".equals(round)) {
			printString += "收银：" + Calculater.divide100(initAmount) + "元" + builder.br();
			printString += "实收：" + Calculater.divide100(realAmount) + "元" + builder.br();
			printString += "四舍五入：" + Calculater.divide100(round) + "元" + builder.br();
		} else {//若无四舍五入，则只显示订单金额。 @yaosong [201512125]
			printString += "订单金额：" + Calculater.divide100(initAmount) + "元" + builder.br();
		}
		printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + builder.br();
		if (!TextUtils.isEmpty(mark)) {
			printString += "备注：" + mark + builder.br();
		}
		printString += builder.branch();
		printString += builder.endPrint();
		controller.print(printString);
		controller.cutPaper();
		LastPrintHelper.beginTransaction().addString(printString).commit();
	}

}
