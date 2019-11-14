
package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.pay_tems.LooperQueryerTransactionState;
import com.wizarpos.pay.cashier.pay_tems.LooperTask;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.LastPrintHelper;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.Des3;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.OrderDef;

public class BaiduPayTransaction extends OnlinePaymentTransactionImpl {

	private LooperQueryerTransactionState queryer;

	public BaiduPayTransaction(Context context) {
		super(context);
	}

	protected String sercrt() throws Exception {
		String sec = Des3.encode(AppConfigHelper.getConfig(AppConfigDef.baidupay_key));
		return sec;
	}

	@Override
	public void handleIntent(Intent intent) {
		super.handleIntent(intent);
		defTransactionType(TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY);
	}

	/**
	 * 轮询
	 */
	protected void looperQuery(final BasePresenter.ResultListener resultListener) {
		queryer = new LooperQueryerTransactionState(transactionInfo.getTranId());
		queryer.setListener(new LooperTask.LooperFinishListener() {
			@Override
			public void onFinish(Object object) {
				Logger2.debug("轮询到结果");
				try {
					if (object == null) {
						resultListener.onFaild(new Response(1, "数据解析失败"));
						return;
					}
					Response response = (Response) object;
					if (response.code == 0) {
						OrderDef orderDef = (OrderDef) response.getResult();
						transactionInfo.setThirdTradeNo(orderDef.getThirdTradeNo());
						if (OrderDef.STATE_PAYED == (orderDef.getState())) {
							printTransInfo();
							Response result = new Response(0, "支付成功", bundleResult());
							resultListener.onSuccess(result);
						} else {
							Response result = new Response(1, OrderDef.getOrderStateDes(orderDef.getState()), orderDef);
							resultListener.onFaild(result);
						}
					} else {
						resultListener.onFaild(response);
					}
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "数据解析失败"));
				}
			}
		});
		queryer.start();
	}

	@Override
	public void printTransInfo() {
		if (transactionInfo.isNeedPrint()) { return; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		if(isMixTransaction()){
			printString += builder.center(builder.bold("组合支付|百付宝支付"));
		}else{
			printString += builder.center(builder.bold("百付宝支付"));
		}
		printString += builder.branch();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid) + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName) + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId) + builder.br();
		// Logger2.debug("订单号：" + transactionInfo.getTranId());
		printString += "流水号："
				+ (!TextUtils.isEmpty(transactionInfo.getTranLogId()) ? transactionInfo.getTranLogId() : "")
				+ builder.br();
		if (!TextUtils.isEmpty(transactionInfo.getThirdTradeNo())) {
			printString += "百付宝交易号：" + builder.br() + transactionInfo.getThirdTradeNo() + builder.br();
		}
		if(isMixTransaction()){
			printString += "总金额:" + Calculater.divide100(transactionInfo.getMixInitAmount()) + builder.br();
		}
		printString += "收银：" + Calculater.divide100(transactionInfo.getInitAmount()) + "元" + builder.br();
		String discountAmount = transactionInfo.getDiscountAmount();
		if (!TextUtils.isEmpty(discountAmount) && !"0".equals(discountAmount)) {
			printString += "折扣减价：" + Calculater.divide100(discountAmount) + "元" + builder.br();
		}
		printString += "扣减：" + Calculater.divide100(transactionInfo.getReduceAmount()) + "元" + builder.br();
		printString += "应收：" + Calculater.divide100(transactionInfo.getShouldAmount()) + "元" + builder.br();
		printString += "实收：" + Calculater.divide100(transactionInfo.getRealAmount()) + "元" + builder.br();
		printString += "找零：" + Calculater.divide100(transactionInfo.getChangeAmount()) + "元" + builder.br();
		String giftPoints = transactionInfo.getGiftPoints();
		if (giftPoints != null && !giftPoints.equals("0")) {
			printString += "赠送积分：" + giftPoints + builder.br();
		}
		printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + builder.br();
		printString += builder.branch();
		String tranLogId = transactionInfo.getTranLogId();
		if (!TextUtils.isEmpty(tranLogId) && Constants.NEED_BAR_CODE_FLAG) {
			controller.print(printString);
			//打印撤销用的流水号条码@yaosong [20151107]
			controller.print(builder.bold(tranLogId) + builder.br());
			controller.print(Tools.getBarcode(tranLogId.replaceFirst(AppConfigHelper.getConfig(AppConfigDef.mid),"")
										, 450, 150));
			controller.print(builder.branch() + builder.endPrint());
		}else{
			printString += builder.endPrint();
			controller.print(printString);
		}
		controller.cutPaper();
		LastPrintHelper.beginTransaction().addString(printString).commit();
	}

	@Override
	public void onDestory() {
		if (queryer != null) {
			queryer.onDestory();
		}
	}
}
