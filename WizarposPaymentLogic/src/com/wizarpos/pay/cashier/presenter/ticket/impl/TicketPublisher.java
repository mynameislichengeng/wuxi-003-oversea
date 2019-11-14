package com.wizarpos.pay.cashier.presenter.ticket.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketPacket;
import com.wizarpos.pay.cashier.model.TicketPacketInfo;
import com.wizarpos.pay.cashier.model.TicketPublishBean;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * 券发行类
 * 
 * @author wu
 */
public class TicketPublisher {

	// 可发行券定义列表
	protected List<TicketDef> publishableTicketDefs;

	// protected List<TicketDef> publishTicketDefs = new ArrayList<TicketDef>();

	int ticketCount = 0;// 券数量
	public String[] kxitem1 = { "", "", "" };
	public String[] kxitem2 = { "", "", "" };
	public String[] kxitem3 = { "", "", "" };
	public String[] kxitem4 = { "", "", "" };
	public int kxamount = 0;

	public TicketPublisher(Context context) {
		super();
	}

	/**
	 * 获取可发行的券
	 * 
	 * @param tranLogId
	 *            流水号
	 * @param cardType
	 *            卡类型 可为空
	 * @param showFlag
	 *            是否显示商户红包券 可为空 ---> 0 : 不显示 1:显示
	 * @param listener
	 */
	public void getPublishableTicket(String tranLogId, String cardType,
			String showFlag, final ResultListener listener) {
		if (TextUtils.isEmpty(tranLogId)) {
			listener.onFaild(new Response(1, "参数错误"));
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterTranLogId", tranLogId);
		params.put("cardType", TextUtils.isEmpty(cardType) ? "" : cardType);
		params.put("showFlag", TextUtils.isEmpty(showFlag) ? "1" : showFlag);// 是否显示商户红包券
																			// 0
																			// :
																			// 不显示
																			// 1:显示
		NetRequest.getInstance().addRequest(
				Constants.SC_520_TICKET_DEF_PUBLISHABLE, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) { // 加载可发行的卡券列表
						JSONObject jsonObj = JSONObject.parseObject(response.getResult().toString());
						JSONArray jsonPackets = jsonObj.getJSONArray("ticketPackets");
						JSONArray jarr = jsonObj.getJSONArray("ticketDefs");
						List<TicketPacket> packetList = jsonObj.parseArray(jsonPackets.toJSONString(), TicketPacket.class);
						if (jarr.size() < 1) {
							response.code = -1;
							listener.onSuccess(response);
							return;
						}
						publishableTicketDefs = new ArrayList<TicketDef>();
						for (int i = 0; i < jarr.size(); i++) {
							JSONObject _jobj = jarr.getJSONObject(i);
//							if (_jobj.getLong("createTime")
//									+ _jobj.getLong("validPeriod") * 86400000 > new Date()
//										.getTime()
//									|| _jobj.getString("validPeriod").equals(
//											"-1"))
								//券改造(不需要根据validPeriod过滤券 直接显示服务器返回数据)
								{		
								TicketDef _ticketDef = new TicketDef();
								_ticketDef.setTicketName(_jobj
										.getString("ticketName"));
								// _ticketDef.setActiveAmount(_jobj.getIntValue("activeAmount"));
								_ticketDef.setBalance(_jobj
										.getIntValue("balance"));
								_ticketDef.setId(_jobj.getString("id"));
								// _ticketDef.setMerchantId(_jobj.getString("merchantId"));
								// _ticketDef.setReusedFlag(_jobj.getBoolean("reusedFlag"));
								// _ticketDef.setTicketCode(_jobj.getIntValue("ticketCode")
								// + "");
								_ticketDef.setUsedFlag(_jobj
										.getBoolean("usedFlag"));
								_ticketDef.setValidPeriod(_jobj
										.getIntValue("validPeriod"));
								_ticketDef.setDescription(_jobj
										.getString("description"));
								_ticketDef.setCreateTime(new Date(_jobj
										.getLongValue("createTime")));
								_ticketDef.setTicketType(_jobj.getString("ticketType"));
								_ticketDef.setGift(_jobj.getString("gift"));
								_ticketDef.setHyDiscount(_jobj.getInteger("hyDiscount"));
								_ticketDef.setWxFlag(TextUtils.isEmpty(_jobj
										.getString("wxFlag")) ? "0" : _jobj
										.getString("wxFlag"));
								_ticketDef.setCreateTime(new Date(_jobj.getLongValue("createTime")));
								_ticketDef.setEndTimestamp((_jobj.getLong("endTimestamp")!=null)?_jobj.getLong("endTimestamp"):0L);
								if (_ticketDef.getWxFlag().equals("1")
										&& _jobj.getString("wxCodeId") == null) {
									continue;
								}
								publishableTicketDefs.add(_ticketDef);
							}
						}
						publishableTicketDefs.addAll(packetList);
						response.code = 0;
						response.msg = "success";
						response.result = publishableTicketDefs;
						listener.onSuccess(response);
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(response);
					}
				});
	}

	public void publishTicket(String tranLogId, String cardType, String cardNo,
			int[] countArray, final ResultListener listener) {
		if (publishableTicketDefs == null || publishableTicketDefs.isEmpty()) {
			listener.onFaild(new Response(1, "未添加任何券"));
			return;
		}
		List<TicketPublishBean> ticketInfoList = new ArrayList<TicketPublishBean>();
		List<TicketPublishBean> ticketPacketList = new ArrayList<TicketPublishBean>();
		for (int i = 0; i < countArray.length; i++) {
			int count = countArray[i];
			String checkCode = "";
			int checkCount = 0;
			for (int n = 0; n < count; n++) {
				TicketDef ticketDef = publishableTicketDefs.get(i);
				//TODO 券包--
				if (ticketDef.isPacket()) {
					List<TicketPacketInfo> packetInfo = ticketDef.getPackInfo();
					for (int packetCount = 0; packetCount < packetInfo.size(); packetCount++) {
						TicketPacketInfo pack = packetInfo.get(packetCount);
						if (TextUtils.isEmpty(pack.getTicketDefId())) {
							continue;
						}
						int countInt = pack.getTicketNums();
						for (int countFor = 0; countFor < countInt; countFor++) {
							TicketPublishBean ticketInfo = new TicketPublishBean();
							ticketInfo.setId(pack.getTicketDefId());
							ticketInfo.setType("1");//"1"券包 0 普通券
							ticketInfoList.add(ticketInfo);		//解决组合券无法打印发行的问题 [20151112]
						}
					}
					TicketPublishBean packInfo = new TicketPublishBean();
					packInfo.setId(ticketDef.getId());
					ticketPacketList.add(packInfo);
					continue;
				}
				
				TicketPublishBean ticketPublishBean = createTicketPublishBean(
						cardType, cardNo, ticketDef);

				String getCode = ticketDef.getTicketCode() + "";
				if (!checkCode.equals(getCode)) {
					ticketCount++;
					checkCount = 0;
					checkCode = getCode;
				}
				checkCount++;
				if (ticketCount == 1) {
					kxitem1[0] = ticketDef.getTicketName();
					kxitem1[1] = "￥" + stringAmount(ticketDef.getBalance());
					kxitem1[2] = checkCount + "";
					kxamount += ticketDef.getBalance();
				}
				if (ticketCount == 2) {
					kxitem2[0] = ticketDef.getTicketName();
					kxitem2[1] = "￥" + stringAmount(ticketDef.getBalance());
					kxitem2[2] = checkCount + "";
					kxamount += ticketDef.getBalance();
				}
				if (ticketCount == 3) {
					kxitem3[0] = ticketDef.getTicketName();
					kxitem3[1] = "￥" + stringAmount(ticketDef.getBalance());
					kxitem3[2] = checkCount + "";
					kxamount += ticketDef.getBalance();
				}
				if (ticketCount == 4) {
					kxitem4[0] = ticketDef.getTicketName();
					kxitem4[1] = "￥" + stringAmount(ticketDef.getBalance());
					kxitem4[2] = checkCount + "";
					kxamount += ticketDef.getBalance();
				}

				ticketInfoList.add(ticketPublishBean);
			}
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("masterTranLogId", tranLogId);
		params.put("cardType", cardType);
		params.put("cardNo", cardNo);
		params.put("ticketInfoList", ticketInfoList);
		params.put("ticketPacketList", ticketPacketList);

		NetRequest.getInstance().addRequest(Constants.SC_501_TICKET_PUBLISH,
				params, new ResponseListener() {

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

	private TicketPublishBean createTicketPublishBean(String cardType,
			String cardNo, TicketDef ticketDef) {
		TicketPublishBean ticketPublishBean = new TicketPublishBean();
		ticketPublishBean.setId(ticketDef.getId());
		// ticketPublishBean.setActiveAmount(ticketDef.getActiveAmount());
		ticketPublishBean.setBalance(ticketDef.getBalance());
		ticketPublishBean.setCardNo(cardNo);
		ticketPublishBean.setCardType(Integer.parseInt(cardType));
		ticketPublishBean.setMid(AppConfigHelper
				.getConfig(AppConfigDef.mid));
		ticketPublishBean.setMerchantId(AppConfigHelper
				.getConfig(AppConfigDef.merchantId));
		ticketPublishBean.setPayId(Integer.parseInt(AppConfigHelper
				.getConfig(AppConfigDef.pay_id)));
		// ticketPublishBean.setReusedFlag(ticketDef.getReusedFlag());
		ticketPublishBean.setTicketCode(ticketDef.getTicketCode());
		ticketPublishBean.setTicketDefId(ticketDef.getId());
		ticketPublishBean.setTicketName(ticketDef.getTicketName());
		// ticketPublishBean.setUsedFlag(ticketDef.getUsedFlag());
		ticketPublishBean.setValidPeriod(ticketDef.getValidPeriod());
		ticketPublishBean.setValidFlag(true);
		ticketPublishBean.setDescription(ticketDef.getDescription());
		return ticketPublishBean;
	}

	public String stringAmount(int amount) {
		return String.format("%.2f", 0.01 * amount);
	}
	
	/**
	 * 设置待发现的券列表
	 * @param publishableTicketDefs
	 */
	public void setPublishableTicketDefs(List<TicketDef> publishableTicketDefs) {
		this.publishableTicketDefs = publishableTicketDefs;
	}
}
