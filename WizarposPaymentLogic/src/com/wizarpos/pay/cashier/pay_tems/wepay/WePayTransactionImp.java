package com.wizarpos.pay.cashier.pay_tems.wepay;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wizarpos.atool.log.Logger;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.pay_tems.LooperQueryerTransactionState;
import com.wizarpos.pay.cashier.pay_tems.LooperTask;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.impl.OnlinePaymentTransactionImpl;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.Des3;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.OrderDef;

public class WePayTransactionImp extends OnlinePaymentTransactionImpl {
	private LooperQueryerTransactionState queryer;

	public WePayTransactionImp(Context context) {
		super(context);
	}

	/**
	 * 对微信的key进行加密
	 * 
	 * @return
	 * @throws Exception
	 */
	protected String getKey() throws Exception {
		String key = Des3.encode(AppConfigHelper.getConfig(AppConfigDef.weixin_app_key));
		return key;
	}

	@Override
	public void handleIntent(Intent intent) {
		super.handleIntent(intent);
		defTransactionType(TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY);
	}

	/**
	 * 轮询
	 * 
	 * @param resultListener
	 *            轮询回调
	 */
	protected void looperQuery(final BasePresenter.ResultListener resultListener) {
		queryer = new LooperQueryerTransactionState(transactionInfo.getTranId());
		queryer.setListener(new LooperTask.LooperFinishListener() {
			@Override
			public void onFinish(Object object) {
				Logger.debug("轮询到结果");
				try {
					if (object == null) {
						resultListener.onFaild(new Response(1, "Data parsing failed"));
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
					resultListener.onFaild(new Response(1, "Data parsing failed"));
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
		StringBuilder sb = new StringBuilder();
		if(isMixTransaction()){
			sb.append(builder.center(builder.bold("组合支付|微信支付")));
		}else{
			sb.append(builder.center(builder.bold("微信支付")));
		}
		sb.append(builder.branch());
		sb.append("慧商户号：").append(AppConfigHelper.getConfig(AppConfigDef.mid)).append(builder.br());
		sb.append("商户名称：").append(AppConfigHelper.getConfig(AppConfigDef.merchantName)).append(builder.br());
		sb.append("终端号：").append(AppConfigHelper.getConfig(AppConfigDef.terminalId)).append(builder.br());
		sb.append("流水号：").append(!TextUtils.isEmpty(transactionInfo.getTranLogId()) ? transactionInfo.getTranLogId() : "").append(builder.br());
		// Logger.debug("订单号：" + transactionInfo.getTranId());
		if (!TextUtils.isEmpty(transactionInfo.getThirdTradeNo())) {
			sb.append("微信交易号：").append(builder.br()).append(transactionInfo.getThirdTradeNo()).append(builder.br());
		}
		if(isMixTransaction()){
			sb.append("总金额: ").append(Calculater.divide100(transactionInfo.getMixInitAmount())).append(builder.br());
		}
		sb.append("收银：").append(Calculater.divide100(transactionInfo.getInitAmount())).append("元").append(builder.br());
		String discountAmount = transactionInfo.getDiscountAmount();
		if (!TextUtils.isEmpty(discountAmount) && !"0".equals(discountAmount)) {
			sb.append("折扣减价：").append(Calculater.divide100(discountAmount)).append("元").append(builder.br());
		}
		sb.append("扣减：").append(Calculater.divide100(transactionInfo.getReduceAmount())).append("元").append(builder.br());
		sb.append("应收：").append(Calculater.divide100(transactionInfo.getShouldAmount())).append("元").append(builder.br());
		sb.append("实收：").append(Calculater.divide100(transactionInfo.getRealAmount())).append("元").append(builder.br());
		sb.append("找零：").append(Calculater.divide100(transactionInfo.getChangeAmount())).append("元").append(builder.br());
		String giftPoints = transactionInfo.getGiftPoints();
		if (giftPoints != null && !giftPoints.equals("0")) {
			sb.append("赠送积分：").append(giftPoints).append(builder.br());
		}
		sb.append("时间：").append(DateUtil.format(new Date(), DateUtil.P2)).append(builder.br()).append(builder.branch());
		String tranLogId = transactionInfo.getTranLogId();
		if (!TextUtils.isEmpty(tranLogId) && Constants.NEED_BAR_CODE_FLAG) {
			//打印撤销用的流水号条码@yaosong [20151107]
			sb.append(builder.bold(tranLogId)).append(builder.br());
			controller.print(sb.toString());
			controller.print(Tools.getBarcode(tranLogId.replaceFirst(AppConfigHelper.getConfig(AppConfigDef.mid),""), 300, 100));
			controller.print(builder.branch() + builder.endPrint());
		}else{
			sb.append(builder.endPrint());
			controller.print(sb.toString());
		}
		controller.cutPaper();
	}

	@Override
	public void onDestory() {
		if (queryer != null) {
			queryer.onDestory();
		}
	}

}
