package com.wizarpos.pay.cashier.presenter.ticket.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionImpl;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicektBean;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicketActivity;
import com.wizarpos.pay.common.utils.FileUtil;

/**
 * 发行微盟券
 * 
 * @author wu
 * 
 */
public class WemengTicketPublisher extends TransactionImpl {
	private TicketInfo ticketInfo;

	public WemengTicketPublisher(Context context, TicketInfo ticketInfo) {
		super(context);
		this.ticketInfo = ticketInfo;
	}

	public TicketInfo getTicketInfo() {
		return ticketInfo;
	}

	/**
	 * 不送券
	 */
	public void noteCloseTicket(final ResultListener listener){
		doTicketPublishAction("0", listener);
	}
	
	/**
	 * 发券
	 * @param isUsedWemngTicket
	 * @param listener
	 */
	public void publishTicket(boolean isUsedWemngTicket, final ResultListener listener) {
		doTicketPublishAction(isUsedWemngTicket?"3":"1", listener);
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-16 上午10:10:33 
	 * @param amount 
	 * @Description:微盟第三方支付完后进行查券
	 */
	public void getTicketForThirdPay(String amount,final ResponseListener listener) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		NetRequest.getInstance().addRequest(Constants.SC_506_TICKET_FF_QUERY, params, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				//---------- 微盟券 wu----------------------
				TicketInfo wemengTicketInfo = null;
				try {
					if(robj.containsKey("giftTicket") ){
						if(!((JSONObject)robj.get("giftTicket")).isEmpty()){
							wemengTicketInfo = Tools.jsonObjectToJavaBean((JSONObject) robj.get("giftTicket"), TicketInfo.class);
							wemengTicketInfo.setIsWeiMengTicket("1");//标示是微盟券
							if(wemengTicketInfo.getWeiMengType().equals(TicketInfo.WEIMENT_TYPE_URL)) {
								printWemobUrl(wemengTicketInfo.getUrl());
								listener.onSuccess(new Response(0, "success", null));
								return;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//---------- end 微盟券 wu ----------------
				listener.onSuccess(new Response(0, "success", wemengTicketInfo));
			}
			
			@Override
			public void onFaild(Response arg0) {
				listener.onFaild(arg0);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-18 上午10:25:09  
	 * @Description:微盟打印URL 微盟返回券有两种 一种券对象,一种URL,若为URL则直接打印该URL的二维码
	 */
	public void printWemobUrl(String bitmapContent) {
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		printString += builder.center(builder.bold("扫一扫"));
		printString += builder.branch();
		controller.print(printString);
		Bitmap bitmap = Tools.genQRCodeTow(bitmapContent);
		int resizeBitmapSize = builder.getResizeBitmapSize();
		controller.print(Tools.resizeBitmap(bitmap, resizeBitmapSize,
				resizeBitmapSize));
		controller.print(builder.endPrint());
		printString += builder.branch();
		printString = "";
		controller.print(builder.endPrint());
		controller.cutPaper();
	}

	private void doTicketPublishAction(final String grantFlag,
			final ResultListener listener) {
		if (ticketInfo == null || !"1".equals(ticketInfo.isWeiMengTicket) || ticketInfo.getWeiMengType().equals(TicketInfo.WEIMENT_TYPE_URL)) {
			listener.onFaild(new Response(1, "操作失败,没有对应的券"));
			return;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("masterTranLogId", tranLogId);
		params.put("cardType", cardType);
		params.put("cardNo", cardNo);
		params.put("amount", initAmount);
		params.put("grantFlag", grantFlag);// 送券对应code,操作标志(0:不送，1：送券 , 2 核销不送新券，3核销并送新券)
		params.put("giftCardCode", ticketInfo.getTicketNo());
		params.put("giftCardId", ticketInfo.getTicketDef().getId());

		NetRequest.getInstance().addRequest(Constants.SC_501_TICKET_PUBLISH, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				if(!"0".equals(grantFlag)){
					printWemeng();
				}
				listener.onSuccess(response);
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	@Override
	public void trans(ResultListener listener) {

	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

	private void printWemeng() {
		if(DeviceManager.getInstance().isSupprotPrint()){
			PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
			Q1PrintBuilder builder = new Q1PrintBuilder();
			String printString = "";
			printString += builder.center(builder.bold("券发行"));

			String ticketName = ticketInfo.getTicketDef().getTicketName();
			printString += builder.branch();
			printString += builder.center("时间："
					+ builder.normal(DateUtil.format(new Date(), DateUtil.P2)));
			controller.print(printString);
			printString = "";
			String printWx = ticketInfo.getTicketNo();
			int resizeBitmapSize = builder.getResizeBitmapSize(Constants.QRCODE_LENGTH);
			Bitmap bitmap = Tools.genQRCodeTow(printWx);
			controller.print(Tools.resizeBitmap(bitmap, resizeBitmapSize,
					resizeBitmapSize));
			printString += builder.br();
			printString += builder.center("名称:" + builder.normal(ticketName));
			printString += builder.center("券号:"+ builder.normal(ticketInfo.getDisplayCode()));
			if (TextUtils.isEmpty(ticketInfo.getEndDate())||"-1".equals(ticketInfo.getEndDate())) {
				printString += builder.center("截止日期:" + builder.normal("永久有效"));
			} else {
				printString += builder.center("截止日期:" + builder.normal(ticketInfo.getEndDate()));
			}
			printString += builder.branch();
			controller.print(printString);
			printString = "";
			controller.print(builder.endPrint());
			controller.cutPaper();
		}else{
			List<DisplayTicektBean> displayTicektBeans = new ArrayList<DisplayTicektBean>();
			DisplayTicektBean ticektBean = new DisplayTicektBean();
			ticektBean.setTitle("券发行");
			ticektBean.setSubTitle("时间:" + DateUtil.format(new Date(), DateUtil.P2));
			Bitmap bitmap = Tools.genQRCodeTow(ticketInfo.getTicketNo());
			String filePath =FileUtil.saveBitmap(bitmap);
			ticektBean.setBitmapPath(filePath);
			String endData = (TextUtils.isEmpty(ticketInfo.getEndDate())||"-1".equals(ticketInfo.getEndDate())) ? "永久有效" : ticketInfo.getEndDate();
			ticektBean.setEndString("名称:"+ ticketInfo.getTicketDef().getTicketName() + "\n" + "券号:" + ticketInfo.getDisplayCode() + "\n" + "截止日期:" + endData);
			displayTicektBeans.add(ticektBean);
			if(displayTicektBeans.isEmpty() == false){
				Intent intent = new Intent(context , DisplayTicketActivity.class);
				intent.putExtra(DisplayTicketActivity.DISPLAY_TICKET, (Serializable) displayTicektBeans);
				context.startActivity(intent);
			}
		}
	}

}
