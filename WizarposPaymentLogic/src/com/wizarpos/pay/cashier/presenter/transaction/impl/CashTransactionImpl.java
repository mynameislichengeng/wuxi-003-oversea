package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.cashier.presenter.transaction.inf.CashTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DisplayHelper;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.TokenGenerater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.db.CashPayRepair;
import com.wizarpos.pay.db.TransactionManager;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 现金交易类
 * 
 * @author wu
 */
public class CashTransactionImpl extends TransactionImpl implements CashTransaction {

	private CommonTicketManager commonTicketManager;

	private TransactionManager transactionManager; // 交易管理类

	private TicketInfo wemengTicketInfo;
	/** 券实际总金额*/
	private int ticketTotalAmount;
	
	private String token;//防重复提交@hong
	private boolean isHandled_submit;
	public CashTransactionImpl(Context context) {
		super(context);
		token = TokenGenerater.newToken(); //生成 token wu@[20151125]
	}

	/**
	 * 添加券
	 */
	@Override
	public Response addTicket(TicketInfo ticket, boolean isFromScan) {
		Response response = new Response();
		try {
			if (isOffline) {
				response.setCode(-1);
				response.setMsg("离线不能用券");
				return response;
			}
			/* 更新应付金额和扣减金额 */
			int blance = ticket.getTicketDef().getBalance();
			LogEx.d("TICKET", blance+"");
			if(commonTicketManager.isAddedTicket(ticket.getId())){
				return new Response(1, "该券已添加,不能重复添加");
			}
			//如果912接口 以后返回ticketsIds，可放开注释 @yaosongqwe
/*			String ticketId = ticket.getTicketDef().getId();
			if (!TextUtils.isEmpty(getTicketIds())) {
				//判断该券是否可用(是否满足营销活动) 
				int i = 0;
				if (canUseTickets.size() != 0) {// 营销活动的券
					for (i = 0; i < canUseTickets.size(); i++) {
						if (ticketId.equals(canUseTickets.get(i).getId())) {
							if (!(getHasUsedTicketNumber(ticketId) < canUseTickets.get(i).getAmount())) {
								response.setCode(-1);
								response.setMsg("营销活动优惠券使用已达上限！");
								return response;
							}
						}
					}
					if (i == canUseTickets.size()) {
						response.setCode(-1);
						response.setMsg("营销活动不支持该类型优惠券！");
						return response;
					}
				} else {
					response.setCode(-1);
					response.setMsg("营销活动不支持该类型优惠券！");
					return response;
				}
			}*/
			Response addResult = commonTicketManager.addTicket(ticket, isFromScan);
			if (addResult.code == 0) {
//				addHasUsedTicket(ticketId);	//如果912接口 以后返回ticketsIds，可放开注释 @yaosongqwe
				reduceAmount = Calculater.plus(reduceAmount, blance+""); 
				if(Calculater.compare(blance+"", shouldAmount) > 0){//更新扣减金额 wu
					shouldAmount = "0";
				}else{
					shouldAmount = Calculater.subtract(shouldAmount, blance+"");
				}
//				calculateAddTicket(blance);
				changeAmount = Calculater.subtract(realAmount, shouldAmount);
			}
			return addResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setCode(-1);
		response.setMsg("添加券失败");
		return response;
	}
	
	public void getTicketInfo(String ticketNum, String shouldPayAmount,final ResultListener listener) {
		commonTicketManager.getTicketInfo(ticketNum, initAmount,shouldPayAmount, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
				if (commonTicketInfoResp.getWemengTicket() != null) {
					wemengTicketInfo = commonTicketInfoResp.getWemengTicket();
				}
				listener.onSuccess(new Response(0, "获取券信息成功", commonTicketInfoResp.getTicketInfo()));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	public void getTicketInfo(String ticketNum, final ResultListener listener) {
		getTicketInfo(ticketNum, "", listener);
	}

	@Override
	public void handleIntent(Intent intent) {
		super.handleIntent(intent);
		defTransactionType(TransactionTemsController.TRANSACTION_TYPE_CASH);
		transactionManager = TransactionManager.getInstance();
		commonTicketManager = TicketManagerFactory.createCommonTicketManager(context);
	}
	public void derate(ResultListener listener){
		if (isMixTransaction() == false && Integer.parseInt(shouldAmount) > Integer.parseInt(realAmount)) {
			listener.onSuccess(new Response(0, changeAmount));
		}else {
			listener.onSuccess(new Response(0, "0"));
		}
	}
	@Override
	public void trans(ResultListener listener) {//initAmount-->shouldAmount:当有扣减时，应收金额==实收金额可以消费的。@hong[2015929]
//		if (isMixTransaction() == false && Integer.parseInt(shouldAmount) > Integer.parseInt(realAmount)) {
//			listener.onFaild(new Response(1, "实收金额需要大于或等于应收金额！"));
//			return;
//		}
//		if (TextUtils.isEmpty(realAmount) || Calculater.compare(realAmount, "1") < 0) {
//			listener.onFaild(new Response(1, "交易金额不能为0"));
//			return;
//		}
		changeAmount = Calculater.subtract(realAmount, shouldAmount);// 计算找零金额
		if (isMixTransaction() && Calculater.compare(changeAmount, "0") < 0) {// 组合支付时,找零金额不能小于0
			changeAmount = "0";
		}

		transTime = DateUtil.format(new Date(), DateUtil.P2);
		//@Hwc 在支付的重新验证下是否离线
		if (!isOffline) {// 当没有传入或者传入值为false时, 取app的状态
			isOffline = Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isOffline, Constants.FALSE));
		}
		if (isOffline) {
			/* 保存离线交易数据 */
			offlineTrans(listener);
		} else {
			uploadTrans(listener);
		}
	}

	/**
	 * 离线交易
	 * 
	 * @param listener
	 */
	private void offlineTrans(ResultListener listener) {
		tranLogId = "F" + DateUtil.format(new Date(), DateUtil.P10);
		Map<String, String> params = new HashMap<String, String>();
		params.put("offlineTranLogId", tranLogId);
		params.put("tranTime", transTime + "");
		params.put("merchantId", AppConfigHelper.getConfig(AppConfigDef.merchantId));
		params.put("mid", AppConfigHelper.getConfig(AppConfigDef.mid));

		params.put("cardType", cardType);

		params.put("payAmount", realAmount);
		params.put("extraAmount", reduceAmount);
		params.put("inputAmount", initAmount);
		// m.put("disCount", disCountNeed);
		params.put("cardNo", cardNo == null ? "" : cardNo);
		params.put("issueTicketMode", isAutoPublishTicket);
		params.put("amount", shouldAmount);
		params.put("rechargeOn", rechargeOn);
		params.put("cardName", "现金");
		String offlineMsg = JSON.toJSONString(params, SerializerFeature.WriteDateUseDateFormat);
		CashPayRepair cashPayRepair = new CashPayRepair();
		cashPayRepair.setCreateTime(transTime);
		cashPayRepair.setMsg(offlineMsg);
		cashPayRepair.setId(tranLogId);
		cashPayRepair.setState("-1");// 支付易的state值为-1
		transactionManager.addTransaction(cashPayRepair);

		DisplayHelper.getInstance().cashPaySuccess(shouldAmount, realAmount, changeAmount);
		if (isNeedPrintCommonTikcet) {
			print();
		}

		listener.onSuccess(new Response(0, "success", bundleResult()));// 打包返回数据
	}

	/**
	 * 打印
	 */
	private String print() {
		if (isNeedPrint == false) { return null; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		StringBuilder sb = new StringBuilder();
		if (isMixTransaction()) {
			sb.append(builder.center(builder.bold("组合支付|现金消费")));
		} else {
			sb.append(builder.center(builder.bold("现金消费")));
		}
		sb.append(builder.branch());
		sb.append("慧商户号：").append(AppConfigHelper.getConfig(AppConfigDef.mid, "")).append(builder.br());
		sb.append("商户名称：").append(AppConfigHelper.getConfig(AppConfigDef.merchantName, "")).append(builder.br());
		sb.append("终端号：").append(AppConfigHelper.getConfig(AppConfigDef.terminalId, "")).append(builder.br());
		sb.append("流水号：").append(tranLogId).append(builder.br());
		if (isMixTransaction()) {
			LogEx.d("mixpay", "print mixInitAmount -->" + mixInitAmount);
			sb.append("总金额:").append(Calculater.divide100(mixInitAmount)).append(builder.br());
		}
		sb.append("收银：").append(Calculater.divide100(initAmount)).append("元").append(builder.br());
		if (!TextUtils.isEmpty(discountAmount) && !"0".equals(discountAmount)) {
			sb.append("折扣减价：").append(Calculater.divide100(discountAmount)).append("元").append(builder.br());
		}
		String printReduceAmount = Calculater.subtract(reduceAmount + "", ticketTotalAmount + "");
		sb.append("扣减：").append(Calculater.divide100(printReduceAmount)).append("元").append(builder.br());
		sb.append("券总计：").append(Calculater.divide100(ticketReduceAomount + "")).append("元").append(builder.br());
		sb.append("应收：").append(Calculater.divide100(shouldAmount)).append("元").append(builder.br());
		sb.append("实收：").append(Calculater.divide100(realAmount)).append("元").append(builder.br());
		if (!"0".equals(round)) {
			sb.append("四舍五入：").append(Calculater.divide100(round)).append("元").append(builder.br());
		}
		sb.append("找零：").append(Calculater.divide100(changeAmount)).append("元").append(builder.br());
		if (!TextUtils.isEmpty(giftPoints) && !giftPoints.equals("0")) {
			sb.append("赠送积分：").append(giftPoints).append(builder.br());
		}
		sb.append("时间：").append(transTime).append(builder.br()).append(builder.branch());
		sb.append(commonTicketManager.getCommonTicketPrintInfo());
		sb.append(builder.endPrint());
		String printString = sb.toString();
		controller.print(printString);
		return printString;
	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}
	
	@Override
	public void setRealAmount(String realAmount) {
		this.realAmount = realAmount;
		if ((TextUtils.isEmpty(realAmount)||TextUtils.isEmpty(shouldAmount)) == false) {//非空判断@hong[20160308]
			changeAmount = Calculater.subtract(realAmount, shouldAmount);// 计算找零金额
		}

	}

	public void setRound(double round) {
		this.round = Tools.toIntMoney(String.valueOf(round)) + "";
		shouldAmount = Calculater.subtract(shouldAmount, this.round);
	}
	
	/**
	 * 上送交易
	 * 
	 * @param listener
	 */
	private void uploadTrans(final ResultListener listener) {
		List<String> ids = new ArrayList<String>();
		List<TicketInfo> tickets = commonTicketManager.getAddedTickets();
		String reduceStr = Calculater.subtract(initAmount, shouldAmount);
		if (tickets != null && !tickets.isEmpty()) {
			for (TicketInfo ticket : tickets) {
				String idStr ;
				if ("true".equals(ticket.getIsFromScan())) {
					idStr = ticket.getId() + "|1";// 如果是扫码的券,值未1,否则为0
				} else {
					idStr = ticket.getId() + "|0";
				}
				// ids.add(ticket.getId() + "|1");// 现金消费过程中使用的券 都是 扫描的券,所以恒为1

				Integer balance = ticket.getTicketDef().getBalance();
				ticketTotalAmount += balance;
				if(Calculater.compare(reduceStr, balance+"") > 0){ //计算券的实际抵消金额 wu
					idStr += ("|" + balance);
					reduceStr = Calculater.subtract(reduceStr, balance+"");
					ticketReduceAomount += balance;
				}else{
					idStr += ("|" + reduceStr);
					ticketReduceAomount += Integer.valueOf(reduceStr);
					reduceStr = "0";
				}
				
				ids.add(idStr);
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardType", cardType);
		params.put("cardNo", cardNo);
//		params.put("payAmount", realAmount);
		params.put("token", token); //增加 token 字段
		if (isMixTransaction()) {
			params.put("payAmount", Calculater.compare(realAmount, shouldAmount) > 0 ? shouldAmount : realAmount);
		}else {
			//changeAmount payAmount=应收金额-减免金额
			params.put("payAmount", Calculater.compare(changeAmount, "0") > 0 ? shouldAmount : Calculater.plus(shouldAmount, changeAmount));
		}
		params.put("extraAmount", reduceAmount);
		
		if(!TextUtils.isEmpty(saleInputAmount) &&!saleInputAmount.equals("0")){ 
			params.put("inputAmount", saleInputAmount);		//当有销售金额传入时,使用 saleInputAmout wu@[20151120]	
		}else{
			params.put("inputAmount", initAmount);			
		}
		
		params.put("issueTicketMode", isAutoPublishTicket);
		params.put("rechargeOn", rechargeOn);
		if(isMixTransaction()){ //增加传入混合支付的总金额 wu
			params.put("masterPayAmount", mixInitAmount);//组合支付总金额
		}
		if(PaymentApplication.getInstance().isWemengMerchant()){
			params.put("amount", initAmount);//为微盟券增加 wu
		}
		if (commonTicketManager.isContainsWemengTicket()) {
			params.put("ticketNo", commonTicketManager.getAddedWemengTicketNo());
		} else {
			params.put("ids", ids);
		}
		params.put("cardName", "现金");
		if (isMixTransaction()) {// 混合支付
			LogEx.d("混合支付主流水号", "现金上送:" + mixTranLogId);
			params.put("masterTranLogId", mixTranLogId);
			params.put("mixFlag", 1);
			params.put("isPayComingForm", isPayComingForm);
		}
		if (!"0".equals(round)) {
			params.put("round",round);
		}
		NetRequest.getInstance().addRequest(Constants.SC_400_CASH_PAY, params, new ResponseListener() {

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}

			@Override
			public void onSuccess(Response response) {
				if (isMixTransaction()) {
					String jsonString = JSON.toJSONString(response.getResult(),
							SerializerFeature.DisableCircularReferenceDetect);
					JSONObject jsonObject = JSON.parseObject(jsonString);
					JSONObject jTranLog = jsonObject.getJSONObject("slaveTranLog").getJSONObject("tranLog");// 子流水
					mixTranLogId = jTranLog.getString("masterTranLogId");// 混合支付主流水号
					LogEx.d("混合支付主流水号", "现金返回:" + mixTranLogId);
					tranLogId = jTranLog.getString("id");// 混合支付子流水号
					//防止订单重复提交@hong 20151217
					LogEx.d("jTranLog", jTranLog.toString());
					String tokenReturn = jTranLog.getString("token");
					boolean doubleClickFlag = jsonObject.getBoolean("doubleClickFlag");
					LogEx.d("token", "current token: " + token );
					LogEx.d("token", "return token: " + tokenReturn + " - doubleClickFlag: " + doubleClickFlag);
					if(!token.equals(tokenReturn)){ //增加 toke 验证逻辑
						return ;
					}
					if(doubleClickFlag && isHandled_submit){
						return ;
					}else{
						isHandled_submit = true;
						LogEx.d("token", "isHandled_submit: "+ isHandled_submit);
					}	
					print();
					response.code = 0;
					response.msg = "success";
					response.result = bundleResult();
					listener.onSuccess(response);
				} else {
					JSONObject _jsonObject = (JSONObject) response.getResult();
					JSONObject _jTranLog = _jsonObject.getJSONObject("tranLog");
					String _publishTickets = _jsonObject.getString("publishTickets");
					giftPoints = _jsonObject.getString("tranPoints");
					tranLogId = _jTranLog.getString("masterTranLogId");
					// transTime = _jTranLog.getLongValue("tranTime")+"";
					//防止订单重复提交@hong 20151217
					LogEx.d("jTranLog", _jTranLog.toString());
					String tokenReturn = _jTranLog.getString("token");
					boolean doubleClickFlag = _jTranLog.getBoolean("doubleClickFlag");
					LogEx.d("token", "current token: " + token );
					LogEx.d("token", "return token: " + tokenReturn + " - doubleClickFlag: " + doubleClickFlag);
					if(!token.equals(tokenReturn)){ //增加 toke 验证逻辑
						return ;
					}
					if(doubleClickFlag && isHandled_submit){
						return ;
					}else{
						isHandled_submit = true;
						LogEx.d("token", "isHandled_submit: "+ isHandled_submit);
					}	
					// ---------------微盟券 wu----------------------
					try {
						if (wemengTicketInfo == null && _jsonObject.containsKey("giftTicket")) {
							if (((JSONObject) _jsonObject.get("giftTicket")).isEmpty() == false) {
								wemengTicketInfo = Tools.jsonObjectToJavaBean(
										(JSONObject) _jsonObject.get("giftTicket"), TicketInfo.class);
								wemengTicketInfo.setIsWeiMengTicket("1");//标示是微盟券
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// ---------------微盟券 wu----------------------

					print();
					if (!TextUtils.isEmpty(_publishTickets)) {// 打印券
						List<TicketInfo> ticketInfos = JSONArray.parseArray(_publishTickets, TicketInfo.class);
						commonTicketManager.printTickets(ticketInfos);
					}

					DisplayHelper.getInstance().cashPaySuccess(shouldAmount, realAmount, changeAmount);

					response.code = 0;
					response.msg = "success";
					response.result = bundleResult();
					listener.onSuccess(response);
				}
			}
		});
	}

	@Override
	protected Intent bundleResult() {
		Intent intent = super.bundleResult();
		if(PaymentApplication.getInstance().isWemengMerchant()){
//			intent.putExtra(Constants.isUsedWemngTicket, commonTicketManager.isContainsWemengTicket()); //为微盟券增加
			intent.putExtra(Constants.wemengTicketInfo, wemengTicketInfo);
		}
		return intent;
	}
}
