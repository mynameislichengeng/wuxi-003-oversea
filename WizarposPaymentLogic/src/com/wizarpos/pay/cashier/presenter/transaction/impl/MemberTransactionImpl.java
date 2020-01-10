package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

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
import com.wizarpos.pay.cashier.model.MemberCardInfoBean;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.pay_tems.wepay.WepayQRCodeRequest;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.payment.impl.MemberPayment;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.TicketOperaterFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.cashier.presenter.ticket.inf.MemberTicketManager;
import com.wizarpos.pay.cashier.presenter.ticket.inf.WepayTicketOperater;
import com.wizarpos.pay.cashier.presenter.transaction.inf.MemberTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.LastPrintHelper;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.DisplayHelper;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicektBean;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicketActivity;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.TokenGenerater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;

/**
 * 银行卡/商户卡支付
 */
public class MemberTransactionImpl extends TransactionImpl implements
		MemberTransaction {

	private MemberTransactionListener memberTranscationListener;

	private MemberCardManager memberCard;// 会员卡
	private MemberPayment memberPayment; // 会员卡支付
	private CommonTicketManager commonTicketManager;// 普通券管理
	private MemberTicketManager memberTicketManager;// 会员券管理
	private WepayTicketOperater wepayTicketOperater;// 微信券管理
	
	// private String cardNo = null; // 会员卡号
	// private Long cid;

	private JSONObject jTranLog = null;
	
	String _publishTickets;

	private TicketInfo wemengTicketInfo;
	private String token;
	/** 添加券返回结果start*/
	//券为空
	protected final int RESPONSE_TICKET_NULL = -1;
	//添加成功
	protected final int RESPONSE_TICKET_SUCCESS = 0;
	//已经有券
	protected final int RESPONSE_TICKET_CONTAIN = 1;
	//不同类型的券不能添加
	protected final int RESPONSE_TICKET_DIFF_TYPE = -2;
	/** 添加券返回结果end*/
	
	/** 券实际总金额*/
	private int ticketTotalAmount;
	
	//券改造 验证券
	protected ArrayList<TicketInfo> addTicketList = new ArrayList<TicketInfo>();
	
	private boolean isHandled_submit;
	public MemberTransactionImpl(Context context) {
		super(context);
		this.context = context;
		this.memberTranscationListener = (MemberTransactionListener) context;
		this.memberCard = new MemberCardManager();
		this.memberPayment = new MemberPayment();
		this.commonTicketManager = TicketManagerFactory
				.createCommonTicketManager(context);
		this.memberTicketManager = TicketManagerFactory
				.createMemberTicketManager(context);
		this.wepayTicketOperater = TicketOperaterFactory
				.createWepayTicketOperater();
		token = TokenGenerater.newToken(); //生成 token@hong[20151217];
	}

	public void handleIntent(Intent intent) {
		super.handleIntent(intent);

		cardNo = intent.getStringExtra("cardNo"); 
		addTicketList = (ArrayList<TicketInfo>) intent.getSerializableExtra("addTicketList");
		String tokenId = intent.getStringExtra("tokenId");
		// transactionType =
		// TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD;
		// 刷卡页面传入
		if (!TextUtils.isEmpty(tokenId)) {
			Logger2.debug("初始化微信会员卡");
			cardType = "99";// 微信会员卡
			if (isMixTransaction() == false) {
				defTransactionType(TransactionTemsController.TRANSACTION_TYPE_WEPAY_MEMBER_CARD); // 微信会员卡
			}
			initWepayMemberCard(tokenId);
		} else if (!TextUtils.isEmpty(cardNo)) {
			Logger2.debug("初始化实体会员卡");
			cardType = "1";// 实体会员卡
			if (isMixTransaction() == false) {
				defTransactionType(TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD);// 实体会员卡
			}
			initWizarMemberCard(cardNo);
		} else {
			memberTranscationListener.onInit(new Response(1, "初始化失败"));
		}

	}

	/**
	 * 初始化微信会员卡信息
	 */
	private void initWepayMemberCard(String tokenId) {
		memberCard.getWepayCardNo(tokenId, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				Logger2.debug("初始化微信会员卡成功");
				cardNo = response.result.toString();// cardNo = UDID
				initWizarMemberCard(cardNo);
			}

			@Override
			public void onFaild(Response response) {
				memberTranscationListener.onInit(response);
			}
		});
	}

	/**
	 * 初始化普通会员卡信息
	 */
	private void initWizarMemberCard(final String cardNo) {
		memberCard.initMemberCardInfo(cardNo, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				Logger2.debug("获取会员卡信息成功");
				MemberCardInfoBean cardInfo = (MemberCardInfoBean) response.result;
				try {
					memberPayment.setMemberCardInfo(cardInfo);
					LogEx.d("MemberCard", "为payment设置cardinfo");
				} catch (Exception e) {
					e.printStackTrace();
					LogEx.d("MemberCard", "交易状态异常:重复的cardInfo");
					memberTranscationListener.onInit(new Response(1,
							"交易状态异常:重复的cardInfo"));
					return;
				}
				// 获取该会员卡下的所有会员券
				memberTicketManager.getMemberTickets(cardNo,
						cardInfo.getCardType(), new ResultListener() {
							@Override
							public void onSuccess(Response response) {
								LogEx.d("MemberCard", "获取券列表成功");
								memberTranscationListener.onInit(response);
							}

							@Override
							public void onFaild(Response response) {
								response.setCode(0);// 没有会员券的时候，仍然返回成功
								response.msg = "没有会员券";
								response.result = null;
								memberTranscationListener.onInit(response);
							}
						});
			}

			@Override
			public void onFaild(Response response) {
				memberTranscationListener.onInit(response);
			}
		});
	}

	/**
	 * 添加卡券按钮 / 券扫描
	 * 
	 * @param ticketNo
	 *            券唯一标识
	 */
	public void getCommonTicketInfo(String ticketNo,
			final ResultListener listener) {
		if (commonTicketManager.isAddedTicket(ticketNo)) {
			Toast.makeText(context, "已使用该券，不能重复使用！", Toast.LENGTH_SHORT).show();
			return;
		}
		commonTicketManager.getTicketInfo(ticketNo, initAmount, shouldAmount, new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
				if (commonTicketInfoResp.getWemengTicket() != null) {
					wemengTicketInfo = commonTicketInfoResp
							.getWemengTicket();
				}
				listener.onSuccess(new Response(0, "获取券信息成功",
						commonTicketInfoResp.getTicketInfo()));
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 添加普通券按钮
	 */
	public Response addCommonTicket(TicketInfo ticket, boolean isFromScan) {
//		int ticketAmount = ticket.getTicketDef().getBalance();
//		if (Calculater.compare(shouldAmount, ticketAmount + "") < 0) {
//			return new Response(1, "券金额大于支付金额，无法用券！");
//		}
		if (commonTicketManager.isAddedTicket(ticket.getId())) {
			return new Response(1, "已近添加该券,无法重复使用");
		}
		Response mResponse = checkTicketType(ticket);
		if(mResponse.code != RESPONSE_TICKET_SUCCESS) {
			return mResponse;
		}
		Response response = commonTicketManager.addTicket(ticket, isFromScan);
		if(response.code == 1){
			return response;
		}
		int blance = ticket.getTicketDef().getBalance();
		LogEx.d("TICKET", blance+"");
		reduceAmount = Calculater.plus(reduceAmount, blance+""); 
		if(Calculater.compare(blance+"", shouldAmount) > 0){//更新扣减金额 wu
			shouldAmount = "0";
		}else{
			shouldAmount = Calculater.subtract(shouldAmount, blance+"");
		}
		changeAmount = Calculater.subtract(realAmount, shouldAmount);
//		calculateAddTicket(ticketAmount);
		return new Response(0, "添加券成功");
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-28 上午9:50:10 
	 * @param ticketInfo
	 * @return 
	 * @Description:验证券是否能添加 (折扣券只能用一张。折扣券和代金券不能同时使用)
	 */
	private Response checkTicketType(TicketInfo ticketInfo) {
		List<TicketInfo> commonList = commonTicketManager.getAddedTickets();
		List<TicketInfo> memberList = memberTicketManager.getAddedTickets();
		List<TicketInfo> allList = new ArrayList<TicketInfo>();
		allList.addAll(commonList);
		allList.addAll(memberList);
		Response response = new Response();
		if(ticketInfo == null)
		{
			response.code = RESPONSE_TICKET_NULL;
			response.msg = "添加券";
			return response;
		}
		if(allList.contains(ticketInfo))
		{
			response.code = RESPONSE_TICKET_CONTAIN;
			response.msg = "已使用该券，不能重复添加";
			return response;
		}
		String ticketType = ticketInfo.getTicketDef().getTicketType();
		if(ticketType.equals(TicketDef.TICKET_TYPE_DISCOUNT))
		{//如果是折扣券
			if(allList.size()>0)
			{
				response.code = RESPONSE_TICKET_DIFF_TYPE;
				response.msg = "折扣券只能单独使用";
				return response;
			}
		}else
		{//如果是代金券
			for(TicketInfo ticketBean:allList)
			{
				if(ticketBean.getTicketDef().getTicketType().equals(TicketDef.TICKET_TYPE_DISCOUNT))
				{
					response.code = RESPONSE_TICKET_DIFF_TYPE;
					response.msg = "类型不同的券不能同时使用";
					return response;
				}
			}
		}
		response.code = RESPONSE_TICKET_SUCCESS;
		response.msg = "添加成功";
		return response;
	}

	/**
	 * 获取微信券信息
	 * 
	 * @param wepayCode
	 *            微信卡的二维码对应的字符串
	 */
	public void getWepayTicketInfo(final String wepayCode,
			final ResultListener listener) {
		if (TextUtils.isEmpty(wepayCode)) {
			listener.onFaild(new Response(1, "获取券信息失败:券号为空"));
			return;
		}
		if (wepayTicketOperater.isAddWepayTicket(wepayCode,
				memberTicketManager.getAddedTickets())) {// 判断当前已添加的会员卡列表中是否含有这张微信券
			listener.onFaild(new Response(1, "该微信券已扫，不能重复使用！"));
			return;
		}
		commonTicketManager.getTicketInfo(wepayCode, initAmount, shouldAmount, new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
				if (commonTicketInfoResp.getWemengTicket() != null) {
					wemengTicketInfo = commonTicketInfoResp
							.getWemengTicket();
				}
				listener.onSuccess(new Response(0, "获取券信息成功",
						commonTicketInfoResp.getTicketInfo()));
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
//		wepayTicketOperater.getTicketDetial(wepayCode, shouldAmount, new ResultListener() {
//			@Override
//			public void onSuccess(Response response) {
//				
//				TicketInfo getedTikcetInfo = (TicketInfo) response.result;
//				if (getedTikcetInfo != null) {
//					// 从会员券列表中取出相同种类的券,如果有多张这种券,默认取第一张(不太合理,后期应该修改这个逻辑),将wx_code赋予这个券
//					TicketInfo _tempTicket = wepayTicketOperater
//							.getTicketByTicketDef(getedTikcetInfo.getId(),
//									memberCard.getAllTickets(),
//									memberTicketManager.getAddedTickets());
//					if (_tempTicket != null) {
//						_tempTicket.setWx_code(getedTikcetInfo.getWx_code());// 将wxcode,cardId缓存至新的ticket
//						// 对象,在调用核销微信券接口时需要用到这两个字段
//						_tempTicket.setCardId(getedTikcetInfo.getCardId());
//						listener.onSuccess(new Response(0, "success",
//								_tempTicket));
//					} else {
//						listener.onFaild(new Response(1, "该会员没有这张券,无法使用"));
//					}
//				} else {
//					listener.onFaild(new Response(1, "该会员没有这张券,无法使用"));
//				}
//			}
//
//			@Override
//			public void onFaild(Response response) {
//				listener.onFaild(response);
//			}
//		});
	}

	/**
	 * 添加微信券
	 * 
	 * @param ticket
	 *            券实体
	 * @param isFromScan
	 *            是否扫码添加
	 * @param listener
	 *            回调
	 */
	public void addWepayTicket(TicketInfo ticket, boolean isFromScan,
			final ResultListener listener) {
//		int ticketAmount = ticket.getTicketDef().getBalance();
//		if (Calculater.compare(shouldAmount, ticketAmount + "") < 0) {
//			listener.onFaild(new Response(1, "券金额大于支付金额，无法用券！"));
//			return;
//		}
		// 验证该会员是否有这张券
//		if (!wepayTicketOperater.isContainsTicketDef(ticket.getTicketDef()
//				.getId(), memberCard.getAllTickets())) {
//			listener.onFaild(new Response(1, "该会员没有对应的微信券！"));
//			return;
//		}
		if (wepayTicketOperater.isAddWepayTicket(ticket.getWx_code(),
				memberTicketManager.getAddedTickets())) {
			listener.onFaild(new Response(1, "已经添加了这张券！无法重复添加"));
			return;
		}
		Response response = commonTicketManager.addTicket(ticket, isFromScan);
		if(response.code == 1){
			listener.onFaild(response);
			return;
		}
		int blance = ticket.getTicketDef().getBalance();
		LogEx.d("TICKET", blance+"");
		reduceAmount = Calculater.plus(reduceAmount, blance+""); 
		if(Calculater.compare(blance+"", shouldAmount) > 0){//更新扣减金额 wu
			shouldAmount = "0";
		}else{
			shouldAmount = Calculater.subtract(shouldAmount, blance+"");
		}
		listener.onSuccess(new Response(0, "添加成功"));
	}

	/**
	 * 添加会员券
	 * 
	 * @param ticket
	 *            券实体
	 */
	public Response addMemberTicket(TicketInfo ticket, boolean isFromScan) {
//		int ticketAmount = ticket.getTicketDef().getBalance();
//		if (Calculater.compare(shouldAmount, ticketAmount + "") < 0) {
//			return new Response(1, "券金额大于支付金额，无法用券！");
//		}
		if (memberTicketManager.isAddedTicket(ticket.getId())) {
			return new Response(1, "已近添加该券,无法重复使用");
		}
		Response response = memberTicketManager.addTicket(ticket, isFromScan);
		if(response.code == 1){
			return response;
		}
		int blance = ticket.getTicketDef().getBalance();
		LogEx.d("TICKET", blance+"");
		reduceAmount = Calculater.plus(reduceAmount, blance+""); 
		if(Calculater.compare(blance+"", shouldAmount) > 0){//更新扣减金额 wu
			shouldAmount = "0";
		}else{
			shouldAmount = Calculater.subtract(shouldAmount, blance+"");
		}
//		calculateAddTicket(ticketAmount);
		return new Response(0, "添加券成功");
	}

	/**
	 * 删除会员券
	 * 
	 * @param ticket
	 *            券实体
	 */
	public boolean removeMemberTicket(TicketInfo ticket) {
		if (!memberTicketManager.removeAddedTicket(ticket.getId())) {
			return false;
		}
		memberTicketManager.removeAddedTicket(ticket.getId());
		int ticketAmount = ticket.getTicketDef().getBalance();
		calulateRemoveTicket(ticketAmount);
		return true;
	}

	/**
	 * 清空所有已添加的会员券
	 */
	public void removeAllMemberTciket() {
		if(memberTicketManager.getAddedTickets() == null){
			return;
		}
		List<TicketInfo> removeTemps = new ArrayList<TicketInfo>();
		for (TicketInfo info : memberTicketManager.getAddedTickets()) {
			removeTemps.add(info);
//			removeMemberTicket(info);//除bug 遍历时操作了list  @yaosong [20151204]
		}
		for (int i = 0; i < removeTemps.size(); i++) {
			removeMemberTicket(removeTemps.get(i));
		}
	}

	// 使用卡券
	private void uploadTrans(final ResultListener listener) {
		String reduceStr = Calculater.subtract(initAmount, shouldAmount);
		
		List<String> ticketIds = new ArrayList<String>();
		for (TicketInfo ticket : memberTicketManager.getAddedTickets()) {// 统计会员券
			String idStr ;
			if ("true".equals(ticket.getIsFromScan())) {
				idStr = ticket.getId() + "|1";// 如果是扫码的券,值未1,否则为0
			} else {
				idStr = ticket.getId() + "|0";
			}

			Integer balance = ticket.getTicketDef().getBalance();
			ticketTotalAmount += balance;
			if(Calculater.compare(reduceStr, balance+"") > 0){ //计算券的实际抵消金额 wu
				idStr += ("|" + balance);
				ticketReduceAomount += balance;
				reduceStr = Calculater.subtract(reduceStr, balance+"");
			}else{
				idStr += ("|" + reduceStr);
				ticketReduceAomount += Integer.valueOf(reduceStr);
				reduceStr = "0";
			}
			
			ticketIds.add(idStr);
		}
		for (TicketInfo ticket : commonTicketManager.getAddedTickets()) {// 统计非会员券
			String idStr ;
			if ("true".equals(ticket.getIsFromScan())) {
				idStr = ticket.getId() + "|1";// 如果是扫码的券,值未1,否则为0
			} else {
				idStr = ticket.getId() + "|0";
			}
			
			Integer balance = ticket.getTicketDef().getBalance();
			ticketTotalAmount += balance;
			if(Calculater.compare(reduceStr, balance+"") > 0){ //计算券的实际抵消金额 wu
				idStr += ("|" + balance);
				ticketReduceAomount += balance;
				reduceStr = Calculater.subtract(reduceStr, balance+"");
			}else{
				idStr += ("|" + reduceStr);
				ticketReduceAomount += Integer.valueOf(reduceStr);
				reduceStr = "0";
			}
			
			ticketIds.add(idStr);
		}

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cardType", cardType);
			if(!TextUtils.isEmpty(saleInputAmount)&&!saleInputAmount.equals("0")){ 
				params.put("inputAmount", saleInputAmount);		//当有销售金额传入时,使用 saleInputAmout wu@[20151120]	
			}else{
				if(isMixTransaction()){ //区分组合支付和普通支付
					params.put("inputAmount", mixInitAmount);
				}else{
					params.put("inputAmount", TextUtils.isEmpty(initAmount) ? "0": initAmount);
				}			
			}
//			params.put("payAmount", TextUtils.isEmpty(realAmount) ? "0"
//					: realAmount);
			String payAmount = Calculater.compare(realAmount, shouldAmount) > 0 ? shouldAmount : realAmount;
			params.put("payAmount", TextUtils.isEmpty(payAmount) ? "0"
			: payAmount);//传入应付金额 wu
			params.put("issueTicketMode", isAutoPublishTicket);// 卡发行模式
			params.put("id", AppConfigHelper.getConfig(AppConfigDef.mid)
					+ cardType + cardNo);
			params.put("cardName", ""); // 不处理
			params.put("token", token); //增加 token 字段 wu @[20151217]
			params.put("cardNo", cardNo);
			if(isMixTransaction()){ //增加传入混合支付的总金额 wu
				params.put("masterPayAmount", mixInitAmount);//组合支付总金额
			}
			if(PaymentApplication.getInstance().isWemengMerchant()){
				params.put("amount", initAmount);//为微盟券增加 wu
			}
			if (commonTicketManager.isContainsWemengTicket()) {
				params.put("ticketNo", commonTicketManager.getAddedWemengTicketNo());
			} else {
				params.put("ids", ticketIds);
			}
			if (isMixTransaction()) {
				params.put("masterTranLogId", mixTranLogId);
				params.put("mixFlag", 1);
				params.put("isPayComingForm", isPayComingForm);
			}
			NetRequest.getInstance().addRequest(
					com.wizarpos.pay.common.Constants.SC_304_MERCHANT_CARD_PAY,
					params, new ResponseListener() {

						@Override
						public void onSuccess(Response response) {
							if (isMixTransaction()) {
								String jsonString = JSON.toJSONString(
										response.getResult(),
										SerializerFeature.DisableCircularReferenceDetect);
								JSONObject jsonObject = JSON
										.parseObject(jsonString);
								jTranLog = jsonObject.getJSONObject(
										"slaveTranLog")
										.getJSONObject("tranLog");// 子流水
								mixTranLogId = jTranLog
										.getString("masterTranLogId");// 混合支付主流水号
								tranLogId = jTranLog.getString("id");// 混合支付子流水号
								//防止订单重复提交
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
				
								response.code = 0;
								response.msg = "success";
								response.result = bundleResult();
								listener.onSuccess(response);
								print();
							} else {
								JSONObject _jsonObject = (JSONObject) response
										.getResult();
								jTranLog = _jsonObject.getJSONObject("tranLog");
								_publishTickets = _jsonObject.getString("publishTickets");
								String ticketId = _jsonObject
										.getString("ticket");
								String ftranAmount = jTranLog
										.getString("ftranAmount");
								tranLogId = jTranLog
										.getString("masterTranLogId");
								//防止订单重复提交
								LogEx.d("jTranLog", jTranLog.toString());
								String tokenReturn = jTranLog.getString("token");
								boolean doubleClickFlag = _jsonObject.getBoolean("doubleClickFlag");
								LogEx.d("token", "current token: " + token );
								LogEx.d("token", "return token: " + tokenReturn + " - doubleClickFlag: " + doubleClickFlag);
								if(!token.equals(tokenReturn)){ //增加 toke 验证逻辑
									return;
								}
								if(doubleClickFlag && isHandled_submit){
									return;
								}else{
									isHandled_submit = true;
									LogEx.d("token", "isHandled_submit: "+ isHandled_submit);
								}	
								// ---------------微盟券 wu----------------------
								try {
									if (wemengTicketInfo == null
											&& _jsonObject
													.containsKey("giftTicket")) {
										if (((JSONObject) _jsonObject
												.get("giftTicket")).isEmpty() == false) {
											wemengTicketInfo = Tools
													.jsonObjectToJavaBean(
															(JSONObject) _jsonObject
																	.get("giftTicket"),
															TicketInfo.class);
											wemengTicketInfo
													.setIsWeiMengTicket("1");// 标示是微盟券
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								// ---------------微盟券 wu----------------------

								String balance = Tools.formatFen(Long
										.parseLong(jTranLog
												.getString("balance")));
								DisplayHelper.getInstance().startKxMemberCard(
										cardNo, jTranLog.getString("tranMark"),
										ftranAmount, balance);
								if (!TextUtils.isEmpty(ticketId)) {
									WepayQRCodeRequest request = new WepayQRCodeRequest();// 请求微信会员卡二维码
									request.setTicketId(ticketId);
									request.setListener(new ResponseListener() {
										@Override
										public void onSuccess(Response response) {
											if (DeviceManager.getInstance()
													.isSupprotPrint()) {
												Q1PrintBuilder builder = new Q1PrintBuilder();
												PrintServiceControllerProxy controller = new PrintServiceControllerProxy(
														context);
												String printString = builder.center(builder
														.bold("扫一扫开通微信会员卡"));
												printString += builder.branch();
												controller.print(printString);
												Bitmap bm = BitmapFactory.decodeFile(response.getResult()
														.toString());
//												controller
//														.printBitmap(response
//																.getResult()
//																.toString());
												controller.print(Tools.resizeBitmap(bm,300, 300));
											} else {
												List<DisplayTicektBean> displayTicektBeans = new ArrayList<DisplayTicektBean>();

												DisplayTicektBean bean = new DisplayTicektBean();
												bean.setTitle("扫一扫开通微信会员卡");

												bean.setBitmapPath(response
														.getResult().toString());

												displayTicektBeans.add(bean);
												if (displayTicektBeans
														.isEmpty() == false) {
													Intent intent = new Intent(
															context,
															DisplayTicketActivity.class);
													intent.putExtra(
															DisplayTicketActivity.DISPLAY_TICKET,
															(Serializable) displayTicektBeans);
													context.startActivity(intent);
												}
											}

											printTransResult(listener);
										}

										@Override
										public void onFaild(Response response) {
											printTransResult(listener);
										}
									});
									request.start();
								} else {
									printTransResult(listener);
								}
							}

						}

						@Override
						public void onFaild(Response response) {
							if (isMixTransaction()) {
								try {
									String backMasterTranLogId = (String) response
											.getResult();
									if (TextUtils.isEmpty(mixTranLogId)
											&& TextUtils
													.isEmpty(backMasterTranLogId)) {
										LogEx.d("mixMember",
												"混合支付第一笔会员卡消费失败,准备删除主单记录");
										Map<String, Object> m = new HashMap<String, Object>();
										m.put("masterTranLogId",
												backMasterTranLogId);
										cancelMixMemberPay(backMasterTranLogId,
												listener);
									}
								} catch (Exception e) {
									e.printStackTrace();
									listener.onFaild(response);
								}
							} else {
								listener.onFaild(response);
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printTransResult(ResultListener listener) {
		if (isNeedPrint == false) {
			//@hong 第三方没有打印的情况下，没有回调结果20151211
			listener.onSuccess(new Response(0, "success", bundleResult()));
			return;
		}
		// 打印
		if (jTranLog.getString("tranPoints") != null
				&& !jTranLog.getString("tranPoints").equals("0")) {
			giftPoints = jTranLog.getString("tranPoints");
		}
		if (isNeedPrintCommonTikcet) {
			print();
			if (!TextUtils.isEmpty(_publishTickets)) {
				List<TicketInfo> ticketInfos = JSONArray.parseArray(_publishTickets, TicketInfo.class);
				commonTicketManager.printTickets(ticketInfos);
			}
		}
		if (isNeedPrintWepayTicket) {
			printWepayTickets();
		}
		
		
		listener.onSuccess(new Response(0, "success", bundleResult()));
	}

	private void print() {
		if (isNeedPrint == false) {
			return;
		}
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = "";
		if (isMixTransaction()) {
			printString += builder.center(builder.bold("组合支付|会员卡消费"));
		} else {
			printString += builder.center(builder.bold("会员卡消费"));
		}
		printString += builder.branch();
		printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid)
				+ builder.br();
		printString += "商户名称："
				+ AppConfigHelper.getConfig(AppConfigDef.merchantName)
				+ builder.br();
		printString += "终端号："
				+ AppConfigHelper.getConfig(AppConfigDef.terminalId)
				+ builder.br();
		printString += "流水号：" + jTranLog.getString("id") + builder.br();
		printString += "卡号："
				+ (cardNo.length() > 10 ? Tools.replace(cardNo, '*', 6,
						cardNo.length() - 10) : cardNo) + builder.br();
		if (isMixTransaction()) {
			printString += "总金额:" + Calculater.divide100(mixInitAmount)
					+ builder.br();
		}
		printString += "收银：" + Calculater.divide100(initAmount) + "元"
				+ builder.br();
		if (!TextUtils.isEmpty(discountAmount) && !"0".equals(discountAmount)) {
			printString += "折扣减价：" + Calculater.divide100(discountAmount) + "元"
					+ builder.br();
		}
		String printReduceAmount = Calculater.subtract(reduceAmount + "", ticketTotalAmount + "");
		printString += "扣减：" + Calculater.divide100(printReduceAmount) + "元"
				+ builder.br();
		printString += "券总计：" + Calculater.divide100(ticketReduceAomount + "") + "元"
				+ builder.br();
//		printStringPayFor += "扣减：" + Calculater.divide100(reduceAmount) + "元"
//				+ builder.br();
		printString += "应收：" + Calculater.divide100(shouldAmount) + "元"
				+ builder.br();
		// printStringPayFor += "实收：" + Calculater.divide100(realAmount) + "元" +
		// pb.br();
		printString += "刷卡：" + Calculater.divide100(realAmount) + "元"
				+ builder.br();
		// printStringPayFor += "找零：" + Calculater.divide100(changeAmount) + "元" +
		// pb.br();
		printString += "余额："
				+ Tools.formatFen(jTranLog.getLongValue("balance")) + "元"
				+ builder.br();
		if (giftPoints != null) {
			printString += "赠送积分：" + giftPoints + builder.br();
		}
		printString += "时间："
				+ DateUtil.format(jTranLog.getLongValue("tranTime"),
						DateUtil.P2) + builder.br();
		printString += builder.branch();
		Logger2.debug(printString);
		// controller.print(printStringPayFor);
		printString += memberTicketManager.getMemberPrintInfo();
		printString += commonTicketManager.getCommonTicketPrintInfo();
		controller.print(printString);
		controller.print(builder.endPrint());
		controller.cutPaper();
		LastPrintHelper.beginTransaction().addString(printString).commit();
	}

	/**
	 * 打印微信券
	 */
	private void printWepayTickets() {
		if (isNeedPrint == false) {
			return;
		}
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String printString = wepayTicketOperater
				.getWepayPrintInfo(commonTicketManager.getAddedTickets());
		controller.print(printString);
		controller.print(builder.endPrint());
		controller.cutPaper();
	}

	/**
	 * 交易
	 */
	public void trans(final ResultListener listener) {
		// offlineInit = true;
//		shouldAmount = Calculater.subtract(initAmount, discountAmount);
//		shouldAmount = Calculater.subtract(initAmount, reduceAmount);// [@hong 折扣金额bug修改] //不再重复计算 wu@20150921
//		setRealAmount(shouldAmount);
//		if (TextUtils.isEmpty(realAmount) //允许交易为0时上送数据
//				|| Calculater.compare(realAmount, "1") < 0) {
//			listener.onFaild(new Response(1, "交易金额不能为0"));
//			return;
//		}
		this.realAmount = shouldAmount; //会员卡消费 : realAmount = shouldAmount
		changeAmount = Calculater.subtract(realAmount, shouldAmount);// 计算找零金额
		
		Response response = memberPayment.pay(realAmount);
		if (response.code != 0) {
			listener.onFaild(response);
			return;
		}
//		changeAmount = Calculater.subtract(realAmount, shouldAmount);// 计算找零金额

		transTime = DateUtil.format(new Date(), DateUtil.P2);
		// 目前的逻辑是先上送交易，然后核销微信券。如果交易上送成功但微信券核销失败，会造成用户微信卡包中多出一张微信券 bug.
		// 微信卡券直接在后台核销 wu@[20150914]
//		uploadTrans(new ResultListener() {
//			@Override
//			public void onSuccess(Response response) {
//				final Response _reResponse = response;
//				wepayTicketOperater.passWepay(
//						filterWepayTickets(memberTicketManager
//								.getAddedTickets()), new ResultListener() {
//
//							@Override
//							public void onSuccess(Response response) {
//								listener.onSuccess(_reResponse);
//							}
//
//							@Override
//							public void onFaild(Response response) {
//								listener.onFaild(response);
//							}
//						});
//			}
//
//			@Override
//			public void onFaild(Response response) {
//				listener.onFaild(response);
//			}
//		});
		uploadTrans(listener);
	}

	@Override
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

	/**
	 * 获取已添加的会员券
	 * 
	 * @return 已添加会员券列表
	 */
	public List<TicketInfo> getAddedMemberTickets() {
		return memberTicketManager.getAddedTickets();
	}

	/**
	 * 获取所有会员券
	 * 
	 * @return 所有会员券列表
	 */
	public List<TicketInfo> getAllMemberTickets() {
		return memberCard.getAllTickets();
	}

	/**
	 * 筛选微信券
	 * 
	 * @param tickets
	 * @return
	 */
	private List<TicketInfo> filterWepayTickets(List<TicketInfo> tickets) {
		List<TicketInfo> wepayTickets = new ArrayList<TicketInfo>();
		for (TicketInfo ticket : tickets) {
			if ("1".equals(ticket.getTicketDef().getWxFlag())) {
				wepayTickets.add(ticket);
			}
		}
		return wepayTickets;
	}

	/**
	 * 撤销混合支付会员消费第一笔
	 * 
	 * @param tranLogId
	 * @param listener
	 */
	private void cancelMixMemberPay(String tranLogId,
			final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterTranLogId", tranLogId);
		NetRequest.getInstance().addRequest(
				com.wizarpos.pay.common.Constants.SC_805_CANCEL_MIX_TRANLOG,
				params, new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						LogEx.d("mixMember", "混合支付第一笔会员卡消费失败,删除主单记录成功");
						listener.onFaild(new Response(1, "交易失败"));
					}

					@Override
					public void onFaild(Response response) {
						LogEx.d("mixMember", "混合支付第一笔会员卡消费失败,删除主单记录失败");
						listener.onFaild(response);
					}
				});
	}

	@Override
	public MemberCardInfoBean getMemeberCardInfo() {
		return memberPayment.getCardInfo();
	}

	@Override
	public void passMixMemberTickets(final List<TicketInfo> infos,
			final ResultListener listener) {
		if (infos == null || infos.isEmpty()) {
			listener.onFaild(new Response(1, "没有需要核销的券"));
			return;
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("masterTranLogId", mixTranLogId);
		m.put("mixFlag", 1);
		m.put("masterPayAmount", mixInitAmount);
		m.put("inputAmount", mixInitAmount);
		m.put("isPayComingForm", isPayComingForm);
		List<String> ids = new ArrayList<String>();
		String reduceStr = initAmount;
		for (TicketInfo info : infos) {
			String idStr = info.getId() + "|1";
			Integer balance = info.getTicketDef().getBalance();
			if (Calculater.compare(reduceStr, balance + "") > 0){
				idStr += ("|" + balance);
				reduceStr = Calculater.subtract(reduceStr, balance+"");
			}else{
				idStr += ("|" + reduceStr);
				reduceStr = "0";
			}
			ids.add(idStr);
//			realAmount = Calculater.plus(realAmount, balance + "");
		}
//		realAmount = shouldAmount = initAmount;
		if(Calculater.compare(reduceStr, "0") > 0){
			realAmount = Calculater.subtract(initAmount, reduceStr);
		}else{
			realAmount = initAmount;
		}
		LogEx.d("组合支付会员券核销", "金额:" + realAmount);
		m.put("ids", ids);
		m.put("cardNo", cardNo);
		NetRequest.getInstance().addRequest(Constants.SC_792_MIX_PAY_MEMTICKET,
				m, new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						LogEx.d("混合支付主流水号", "现金返回:" + mixTranLogId);
						String jsonString = JSON.toJSONString(
								response.getResult(),
								SerializerFeature.DisableCircularReferenceDetect);
						JSONObject jsonObject = JSON.parseObject(jsonString);
						jTranLog = jsonObject.getJSONObject("masterTranLog");// [@hong bundleResult[中bug]]
						JSONArray printUsedTickets = jsonObject.getJSONArray("slaveTranLog");
						printMixMemberTicket(printUsedTickets);
						JSONObject object = jsonObject.getJSONObject("masterTranLog");
						tranLogId = object.getString("id");// 子流水
						mixTranLogId = object.getString("masterTranLogId");// 混合支付主流水号
						addTicketList.addAll(infos);//券改造
						Intent intent = bundleResult();
						intent.putExtra(
								Constants.TRANSACTION_TYPE,
								TransactionTemsController.TRANSACTION_TYPE_MEMBER_TICKET_CANCEL);
						listener.onSuccess(new Response(0, "success", intent));
					}

					@Override
					public void onFaild(Response arg0) {
						listener.onFaild(arg0);
					}
				});
	}

	private void printMixMemberTicket(JSONArray printUsedTickets){
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		String mid = AppConfigHelper.getConfig(AppConfigDef.mid);
		String printString = "";
		int costAmount = 0;
		printString += builder.center(builder.bold("会员券|组合支付"));
		printString += builder.normal("--------------------------------") + builder.br();
		printString += "慧商户号：" + mid + builder.br();
		printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName) + builder.br();
		printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId) + builder.br();
		printString += "卡号：" + (cardNo.length() > 10 ? Tools.replace(cardNo, '*', 6, cardNo.length() - 10) : cardNo) + builder.br();
		printString += "时间：" + DateUtil.format(jTranLog.getLongValue("tranTime"), DateUtil.P2) + builder.br();
		printString += builder.branch();
		printString += "流水号" + builder.tab("") + builder.tab("") + builder.tab("金额(元)  ") + builder.tab("数量") + builder.br();
		for (int i = 0; i < printUsedTickets.size(); i++) {
			JSONObject ticketPrintObj = printUsedTickets.getJSONObject(i);
			printString += builder.normal(Tools.deleteMidTranLog(ticketPrintObj.getString("id"), mid)) + builder.tab("") + builder.tab("") + builder.tab("") + builder.br();
			printString += builder.normal("") + builder.tab("") + builder.tab("") + builder.tab(Tools.formatFen(ticketPrintObj.getIntValue("tranAmount"))) + builder.tab(" ")
					+ builder.tab(" ") + builder.tab("1") + builder.br();
			costAmount += ticketPrintObj.getIntValue("tranAmount");
		}
		printString += builder.normal("--------------------------------");
		printString += "总金额：" + Tools.formatFen(Integer.valueOf(mixInitAmount)) + "元" + builder.br();
		printString += "待收：" + Tools.formatFen(Integer.valueOf(shouldAmount)) + "元" + builder.br();
		printString += "实收: "+ Tools.formatFen(costAmount)+"元"+builder.br();
		// printer.print(printStringPayFor);
		printString += builder.branch();
		printString += builder.endPrint();
		controller.print(printString);
	}



	@Override
	protected Intent bundleResult() {
		Intent intent = super.bundleResult();
		intent.putExtra("cardBalance",
				Tools.formatFen(jTranLog.getLongValue("balance")));
		intent.putExtra("showFlag", "1"); // 是否显示商户红包券
		intent.putExtra("addTicketList", addTicketList);//券改造
		if(PaymentApplication.getInstance().isWemengMerchant()){//为微盟券增加
//			intent.putExtra(Constants.isUsedWemngTicket, commonTicketManager.isContainsWemengTicket()); 
			intent.putExtra(Constants.wemengTicketInfo, wemengTicketInfo);
		}
		return intent;
	}

	public ArrayList<TicketInfo> getAddTicketList() {
		return addTicketList;
	}
	
	
}
