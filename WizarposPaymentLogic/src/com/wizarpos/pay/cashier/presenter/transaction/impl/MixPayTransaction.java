package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.model.TransactionInfo2;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionImpl2;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.MixWepayTicketBean;

/**
 * 混合支付
 * 
 * @author wu
 * 
 */
public class MixPayTransaction extends TransactionImpl2 {

	public MixPayTransaction(Context context) {
		super(context);
	}

	private String disCountTime;

	@Override
	public void handleIntent(Intent intent) {
		super.handleIntent(intent);
		if(transactionInfo.getMixInitAmount() == 0){
			transactionInfo.setMixInitAmount(transactionInfo.getInitAmount());
		}
		transactionInfo.setTransactionType(TransactionTemsController.TRANSACTION_TYPE_MIXPAY);
	}

	/**
	 * 增加子支付方式
	 */
	public void addTransactionInfo(TransactionInfo2 trInfo) {
		LogEx.d("混合支付主流水号", trInfo.getMixTranLogId());
		transactionInfo.getPayedTransactionInfo().add(trInfo);
		transactionInfo.addRealAmount(trInfo.getRealAmount());
		transactionInfo.setMixTranLogId(trInfo.getMixTranLogId());
	}

	@Override
	public void trans(ResultListener listener) {

	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

	public TransactionInfo2 getMixTransactionInfo() {
		return transactionInfo;
	}

	/**
	 * 折扣处理--直接折扣
	 * 
	 * @param discountAmount
	 *            直接扣减金额
	 */
	public Response handleReduceByAmount(int discountAmount) {
		if (transactionInfo.getDiscountPrecent() != 100) { return new Response(1, "折扣只能是 直接折扣 或 比例折扣 ,不能重叠!"); }
		disCountTime = DateUtil.format(new Date(), DateUtil.P2);
		transactionInfo.addDiscountAmount(discountAmount);// 增加折扣金额
		LogEx.d("折扣处理", "直接折扣设置成功:" + discountAmount);
		return new Response(0, "success",discountAmount);
	}

	/**
	 * 
	 * 折扣处理--比例折扣
	 * 
	 * @param discountPrecent
	 */
	public Response handleReduceByPrecent(int discountPrecent) {
		if (transactionInfo.getDiscountAmount() == 100 && transactionInfo.getDiscountAmount() != 0) { return new Response(
				1, "折扣只能是 直接折扣 或 比例折扣 ,不能重叠!"); }
		disCountTime = DateUtil.format(new Date(), DateUtil.P2);
		int discountAmount = transactionInfo.getShouldAmount() * (100 - discountPrecent) / 100;
		transactionInfo.addDiscountAmount(discountAmount);
		LogEx.d("折扣处理", "比例折扣设置成功:" + discountPrecent);
		return new Response(0, "success", discountAmount);
	}

	public boolean isReduced() {
		return transactionInfo.getDiscountAmount() != 0;
	}

	/**
	 * 折扣处理
	 */
	public void uploadDiscount(final String precent, final String amount, final ResultListener listener) {
		if (!isReduced()) {
			LogEx.d("折扣处理", "没有折扣");
			listener.onSuccess(new Response(0, "success"));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		int precentInt = 0;
		if(TextUtils.isEmpty(precent) == false){
			try {
				precentInt = Integer.parseInt(precent);				
			} catch (Exception e) {
			}
		}
		if (0 < precentInt && precentInt < 100) {
			params.put("disCount", precent);
		}
		params.put("disAmount", amount);
		LogEx.d("混合支付主流水号", "折扣 上送 " + transactionInfo.getMixTranLogId());
		params.put("masterTranLogId", transactionInfo.getMixTranLogId());
		params.put("mixFlag", 1);
		params.put("inputAmount", transactionInfo.getInitAmount());
		params.put("isPayComingForm", transactionInfo.getIsPayComingForm());
		params.put("cardNo", transactionInfo.getCardNo());
		params.put("masterPayAmount", transactionInfo.getMixInitAmount());//组合支付总金额
		params.put("inputAmount", transactionInfo.getMixInitAmount());
		NetRequest.getInstance().addRequest(com.wizarpos.pay.common.Constants.SC_796_MIX_PAY_DISCOUNT, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						LogEx.d("MixPayTransaction", "混合支付_折扣成功");
						String jsonString = JSON.toJSONString(response.getResult(),SerializerFeature.DisableCircularReferenceDetect);
						JSONObject jsonObject = JSON.parseObject(jsonString);
						JSONObject jTranLog = jsonObject.getJSONObject("slaveTranLog");// 子流水
						transactionInfo.setMixTranLogId(jTranLog.getString("masterTranLogId"));
						LogEx.d("混合支付主流水号", "折扣 返回 " + transactionInfo.getMixTranLogId());
						TransactionInfo2 info = new TransactionInfo2();
						info.setTranLogId(jTranLog.getString("id"));// 混合支付子流水号
						info.setMixTranLogId(jTranLog.getString("masterTranLogId"));
						info.setTransactionType(TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_DISCOUNT);
						info.setDiscountAmount(transactionInfo.getDiscountAmount());
						printDiscount(info);
						listener.onSuccess(new Response(0, "折扣成功"));
					}

					@Override
					public void onFaild(Response arg0) {
						LogEx.d("MixPayTransaction", "混合支付_折扣失败:" + arg0.getMsg());
						listener.onFaild(arg0);
					}
				});
	}

