package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.inf.Transaction;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.db.DBHelper;
import com.wizarpos.pay.model.MarketPayReq;
import com.wizarpos.pay.model.UseTicket;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易定义类
 * 
 * @author wu
 */

public abstract class TransactionImpl extends BasePresenter implements Transaction {

	// // 交易类型编号 function
	// protected static final int TRANSACTION_TYPE_BANK_CARD = 1;// 1 银行卡
	// protected static final int TRANSACTION_TYPE_MEMBER_CARD = 2;// 2会员卡
	// protected static final int TRANSACTION_TYPE__CASH = 3;// 3 现金
	// protected static final int TRANSACTION_TYPE_OTHER = 4;// 4 其他
	// protected static final int TRANSACTION_TYPE_WEPAY_MEMBER_CARD = 6;// 6
	// 微信会员卡
	// protected static final int TRANSACTION_TYPE_ALIPAY_MEMBER_CARD = 7;//
	// 7支付宝支付
	// protected static final int TRANSACTION_TYPE_WEPAY_PAY = 8;// 8微信支付
	// protected static final int TRANSACTION_TYPE_TEN_PAY = 9;// 8微信支付
	protected int transactionType = -1;

	protected String tranId;// 订单号 (线上支付订单号)
	protected String transTime;// 付款时间
	protected String giftPoints;// 赠送积分
	protected boolean isOffline;// 是否是离线模式
	protected String tranLogId = "";// 流水号 (为简便,离线交易和在线交易统一使用该id)
	protected String mixTranLogId = "";// 混合支付流水号
	protected String isPayComingForm = "0";
	/** 券的扣减金额*/
	protected int ticketReduceAomount;

	protected ThirdAppTransactionRequest thirdRequest;
	/*
	 * 服务端金额定义和本地定义不同<br> 服务端:<br> payAmount 应付金额<br> extraAmount 扣减金额<br>
	 * inputAmount 实付金额<br> 本地:<br> initAmount 初始金额 (传入金额)<br> shouldAmount
	 * 应付金额<br> realAmount 实付金额<br> reduceAmount 扣减金额 <br> changeAmount 找零金额
	 * <br> discountAmount 折扣扣减金额<br>
	 */

	/* 金额精确到分 */
	/**
	 * 初始金额 (传入金额)
	 */
	protected String initAmount = "0";
	/**
	 * 应付金额
	 */
	protected String shouldAmount = "0";
	/**
	 * 实付金额
	 */
	protected String realAmount = "0";
	/**
	 * 扣减金额
	 */
	protected String reduceAmount = "0";
	/**
	 * 找零金额
	 */
	protected String changeAmount = "0";
	/**
	 * 折扣扣减金额
	 */
	protected String discountAmount = "0";
	/**
	 * 已收金额
	 */
	protected String receivedAmount = "0";

	/**
	 * 混合支付总金额
	 */
	protected String mixInitAmount = "0";
	
	/**
	 * 四舍五入金额
	 */
	protected String round = "0";
	
	protected String saleInputAmount = "0";

//	/**
//	 * 混合支付总金额
//	 */
//	protected String masterPayAmount = "0";

	protected DeviceManager deviceManager;

	// ========额外参数(其他应用传入)===================================

	/**
	 * 是否自动发券
	 */
	protected String isAutoPublishTicket = "3";// 初始值为3 , 是否自动发券(1(auto) 自动
												// 2(self)手工 3(auto_self)半自动)

	protected String cardNo = null; // 会员卡号 (可能从第三方应用传入)
	protected String cardType = "0"; // 卡类型 (可能从第三方应用传入)
	protected boolean isNeedPrint = true; // 是否需要打印小票,第三方应用传入
	protected boolean isNeedTicket = true; // 是否需要发券，第三方应用传入
	protected boolean isNeedPrintCommonTikcet = true; // 是否需要打印普通券,第三方应用传入
	protected boolean isNeedPrintWepayTicket = true; // 是否需要打印微信券,第三方应用传入
	protected String showFlag = null; // 是否显示商户红包券

	protected String rechargeOn;// 充值(会员易调用支付易传入)

	protected String goodsName = "";// 商品名称

	protected String ticketIds = "";//营销活动一期 返回的券限制
	protected String marketOriginalPrice = "";//营销活动一期 营销原价
	/**	营销活动可用券	**/
	protected List<UseTicket> canUseTickets;
	/**	营销活动已用券	**/
	protected List<UseTicket> hasUsedTickets;

	// ========end 额外参数(其他应用传入)===================================

	public TransactionImpl(Context context) {
		super(context);
	}

