package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.model.MarketPayReq;
import com.wizarpos.pay.model.ThirdGiveTicketsReq;


public class TransactionMarketingActivitiesPresenter {
	private Context context;
	private MarketPayReq req;
	
	public TransactionMarketingActivitiesPresenter()
	{
		
	}
	
	public TransactionMarketingActivitiesPresenter(Context context,
			MarketPayReq req) {
		this.context = context;
		this.req = req;
	}
	public void goMarketPay(MarketPayReq req,final ResultListener listener){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mid", req.getMid());
		params.put("cardNo", req.getCardNo());
		params.put("payMethod", req.getPayMethod());
		params.put("rechargeOn", req.getReChangeOn());
		params.put("payAmount", new BigDecimal(req.getPayAmount()));
		NetRequest.getInstance().addRequest(Constants.SC_912_MARKETING_PAY, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-9-29 下午1:45:10 
	 * @Description: 营销规则二期 支付完后调用此接口
	 */
	public void thirdGiveTickets(ThirdGiveTicketsReq req,final ResultListener listener)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tranLogId", req.getTranLogId());
		NetRequest.getInstance().addRequest(Constants.SC_872_THIRD_GIVE_TICKETS, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				String resultStr = response.getResult().toString();
				List<com.wizarpos.pay.cashier.model.TicketInfo> publishTicketsBean = JSONArray
						.parseArray(resultStr,com.wizarpos.pay.cashier.model.TicketInfo.class);
				response.setResult(publishTicketsBean);
				listener.onSuccess(response);
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	
}