	/**
	 * 零头处理
	 */
	public void handleChange(final ResultListener listener) {
		final int reduceAmount;
		int shouldAmount = transactionInfo.getShouldAmount();
		if (shouldAmount > 99) {
			reduceAmount = shouldAmount - (shouldAmount / 100) * 100;
		} else {
			reduceAmount = shouldAmount;
		}
		if (reduceAmount == 0) {
			listener.onSuccess(new Response(0, "success"));
			return;
		}
		LogEx.d("MixPayTransaction", "混合支付_抹0" + reduceAmount + "   ----应付金额:" + transactionInfo.getShouldAmount());
		Map<String, Object> params = new HashMap<String, Object>();
		LogEx.d("混合支付主流水号", "抹0 上送 " + transactionInfo.getMixTranLogId());
		params.put("masterTranLogId", transactionInfo.getMixTranLogId());
		params.put("mixFlag", 1);
		params.put("wipeZeroAmount", reduceAmount);
		params.put("inputAmount", transactionInfo.getInitAmount());
		params.put("isPayComingForm", transactionInfo.getIsPayComingForm());
		params.put("cardNo", transactionInfo.getCardNo());
		params.put("masterPayAmount", transactionInfo.getMixInitAmount());//组合支付总金额
		params.put("inputAmount", transactionInfo.getMixInitAmount());
		NetRequest.getInstance().addRequest(com.wizarpos.pay.common.Constants.SC_795_MIX_PAY_WIPE_ZERO, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						LogEx.d("MixPayTransaction", "混合支付_抹0 成功");
						String jsonString = JSON.toJSONString(response.getResult(), SerializerFeature.DisableCircularReferenceDetect);
						JSONObject jsonObject = JSON.parseObject(jsonString);
						JSONObject jTranLog = jsonObject.getJSONObject("slaveTranLog");// 子流水
						transactionInfo.setMixTranLogId(jTranLog.getString("masterTranLogId"));
						LogEx.d("混合支付主流水号", "抹0 返回 " + transactionInfo.getMixTranLogId());
						transactionInfo.addReduceAmount(reduceAmount);
						LogEx.d("MixPayTransaction", "混合支付_抹0 结束" + reduceAmount + "   ----应付金额:" + transactionInfo.getShouldAmount());
						TransactionInfo2 info = new TransactionInfo2();
						info.setTranLogId(jTranLog.getString("id"));
						info.setMixTranLogId(jTranLog.getString("masterTranLogId"));
						info.setTransactionType(TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL);
						info.setReduceAmount(reduceAmount);
						transactionInfo.getPayedTransactionInfo().add(info);
						printCharge(info);
						listener.onSuccess(new Response(0, "抹零成功"));
					}

					@Override
					public void onFaild(Response arg0) {
						LogEx.d("MixPayTransaction", "混合支付_抹零失败:" + arg0.getMsg());
						listener.onFaild(arg0);
					}
				});
	}

	/**
	 * 打印订单抹0
	 * 
	 * @param info
	 */
	private void printCharge(TransactionInfo2 info) {
		if (transactionInfo.isNeedPrint() == false) { return; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		printString += builder.center(builder.bold("抹零|组合支付"));
		printString += builder.branch();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
		printString += "流水号：" + info.getTranLogId() + builder.br();
		printString += "总金额：" + Calculater.divide100(transactionInfo.getInitAmount()+"") + builder.br();
		printString += "抹零金额：" + Calculater.divide100(info.getReduceAmount()+"") + "元" + builder.br();
		printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + builder.br();
		printString += builder.branch();
		printString += builder.endPrint();
		controller.print(printString);
	}

	/**
	 * 打印订单折扣
	 * 
	 * @param info
	 */
	private void printDiscount(TransactionInfo2 info) {
		if (transactionInfo.isNeedPrint() == false) { return; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		printString += builder.center(builder.bold("折扣|组合支付"));
		printString += builder.normal("--------------------------------") + builder.br();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
		printString += "流水号：" + info.getTranLogId() + builder.br();
		printString += "总金额：" + Calculater.divide100(transactionInfo.getInitAmount()+"") + builder.br();
		printString += "折扣金额：" + Calculater.divide100(info.getDiscountAmount()+"") + "元" + builder.br();
		printString += "时间：" + disCountTime + builder.br();
		printString += builder.normal("--------------------------------") + builder.br() + builder.br();
		printString += builder.branch();
		printString += builder.endPrint();
		controller.print(printString);
	}

	/**
	 * 判断交易是否结束
	 * 
	 * @return
	 */
	public boolean isTransactionFinsh() {
		return transactionInfo.getShouldAmount() <= 0;
	}

	/**
	 * 打包返回结果
	 */
	public Intent bundleResult() {
		Intent intent = new Intent();
		intent.putExtra(Constants.shouldAmount, transactionInfo.getShouldAmount() + "");// 应付金额
		intent.putExtra(Constants.initAmount, transactionInfo.getInitAmount() + "");// 初始金额
		intent.putExtra(Constants.realAmount, transactionInfo.getRealAmount() + "");// 实付金额
		intent.putExtra(Constants.reduceAmount, transactionInfo.getReduceAmount() + "");// 扣减金额
		intent.putExtra(Constants.changeAmount, transactionInfo.getChangeAmount() + "");// 找零金额
		intent.putExtra(Constants.discountAmount, transactionInfo.getDiscountAmount() + "");// 折扣扣减金额
		intent.putExtra(Constants.TRANSACTION_TYPE, TransactionTemsController.TRANSACTION_TYPE_MIXPAY);// function
		intent.putExtra(Constants.mixFlag, "1");// 混合支付flag
		intent.putExtra(Constants.cardNo, transactionInfo.getCardNo());
		intent.putExtra(Constants.mixTranLogId, transactionInfo.getMixTranLogId());// 混合支付主流水号
		return intent;
	}
	
	public void uploadWepayTicketMixTransaction(MixWepayTicketBean ticketBean,final ResultListener listener){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterTranLogId", ticketBean.getMasterTranLogId());
		params.put("masterPayAmount", ticketBean.getMasterPayAmount());
		params.put("mixFlag", ticketBean.getMixFlag());
		params.put("inputAmount", ticketBean.getInputAmount());
		params.put("isPayComingForm", ticketBean.getIsPayComingForm());
		params.put("wxTicketAmount", ticketBean.getWxTicketAmount());
		params.put("wxCode", ticketBean.getWxCode());
		params.put("masterPayAmount", transactionInfo.getMixInitAmount());//组合支付总金额
//		params.put("inputAmount", transactionInfo.getMixInitAmount());//重复设置了两次 @hwc
		if(ticketBean.getWxTrueAmount() != 0) {
			params.put("wxTrueAmount", ticketBean.getWxTrueAmount());
		}
		if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
		{//1 罗森标示 0 正常标示 不传默认0
			params.put("luosenFlag", "1");
		}
		NetRequest.getInstance().addRequest(com.wizarpos.pay.common.Constants.SC_793_MIX_PAY_WXSINGLE_TICKET, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						LogEx.d("MixPayTransaction", "微信卡券核销 成功");
						listener.onSuccess(new Response(0, "微信卡券核销成功",response.result));
					}

					@Override
					public void onFaild(Response arg0) {
						LogEx.d("MixPayTransaction", "混合支付_微信卡券核销失败:" + arg0.getMsg());
						listener.onFaild(arg0);
					}
				});
	}

	public void uploadWepayTicketMixTransaction(final int amount, final String ticketNum, final ResultListener listener) {
		LogEx.d("混合支付", "开始上送混合支付微信卡券核销");
		Map<String, Object> params = new HashMap<String, Object>();
		LogEx.d("混合支付主流水号", "微信卡券核销 上送 " + transactionInfo.getMixTranLogId());
		params.put("masterTranLogId", transactionInfo.getMixTranLogId());
		params.put("masterPayAmount", transactionInfo.getMixInitAmount());//组合支付总金额
		params.put("mixFlag", 1);
		LogEx.d("混合支付", "微信卡券核销上送金额 " + transactionInfo.getInitAmount());
		params.put("inputAmount", transactionInfo.getInitAmount());
		params.put("wxCode", ticketNum);
		params.put("isPayComingForm", transactionInfo.getIsPayComingForm());
		params.put("wxTicketAmount", amount);
		params.put("inputAmount", transactionInfo.getMixInitAmount());
		if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
		{//1 罗森标示 0 正常标示 不传默认0
			params.put("luosenFlag", "1");
		}
		NetRequest.getInstance().addRequest(com.wizarpos.pay.common.Constants.SC_793_MIX_PAY_WXSINGLE_TICKET, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						LogEx.d("MixPayTransaction", "微信卡券核销 成功");
						String jsonString = JSON.toJSONString(response.getResult(), SerializerFeature.DisableCircularReferenceDetect);
						JSONObject jsonObject = JSON.parseObject(jsonString);
						JSONObject jTranLog = jsonObject.getJSONObject("slaveTranLog");// 子流水
						transactionInfo.setMixTranLogId(jTranLog.getString("masterTranLogId"));
						LogEx.d("混合支付主流水号", "微信卡券核销 返回" + transactionInfo.getMixTranLogId());
						TransactionInfo2 info = new TransactionInfo2();
						info.setTranLogId(jTranLog.getString("id"));
						info.setMixTranLogId(jTranLog.getString("masterTranLogId"));
						info.setTransactionType(TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL);
						info.setInitAmount(transactionInfo.getInitAmount());
						info.setShouldAmount(amount);
						info.setRealAmount(amount);
						if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
						{
							printWeixinPass(response.getResult().toString(),info, ticketNum);
						}else
						{
							printWepayTicket(info, ticketNum);
						}
						listener.onSuccess(new Response(0, "微信卡券核销成功", info));
					}

					@Override
					public void onFaild(Response arg0) {
						LogEx.d("MixPayTransaction", "混合支付_微信卡券核销失败:" + arg0.getMsg());
						listener.onFaild(arg0);
					}
				});
	}
	
	public void getWepayTicketInfo(String wxCode,String shouldPayAmount,final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wxCode", wxCode);
		params.put("payFlag", "0");//0查询 1支付
		params.put("shouldPayAmount", shouldPayAmount);
		if(Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)
		{
			params.put("luosenFlag", "1");//0正常 1罗森
		}
		NetRequest.getInstance().addRequest(com.wizarpos.pay.common.Constants.SC_703_WEIXIN_TICKET_DETAIL, params,
				new ResponseListener() {
					
					@Override
					public void onSuccess(Response response) {
						LogEx.i("getWepayTicketInfo", response.getResult().toString());
						listener.onSuccess(response);
					}
					
					@Override
					public void onFaild(Response response) {
						LogEx.i("getWepayTicketInfo", response.getMsg());
						listener.onFaild(response);
					}
				});
	}
	
	public void getWepayTicketInfo(String wxCode,final ResultListener listener){ 
		getWepayTicketInfo(wxCode, "", listener);
	}
	

	/**
	 * 打印订单抹0
	 * 
	 * @param info
	 */
	private void printWepayTicket(TransactionInfo2 info, String ticketNum) {
		if (transactionInfo.isNeedPrint() == false) { return; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		printString += builder.center(builder.bold("微信券|组合支付"));
		printString += builder.branch();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
		printString += "流水号：" + info.getTranLogId() + builder.br();
		printString += "总金额：" + Calculater.formotFen(transactionInfo.getInitAmount() + "") + builder.br();
		printString += "券号：" + ticketNum + builder.br();
		printString += "金额：" + Calculater.formotFen(info.getRealAmount() + "") + "元" + builder.br();
		printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + builder.br();
		printString += builder.branch();
		printString += builder.endPrint();
		controller.print(printString);
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-9 下午11:45:25 
	 * @param response 
	 * @Description:罗森 打印微信券核销
	 */
	private void printWeixinPass(String response,TransactionInfo2 info, String ticketNum)
	{
		if (transactionInfo.isNeedPrint() == false) { return; }
		JSONObject jsonObject = JSONObject.parseObject(response);
		JSONObject ticketDef = jsonObject.getJSONObject("ticketDef");
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		printString += builder.center(builder.bold("微信券|组合支付"));
		printString += builder.branch();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
		printString += "券名称：" + ticketDef.getString("ticketName") + builder.br();
		printString += "流水号：" + info.getTranLogId() + builder.br();
		printString += "总金额：" + Calculater.formotFen(transactionInfo.getInitAmount() + "") + builder.br();
		printString += "券号：" + ticketNum + builder.br();
		printString += "金额：" + Calculater.formotFen(info.getRealAmount() + "") + "元" + builder.br();
		printString += "操作员：" + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName, "") + builder.br();
		printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + builder.br();
		printString += builder.branch();
		printString += builder.endPrint();
		controller.print(printString);
	}
	
	/**
	 * 混合支付核销普通券
	 */
	public void passMixCommonTicket(final TicketInfo ticket, final ResultListener listener) {
		List<String> ids = new ArrayList<String>();
		String idStr ;
		if ("true".equals(ticket.getIsFromScan())) {
			idStr = ticket.getId() + "|1";// 如果是扫码的券,值未1,否则为0
		} else {
			idStr = ticket.getId() + "|0";
		}
		final int balance = ticket.getTicketDef().getBalance();
		if (transactionInfo.getShouldAmount()  > balance){
			idStr += ("|" + balance);
		}else{
			idStr += ("|" + transactionInfo.getShouldAmount()+"");
		}
		ids.add(idStr);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("masterTranLogId", transactionInfo.getMixTranLogId());
		m.put("mixFlag", 1);
		m.put("ids", ids);
		m.put("isPayComingForm", transactionInfo.getIsPayComingForm());
		m.put("masterPayAmount", transactionInfo.getMixInitAmount());//组合支付总金额
		m.put("inputAmount", transactionInfo.getMixInitAmount());
		NetRequest.getInstance().addRequest(com.wizarpos.pay.common.Constants.SC_791_MIX_PAY_UNMEMTICKET, m,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						String jsonString = JSON.toJSONString(response.getResult(),
								SerializerFeature.DisableCircularReferenceDetect);
						JSONObject jsonObject = JSON.parseObject(jsonString);
						JSONObject object = jsonObject.getJSONObject("masterTranLog");
						String tranLogId = object.getString("id");// 子流水
						String mixTranLogId = object.getString("masterTranLogId");// 混合支付主流水号
						TransactionInfo2 cashInfo = new TransactionInfo2();
//						cashInfo.setInitAmount(ticket.getTicketDef().getBalance());
//						cashInfo.setShouldAmount(ticket.getTicketDef().getBalance());
						if(transactionInfo.getShouldAmount()  > balance){
							cashInfo.setRealAmount(balance);
						}else{
							cashInfo.setRealAmount(transactionInfo.getShouldAmount());
						}
						cashInfo.setTransactionType(TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_CANCEL);
						cashInfo.setMixFlag("1");
						cashInfo.setTranLogId(tranLogId);
						cashInfo.setMixTranLogId(mixTranLogId);
						listener.onSuccess(new Response(0, "success", cashInfo));
					}

					@Override
					public void onFaild(Response arg0) {
						listener.onFaild(arg0);
					}
				});
	}	
}