	public void handleIntent(Intent intent) {

		transactionType = intent.getIntExtra(Constants.TRANSACTION_TYPE, -1);// 交易类型.如果传入transactionType.则已传入的类型为准,否则子类根据自己的交易类型定义

		initAmount = intent.getStringExtra("initAmount");
		if (TextUtils.isEmpty(initAmount)) {
			initAmount = "0";
		}
		shouldAmount = intent.getStringExtra("shouldAmount");
		if (TextUtils.isEmpty(shouldAmount)) {
			shouldAmount = initAmount;
		}
		discountAmount = intent.getStringExtra("discountAmount");
		if (TextUtils.isEmpty(discountAmount)) {
			discountAmount = "0";
		} else {
			shouldAmount = Calculater.subtract(shouldAmount, discountAmount);
		}
		reduceAmount = intent.getStringExtra("reduceAmount");
		if (TextUtils.isEmpty(reduceAmount)) {
			reduceAmount = "0";
		} else {
			shouldAmount = Calculater.subtract(shouldAmount, reduceAmount);
		}
		realAmount = intent.getStringExtra("realAmount");
		if (TextUtils.isEmpty(realAmount)) {
			realAmount = "0";
		}
		changeAmount = intent.getStringExtra("changeAmount");
		if (TextUtils.isEmpty(changeAmount)) {
			changeAmount = "0";
		}
		receivedAmount = intent.getStringExtra("receivedAmount");
		if (TextUtils.isEmpty(receivedAmount)) {
			receivedAmount = "0";
		}
		mixInitAmount = intent.getStringExtra(Constants.mixInitAmount);
		if (TextUtils.isEmpty(mixInitAmount)) {
			mixInitAmount = "0";
		}
		tranLogId = intent.getStringExtra(Constants.tranLogId);
		mixTranLogId = intent.getStringExtra(Constants.mixTranLogId);
		ThirdAppTransactionRequest thirdAppTransactionRequest =
				(ThirdAppTransactionRequest)intent.getSerializableExtra(Constants.thirdRequest);
		if(thirdAppTransactionRequest != null) {
			saleInputAmount = thirdAppTransactionRequest.getSaleInputAmount();//@hong第三方传递过来的saleInputAmount[2016118]
		}
//		saleInputAmount = intent.getStringExtra(Constants.saleInputAmount);//销售传入的初始金额 wu@[20151120]
		
		LogEx.d("mixpay", "handleIntent initAmount --> " + initAmount + " mixInitAmount --> "+ mixInitAmount + " mixTranLogId --> "+ mixTranLogId);
		
//		transactionType = intent.getIntExtra("TRANSACTION_TYPE", -1);
		cardNo = intent.getStringExtra("cardNo");
		if (cardNo == null) {
			cardNo = "";
		}
		cardType = intent.getStringExtra("cardType");
		if (TextUtils.isEmpty(cardType)) {
			cardType = "1";
		}
		if (intent.hasExtra("goodsName")) {
			goodsName = intent.getStringExtra("goodsName");
		}
		if (intent.hasExtra("ticketIds")) {
			ticketIds = intent.getStringExtra("ticketIds");
//			initCanUseTicket();//912接口返回ticketsId才需要用
		}
		if (intent.hasExtra("marketOriginalPrice")) {
			marketOriginalPrice = intent.getStringExtra("marketOriginalPrice");
			if (!TextUtils.isEmpty(marketOriginalPrice)) {
				initAmount = marketOriginalPrice;
				reduceAmount = Calculater.subtract(initAmount, shouldAmount);
			}
		}
		if (intent.hasExtra("rechargeOn")) {//bugfix reChargeOn为null yaosong [20160114]
			rechargeOn = intent.getStringExtra("rechargeOn");
		}
		
		initExtraParams(intent);
	}

