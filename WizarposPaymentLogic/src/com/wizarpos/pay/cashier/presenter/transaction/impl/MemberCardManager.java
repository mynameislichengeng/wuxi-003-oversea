package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.MemberCardInfoBean;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.transaction.bean.MerchantCardQueryBean;
import com.wizarpos.pay.cashier.presenter.transaction.bean.MerchantCardQueryResponse;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.base.net.NetRequest;

/**
 * 会员卡管理类 Created by wu on 2015/4/13 0013.
 */
public class MemberCardManager {

	protected List<TicketInfo> allTickets = new ArrayList<TicketInfo>();

	/**
	 * 获取会员卡下所有的券
	 */
	public List<TicketInfo> getAllTickets() {
		return allTickets;
	}

	/**
	 * 券列表中是否又这张券
	 * 
	 * @param ticketId
	 *            券号
	 */
	public boolean hasTicket(String ticketId) {
		for (TicketInfo ticket : allTickets) {
			if (ticket.getId().equals(ticketId)) { return true; }
		}
		return false;
	}

	/**
	 * 查询会员卡信息
	 * 
	 * @param cardNum
	 *            卡号
	 * @param listener
	 *            回调
	 */
	public void queryMemberCard(String cardNum, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardNo", cardNum);
		NetRequest.getInstance().addRequest(Constants.SC_301_MERCHANT_CARD_QUERY, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						// 查会员卡
						JSONObject robj = (JSONObject) response.getResult();
						if (robj == null) {
							response.code = 1;
							response.msg = "卡信息不存在";
							listener.onFaild(response);
						} else {
							response.code = 0;
							response.msg = "查询卡信息成功";
							listener.onSuccess(response);
						}
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(response);
					}
				});
	}
	
	public void merchantCardQuery(MerchantCardQueryBean bean,final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardNo", bean.getCardNo());
		params.put("mobileNo", bean.getMobileNo());
		params.put("username", bean.getUsername());
		params.put("birthday", bean.getBirthday());
		params.put("cardType", bean.getCardType());
		NetRequest.getInstance().addRequest(Constants.SC_341_MERCHANT_CARD_QUERY_UNION, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						JSONArray jsonArray = (JSONArray) response.getResult();
						List<MerchantCardQueryResponse> beanList = JSONObject.parseArray(jsonArray.toJSONString(), MerchantCardQueryResponse.class);
						response.setResult(beanList);
						listener.onSuccess(response);
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(response);
					}
				});
	}

	/**
	 * 初始化微信会员卡信息
	 * 
	 * @param tokenId
	 *            微信token
	 * @param listener
	 *            回调
	 */
	public void getWepayCardNo(String tokenId, final BasePresenter.ResultListener listener) {
		LogEx.d("MemeberCard", "微信会员卡TOKEN:" + tokenId);
		queryWepayCard(tokenId, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				if (response.getResult() == null) {
					LogEx.d("MemeberCard", "微信会员卡信息查询:查询卡信息失败,返回数据为空");
					listener.onFaild(new Response(1, "获取会员卡号失败"));
					return;
				}
				JSONObject robj = (JSONObject) response.getResult();
				String cardNum = robj.getString("cardNo");
				if (TextUtils.isEmpty(cardNum)) {
					LogEx.d("MemeberCard", "微信会员卡信息查询:查询卡信息失败,cardNo为空");
					listener.onFaild(new Response(1, "获取会员卡号失败"));
				} else {
					LogEx.d("MemeberCard", "微信会员卡信息查询:查询卡信息成功,cardNo:" + cardNum);
					listener.onSuccess(new Response(0, "获取会员卡号成功", cardNum));
				}
			}

			@Override
			public void onFaild(Response response) {
				LogEx.d("MemeberCard", "微信会员卡信息查询:查询卡信息失败");
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 初始化会员卡信息
	 * 
	 * @param cardNum
	 *            卡号
	 * @param listener
	 *            回调
	 */
	public void initMemberCardInfo(final String cardNum, final BasePresenter.ResultListener listener) {
		LogEx.d("MemeberCard", "初始化会员卡信息,传入卡号:" + cardNum);
		queryMemberCard(cardNum, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				if (response.getResult() == null) {
					LogEx.d("MemeberCard", "会员卡信息查询:查询卡信息失败,数据解析异常");
					listener.onFaild(new Response(1, "获取会员卡信息失败"));
					return;
				}
				MemberCardInfoBean cardInfo = JSONObject.parseObject(response.getResult().toString(),
						MemberCardInfoBean.class);
				listener.onSuccess(new Response(0, "success", cardInfo));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 微信会员卡信息查询
	 */
	public void queryWepayCard(String tokenId, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tokenId", tokenId);
		NetRequest.getInstance().addRequest(Constants.SC_810_WEIXIN_CARD_QUERY, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						JSONObject robj = (JSONObject) response.getResult();
						if (robj == null) {
							response.code = 1;
							response.msg = "卡信息不存在";
							listener.onFaild(response);
						} else {
							response.code = 0;
							response.msg = "查询卡信息成功";
							listener.onSuccess(response);
						}
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(response);
					}
				});
	}

}