	private void initExtraParams(Intent intent) {
		// 人工/自动 发券设置
		String _isAutoPrintTicket = DBHelper.querySelfAuto(context);// 查询支付易是否自动发券的参数配置
		if (_isAutoPrintTicket.equals("auto")) {
			isAutoPublishTicket = "1";
		} else if (_isAutoPrintTicket.equals("self")) {
			isAutoPublishTicket = "2";
		} else if (_isAutoPrintTicket.equals("auto_self")) {
			isAutoPublishTicket = "3";
		}
		String _isPrintCommonTicket = intent.getStringExtra("payment_need_dayin");
		isNeedPrintCommonTikcet = (_isPrintCommonTicket == null || "1".equals(_isPrintCommonTicket)); // 是否需要打印普通券
		String _isPrintWepayTicket = intent.getStringExtra("weixin_ticket_checked_need_dayin");
		isNeedPrintWepayTicket = (_isPrintWepayTicket == null || "1".equals(_isPrintWepayTicket)); // 是否需要打印微信券
		isOffline = ("1".equals(intent.getStringExtra("offline"))); // 如果intent
																	// 传入了offine
																	// 则以传入的值为准
		if (!isOffline) {// 当没有传入或者传入值为false时, 取app的状态
			isOffline = Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isOffline, Constants.FALSE));
		}

		thirdRequest = (ThirdAppTransactionRequest) intent.getSerializableExtra(Constants.thirdRequest);
		if (thirdRequest == null) { return; }
		isNeedPrint = TextUtils.isEmpty(thirdRequest.getNoPrint());// 有该参数表示第三方应用调用“收款”应用时支付后不打印小票。
		isNeedTicket = TextUtils.isEmpty(thirdRequest.getNoTicket());// 有该参数表示第三方应用调用“收款”应用时支付后不需要发券
		// disCountNeed = intent.getStringExtra("disCount");
	}

	/**
	 * 定义支付方式.
	 * 
	 * @param type
	 */
	protected void defTransactionType(int type) {
		if (transactionType == -1) {
			transactionType = type;
		}
	}

	/**
	 * 根据流水号查询交易明细
	 */
	public void getTransDetial(String tranLogId, final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tranLogId", tranLogId);
		if (TextUtils.isEmpty(tranLogId.trim())) {
			listener.onFaild(new Response(1, "输入的终端流水号不正确"));
			return;
		}
		NetRequest.getInstance().addRequest(Constants.SC_958_TERMINAL_TRANLOG_ID, params, new ResponseListener() {

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
	 * 根据流水号查询交易明细
	 */
	public void getTransDetial(String tranLogId,boolean bankcardpay,final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tranLogId", tranLogId);
		if (bankcardpay) {//isSimpleAcq 1
			params.put("isSimpleAcq", Constants.BANKCARDPAYQUERY);
		}
		if (TextUtils.isEmpty(tranLogId.trim())) {
			listener.onFaild(new Response(1, "输入的终端流水号不正确"));
			return;
		}
		NetRequest.getInstance().addRequest(Constants.SC_958_TERMINAL_TRANLOG_ID, params, new ResponseListener() {

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
	 * 打包返回结果
	 */
	protected Intent bundleResult() {
		Intent intent = new Intent();
		intent.putExtra("tranId", tranId);// 订单号
		if (!TextUtils.isEmpty(giftPoints) && !giftPoints.equals("0")) {// 积分
			intent.putExtra("giftPoints", giftPoints);
		}
		intent.putExtra(Constants.cardNo, cardNo);
		intent.putExtra(Constants.cardType, cardType);
		intent.putExtra(Constants.shouldAmount, shouldAmount);// 应付金额
		intent.putExtra(Constants.initAmount, initAmount);// 初始金额
		intent.putExtra(Constants.realAmount, realAmount);// 实付金额 bugfix 这个地方应为实付金额 wu@[20150909]
		intent.putExtra(Constants.reduceAmount, reduceAmount);// 扣减金额
		intent.putExtra(Constants.changeAmount, changeAmount);// 找零金额
		intent.putExtra(Constants.discountAmount, discountAmount);// 折扣金额
		intent.putExtra(Constants.TRANSACTION_TYPE, transactionType);// function
		intent.putExtra(Constants.offline, isOffline ? Constants.TRUE : Constants.FALSE);// 是否离线交易
		intent.putExtra(Constants.tranLogId, tranLogId);// 流水号
		intent.putExtra(Constants.mixFlag, isMixTransaction() ? "1" : "0");// 混合支付flag
		intent.putExtra(Constants.mixTranLogId, mixTranLogId);// 混合支付主流水号
		intent.putExtra(Constants.saleInputAmount, saleInputAmount); //销售传入金额
		intent.putExtra("ticketReduceAmount", ticketReduceAomount + "");//券扣减总计金额
		return intent;
	}

	/**
	 * 添加券时计算金额
	 * 
	 * @param ticketAmount
	 */
	protected void calculateAddTicket(int ticketAmount) {
		shouldAmount = Calculater.subtract(shouldAmount, ticketAmount + "");
		if (Calculater.compare("0", shouldAmount) > 0)// 如果券的金额大于应付金额
		{
			if (TransactionTemsController.TRANSACTION_TYPE_BANK_CARD == transactionType) {// 如果是银行卡类型,至少刷1分钱
				shouldAmount = "1";
			} else {
				shouldAmount = "0";
			}
		}
		reduceAmount = Calculater.plus(reduceAmount, ticketAmount + "");
	}

	/**
	 * 移除券时计算金额
	 * 
	 * @param ticketAmount
	 */
	protected void calulateRemoveTicket(int ticketAmount) {
		shouldAmount = Calculater.plus(shouldAmount, ticketAmount + "");
		reduceAmount = Calculater.subtract(reduceAmount, ticketAmount + "");
	}

	/**
	 * 设置实付金额
	 * 
	 * @param realAmount
	 *            实付金额
	 */
	public void setRealAmount(String realAmount) {
		this.realAmount = realAmount;
	}

	/**
	 * 获取初始金额
	 */
	public String getInitAmount() {
		return initAmount;
	}

	/**
	 * 获取应收金额
	 */
	public String getShouldAmount() {
		return shouldAmount;
	}

	/**
	 * 获取实收金额
	 */
	public String getRealAmount() {
		return realAmount;
	}

	/**
	 * 获取扣减金额
	 */
	public String getReduceAmount() {
		return reduceAmount;
	}

	/**
	 * 获取找零金额
	 */
	public String getChangeAmount() {
		return changeAmount;
	}

	/**
	 * 获取折扣扣减金额
	 */
	public String getDiscountAmount() {
		return discountAmount;
	}

	/**
	 * 设置已收金额
	 * 
	 * @param receivedAmount
	 */
	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	/**
	 * 获取已收金额
	 * 
	 * @return
	 */
	public String getReceivedAmount() {
		return receivedAmount;
	}

	@Override
	public boolean isMixTransaction() {
		return transactionType == TransactionTemsController.TRANSACTION_TYPE_MIXPAY;
	}

	@Override
	public String getTranId() {
		return tranId;
	}

	@Override
	public String getTranLogId() {
		return tranLogId;
	}

	@Override
	public int getTransactionType() {
		return transactionType;
	}

	public String getTicketIds() {
		return ticketIds;
	}

	public void setTicketIds(String ticketIds) {
		this.ticketIds = ticketIds;
	}
	
	public String getMarketOriginalPrice() {
		return marketOriginalPrice;
	}

	public void setMarketOriginalPrice(String marketOriginalPrice) {
		this.marketOriginalPrice = marketOriginalPrice;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onDestory() {

	}
	/**
	 * 营销活动
	 */
	private void goMarketPay(MarketPayReq req,final ResultListener listener ){
		/*接口号 912 参数 mid，cardNo，payMethod（1、现金支付  2、会员卡支付  3、微信支付  4、支付宝支付  5、银行卡支付
		），payAmount 

		{"code":0,"msg":"success","result":{"reducAmount":9980,"ticketIds":[]}} 成功返回报文*/

		try {
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("mid", req.getMid());
			map.put("cardNo", req.getCardNo());
			map.put("payMethod", req.getPayMethod());
			map.put("payAmount",new BigDecimal(req.getPayAmount()).multiply(new BigDecimal(100)).intValue() + "");
			NetRequest.getInstance().addRequest(Constants.SC_912_MARKETING_PAY, map, new ResponseListener() {
				@Override
				public void onSuccess(Response response) {
					listener.onSuccess(response);
				}

				@Override
				public void onFaild(Response response) {
					listener.onFaild(response);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 912接口返回ticketsId才需要用
	 * 初始化营销活动可用券
	 */
	protected void initCanUseTicket() {
		JSONArray jsonArray = JSONArray.parseArray(getTicketIds());
		for (int i = 0; i < jsonArray.size(); i++) {
			UseTicket useTicket = new UseTicket();// substring()
			int amount = jsonArray.get(i).toString().indexOf("|");
			useTicket.setId(jsonArray.get(i).toString().substring(0, amount));
			String mount = (String) jsonArray.get(i).toString().subSequence(amount + 1, jsonArray.get(i).toString().length());
			useTicket.setAmount(Integer.parseInt(mount));
			canUseTickets.add(useTicket);
			useTicket.setAmount(0);
			hasUsedTickets.add(useTicket);
		}
	}

	/**
	 * 912接口返回ticketsId才需要用
	 * 使用营销活动券
	 * @param ticketId
	 */
	protected void addHasUsedTicket(String ticketId) {
		for (int i = 0; i < hasUsedTickets.size(); i++) {
			if (ticketId.equals(hasUsedTickets.get(i).getId())) {
				hasUsedTickets.get(i).setAmount(hasUsedTickets.get(i).getAmount() + 1);
			}
		}
	}
	
	/**
	 * 912接口返回ticketsId才需要用
	 * 获取已使用营销活动券张数
	 * @param ticketId
	 * @return -1表示该券
	 */
	protected int getHasUsedTicketNumber(String ticketId) {
		for (int i = 0; i < hasUsedTickets.size(); i++) {
			if (ticketId.equals(hasUsedTickets.get(i).getId())) {
				return hasUsedTickets.get(i).getAmount();
			}
		}
		return -1;
	}
}
