package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.cashier.model.AlipayGetOrderNoReq;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatCreateOrder;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.inf.OnlinePaymentTransaction;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.ZXingUtil;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.OrderDef;
import com.wizarpos.pay.model.TransactionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线上支付 Created by wu on 2015/6/26 0026.
 */
public abstract class OnlinePaymentTransactionImpl implements OnlinePaymentTransaction {

	protected Context context;

	protected TransactionInfo transactionInfo;

	protected List<TicketInfo> usedTicketlist = new ArrayList<TicketInfo>();

	public OnlinePaymentTransactionImpl(Context context) {
		this.context = context;
	}

	@Override
	public TransactionInfo getTransactionInfo() {
		return transactionInfo;
	}

	@Override
	public void handleIntent(Intent intent) {

		transactionInfo = new TransactionInfo();

		String initAmount = intent.getStringExtra(Constants.initAmount);
		if (TextUtils.isEmpty(initAmount)) {
			initAmount = "0";
		}
		transactionInfo.setInitAmount(initAmount);
		String saleInputAmount = intent.getStringExtra(Constants.saleInputAmount);
		if (!TextUtils.isEmpty(saleInputAmount)) {
			transactionInfo.setSaleInputAmount(saleInputAmount);
		}
		String shouldAmount = intent.getStringExtra(Constants.shouldAmount);
		if (TextUtils.isEmpty(shouldAmount)) {
			shouldAmount = initAmount;
		}
		transactionInfo.setShouldAmount(shouldAmount);

		String discountAmount = intent.getStringExtra(Constants.discountAmount);
		if (TextUtils.isEmpty(discountAmount)) {
			discountAmount = "0";
		}else{
			shouldAmount =Calculater.subtract(shouldAmount, discountAmount);
			transactionInfo.setShouldAmount(shouldAmount);
		}
		transactionInfo.setDiscountAmount(discountAmount);

		String reduceAmount = intent.getStringExtra(Constants.reduceAmount);
		if (TextUtils.isEmpty(reduceAmount)) {
			reduceAmount = "0";
		}else{
			shouldAmount =Calculater.subtract(shouldAmount, reduceAmount);
			transactionInfo.setShouldAmount(shouldAmount);
		}
		transactionInfo.setReduceAmount(reduceAmount);

		String realAmount = intent.getStringExtra(Constants.realAmount);
		if (TextUtils.isEmpty(realAmount)) {
			realAmount = "0";
		}
		transactionInfo.setRealAmount(realAmount);

		String changeAmount = intent.getStringExtra(Constants.changeAmount);
		if (TextUtils.isEmpty(changeAmount)) {
			changeAmount = "0";
		}
		String tipsAmount = intent.getStringExtra(Constants.tipsAmount);
		if (TextUtils.isEmpty(tipsAmount)) {
			tipsAmount = "0";
		}
		transactionInfo.setTips(tipsAmount);
		transactionInfo.setChangeAmount(changeAmount);

		String ticketTotalAmount = intent.getStringExtra("ticketTotalAmount");
		if(TextUtils.isEmpty(ticketTotalAmount)) {
			ticketTotalAmount = "0";
		}
		transactionInfo.setTicketTotalAomount(ticketTotalAmount);

		if (intent.hasExtra("marketOriginalPrice")) {
			transactionInfo.setMarketOriginalPrice(intent.getStringExtra("marketOriginalPrice"));
			if (!TextUtils.isEmpty(transactionInfo.getMarketOriginalPrice())) {
				initAmount = transactionInfo.getMarketOriginalPrice();
				transactionInfo.setInitAmount(initAmount);
				reduceAmount = Calculater.subtract(initAmount, shouldAmount);
				transactionInfo.setReduceAmount(reduceAmount);
			}
		}

		String mixInitAmount = intent.getStringExtra(Constants.mixInitAmount);
		if (TextUtils.isEmpty(mixInitAmount)) {
			mixInitAmount = "0";
		}
		String payType=intent.getStringExtra(Constants.payTypeFlag);
		transactionInfo.setMixInitAmount(mixInitAmount);
		transactionInfo.setPayTypeFlag(payType);
		transactionInfo.setTransactionType(intent.getIntExtra(Constants.TRANSACTION_TYPE, -1));

		transactionInfo.setBody(intent.getStringExtra(Constants.body));

		transactionInfo.setCardNo(intent.getStringExtra("cardNo"));
		transactionInfo.setRechargeOn(intent.getStringExtra("rechargeOn"));
		transactionInfo.setIsPayComingForm(intent.getStringExtra("isPayComingForm"));
		String mixFlag = intent.getStringExtra("mixFlag");
		transactionInfo.setMixFlag(mixFlag);
		if("1".equals(mixFlag)){
			transactionInfo.setTransactionType(TransactionTemsController.TRANSACTION_TYPE_MIXPAY);
		}
//		transactionInfo.setMixTranLogId(intent.getStringExtra("masterTranLogId"));
		transactionInfo.setMixTranLogId(intent.getStringExtra(Constants.mixTranLogId)); // 除bug 这个字段取错了... wu@[20150910]

		transactionInfo.setCommoncashierOrderId(intent.getStringExtra(Constants.commonCashierOrderId)); //销售单ID wu@[20151012]
		transactionInfo.setFirstTransaction(intent.getBooleanExtra(Constants.isFirstTrans, false));//是否是第一笔交易 wu@[20151021]

		if (intent.getSerializableExtra("usedTicketlist") != null && ((List<TicketInfo>)intent.getSerializableExtra("usedTicketlist")).size() > 0) {
			usedTicketlist = (List<TicketInfo>) intent.getSerializableExtra("usedTicketlist");
		}
		if (intent.getStringArrayListExtra("ticketIds") != null && intent.getStringArrayListExtra("ticketIds").size() > 0) {
			transactionInfo.setIds(intent.getStringArrayListExtra("ticketIds"));
		}
		initExtraParams(intent);
	}

	protected final static String FLAG_BAIDU_MICRO = "baidu_micro";
	protected final static String FLAG_BAIDU_NATIVE = "baidu_native";
	protected final static String FLAG_WEPAY = "wexin";
	protected final static String FLAG_ALIPAY = "zhifubao";
	protected final static String FLAG_QQ = "qq";
	protected final static String FLAG_XUNLIAN = "xunlian";
	protected final static String FLAG_ALIPAY_MICRO = "alipay_micro";
	protected final static String FLAG_ALIPAY_NATIVE = "alipay_native";
	protected final static String FLAG_WECHATPAY_MICRO = "weixin_micro";
	protected final static String FLAG_WECHATPAY_NATIVE = "weixin_native";
	protected final static String FLAG_TENPAY_MICRO = "qq_micro";

	private void initExtraParams(Intent intent) {

		ThirdAppTransactionRequest thirdRequest = (ThirdAppTransactionRequest) intent
				.getSerializableExtra(Constants.thirdRequest);
		if (thirdRequest == null) { return; }
		transactionInfo.setNeedPrint(TextUtils.isEmpty(thirdRequest.getNoPrint()) == false); // 没有该参数表示第三方应用调用“收款”应用时支付后不打印小票。
		transactionInfo.setNeedTicket(TextUtils.isEmpty(thirdRequest.getNoTicket()) == false); // 没有该参数表示第三方应用调用“收款”应用时支付后不需要发券
		// disCountNeed = intent.getStringExtra("disCount");
	}

	/**
	 * 生成扫码支付订单接口
	 *
	 * @param flag
	 *            交易类型
	 * @param paramJsonObject
	 *            请求参数 json
	 * @param listener
	 */
	protected void createOrder(String flag, String paramJsonObject, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", flag);
		String code = "";
		if (flag.equals(FLAG_BAIDU_MICRO) || flag.equals(FLAG_BAIDU_NATIVE)) {
			code = Constants.SC_850_BAIDU;
		} else if (flag.equals(FLAG_WEPAY)) {
			code = Constants.SC_816_WEIXIN_ORDER_NO;
		} else if (flag.equals(FLAG_ALIPAY)) {
			code = Constants.SC_815_ALIPAY_ORDER_NO;
		} else if (flag.equals(FLAG_QQ)) {
			code = Constants.SC_840_TENPAY_ORDER_NO;
		} else if (flag.equals(FLAG_ALIPAY_MICRO) || flag.equals(FLAG_ALIPAY_NATIVE)
				|| flag.equals(FLAG_WECHATPAY_MICRO) || flag.equals(FLAG_WECHATPAY_NATIVE)
				|| flag.equals(FLAG_TENPAY_MICRO)) {
			code = Constants.SC_853_ALIPAY_MICRO_PAY;
		}
		params.put("paramJsonObject", paramJsonObject);
		NetRequest.getInstance().addRequest(code, params, new ResponseListener() {
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

	protected void pay(String flag, String paramJsonObject, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", flag);
		params.put("paramJsonObject", paramJsonObject);
		NetRequest.getInstance().addRequest(Constants.SC_871_ONLINE_CREAT_PAY, params, new ResponseListener() {
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
	 * 接口871支付改成874
	 * @author yaoSong
	 */
	protected void payNew(String flag, String paramJsonObject, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", flag);
		params.put("paramJsonObject", paramJsonObject);
		NetRequest.getInstance().addRequest(Constants.SC_874_ONLINE_CREAT_PAY, params, new ResponseListener() {
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
	 * @date 2015-12-4 下午5:03:08 
	 * @param flag
	 * @param paramJsonObject
	 * @param listener
	 * @Description:移动支付(copy from {@link #payNew})
	 */
	protected void payUnion(String flag, String paramJsonObject, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", flag);
		params.put("paramJsonObject", paramJsonObject);
		//如果开启移动支付,修改接口成875(与874相比 不同在返回err_code统一,根据统一的err_code判断用户是否在输密码) @Hwc
		NetRequest.getInstance().addRequest(Constants.SC_875_BAT_UNION_PAY, params, new ResponseListener() {
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
	 * bat生成订单
	 * payBatType 1 微信 2支付宝 3qq 4百度 
	 * 接口号870 生成订单
	 * @author hong
	 */
	protected void createBatOrder(BatCreateOrder req, final String serviceCode, final BasePresenter.ResultListener listener){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", req.getAmount());
		params.put("extraAmount", req.getExtraAmount());
		params.put("inputAmount", req.getInputAmount());
		params.put("cardNo", req.getCardNo());
		params.put("rechargeOn", req.getRechargeOn());
		params.put("mixFlag", req.getMixFlag());
		params.put("masterTranLogId", req.getMasterTranLogId());
		params.put("masterPayAmount", req.getMasterPayAmount());
		params.put("payBatType", req.getPayBatType());
		params.put("isPayComingForm", req.getIsPayComingForm());
		params.put("captcha", req.getCaptcha());
		params.put("orderId", req.getCommoncashierOrderId());
		params.put("orderId", req.getMnFlag());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {

				}
				listener.onSuccess(new Response(0, "success"));
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
	 * @date 2015-12-4 下午4:40:49 
	 * @param req
	 * @param serviceCode
	 * @param listener
	 * @Description:移动支付支付 (Copy from {@link #createBatOrderNew})
	 */
	protected void createUnionBatOrder(BatCreateOrder req, final String serviceCode, final BasePresenter.ResultListener listener)
	{

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", req.getAmount());
		params.put("extraAmount", req.getExtraAmount());
		params.put("inputAmount", req.getInputAmount());
		params.put("cardNo", req.getCardNo());
		params.put("rechargeOn", req.getRechargeOn());
		params.put("mixFlag", req.getMixFlag());
		params.put("masterTranLogId", req.getMasterTranLogId());
		params.put("masterPayAmount", req.getMasterPayAmount());
		/**如果开启了统一扫码的话,pay_channel标示为"U" 并且将authCode传递给服务端(服务端根据authCode判断什么类型支付)*/
		params.put("pay_channel", "U");
		params.put("auth_code", req.getAuthCode());
		params.put("isPayComingForm", req.getIsPayComingForm());
		params.put("captcha", req.getCaptcha());
		params.put("orderId", req.getCommoncashierOrderId());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {
				}

				listener.onSuccess(new Response(0, "success"));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});

	}

	/**
	 * bat生成订单
	 * payBatType 1 微信 2支付宝 3qq 4百度 
	 * 接口号873 生成订单
	 * @author yaosong
	 */
	protected void createBatOrderNew(BatCreateOrder req, final String serviceCode, final BasePresenter.ResultListener listener){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", req.getAmount());
		params.put("extraAmount", req.getExtraAmount());
		params.put("inputAmount", req.getInputAmount());
		params.put("cardNo", req.getCardNo());
		params.put("rechargeOn", req.getRechargeOn());
		params.put("mixFlag", req.getMixFlag());
		params.put("masterTranLogId", req.getMasterTranLogId());
		params.put("masterPayAmount", req.getMasterPayAmount());
		params.put("pay_channel", adjustPayTypetoNewVersion(req.getPayBatType()));
		params.put("isPayComingForm", req.getIsPayComingForm());
		params.put("captcha", req.getCaptcha());
		params.put("orderId", req.getCommoncashierOrderId());
		params.put("ids", req.getIds());
		params.put("mnFlag", req.getMnFlag());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {
				}

				listener.onSuccess(new Response(0, "success"));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 调整参数去适应新的BAT生成订单接口
	 * @param payBatType
	 * @return
	 */
	private String adjustPayTypetoNewVersion(String payBatType) {
		if (Constants.BAT_V1_4_FLAG) {
			switch (payBatType) {
				case Constants.ALIPAY_BAT:
					return "A";

				case Constants.BAIDUPAY_BAT:

					return "B";

				case Constants.WEPAY__BAT:

					return "W";

				case Constants.TENPAY_BAT:

					return "T";

				default:
					break;
			}
		}
		return payBatType;
	}

//	/**                                                //组合支付修改
//	 * 获取订单号
//	 * 
//	 * @param listener
//	 */
//	protected void getBaiduOrderNo(String key, String patternId, final String serviceCode,
//			final BasePresenter.ResultListener listener) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("amount", transactionInfo.getRealAmount());
//		params.put("extraAmount", transactionInfo.getReduceAmount());
//		params.put("inputAmount", transactionInfo.getInitAmount());
//		params.put("cardNo", "");
//		params.put("rechargeOn", "");
//		params.put("patternId", patternId);
//		params.put("QQKey", key);
//		NetRequest.getInstance().addRequest(serviceCode, params, new BaseRequest.ResponseListener() {
//
//			@Override
//			public void onSuccess(Response response) {
//				JSONObject robj = (JSONObject) response.getResult();
//				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
//				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
//				transactionInfo.setTranLogId(orderDefJson.getString("id"));
//				try {//XXX 解析混合支付的主流水号
//					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
//					transactionInfo.setMixTranLogId(tempMixTranlogId);					
//				} catch (Exception e) {
//				}
//				listener.onSuccess(new Response(0, "success"));
//			}
//
//			@Override
//			public void onFaild(Response response) {
//				listener.onFaild(response);
//			}
//		});
//	}


	/**
	 * 获取订单号
	 *
	 * @param listener
	 */
	protected void getBaiduOrderNo(final AlipayGetOrderNoReq req,final String serviceCode,
								   final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", transactionInfo.getRealAmount());
		params.put("extraAmount", transactionInfo.getReduceAmount());
		params.put("inputAmount", transactionInfo.getInitAmount());
		params.put("cardNo", "");
		params.put("rechargeOn", transactionInfo.getRechargeOn());
		params.put("patternId", req.getPatternId());
		params.put("QQKey", req.getQQKey());
		params.put("orderId", req.getCommomcashierOrderId());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {
				}
				listener.onSuccess(new Response(0, "success"));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}


	/**
	 * 获取支付宝订单号[支付宝需要多传一个patternId]
	 *
	 * @param listener
	 */
	protected void getAlipayOrderNo(final AlipayGetOrderNoReq req, final String serviceCode,
									final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", req.getAmount());
		params.put("extraAmount", req.getExtraAmount());
		params.put("inputAmount", req.getInputAmount());
		params.put("cardNo", req.getCardNo());
		params.put("rechargeOn", req.getRechargeOn());
		params.put("patternId", req.getPatternId());
		params.put("QQKey", req.getQQKey());
		params.put("mixFlag", req.getMixFlag());
		params.put("masterTranLogId", req.getMasterTranLogId());
		params.put("masterPayAmount", req.getMasterPayAmount());
		params.put("isPayComingForm", req.getIsPayComingForm());
		params.put("orderId", req.getCommomcashierOrderId());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {
				}

				listener.onSuccess(new Response(0, "success"));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 获取QQ钱包订单号 QQkey patternId userId userPasswd
	 *
	 * @param listener
	 */
	protected void getTenpayOrderNo(String key, String patternId, final String serviceCode, String userId,
									String userPasswd, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", transactionInfo.getRealAmount());
		params.put("extraAmount", transactionInfo.getReduceAmount());
		params.put("inputAmount", transactionInfo.getInitAmount());
		params.put("cardNo", "");
		params.put("rechargeOn", transactionInfo.getRechargeOn());
		params.put("patternId", patternId);
		params.put("QQKey", key);
		params.put("userId", userId);
		params.put("userPasswd", userPasswd);
		params.put("payBatType", Constants.TENPAY_BAT);
		params.put("orderId", transactionInfo.getCommoncashierOrderId());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {
				}
				listener.onSuccess(new Response(0, "success"));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 获取微信订单号
	 *
	 * @param listener
	 */
	protected void getWechatpayOrderNo(AlipayGetOrderNoReq req, final String serviceCode,
									   final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", req.getAmount());
		params.put("extraAmount", req.getExtraAmount());
		params.put("inputAmount", req.getInputAmount());
		params.put("cardNo", req.getCardNo());
		params.put("rechargeOn", req.getRechargeOn());
		params.put("patternId", req.getPatternId());
		params.put("QQKey", req.getQQKey());
		params.put("mixFlag", req.getMixFlag());
		params.put("masterTranLogId", req.getMasterTranLogId());
		params.put("masterPayAmount", req.getMasterPayAmount());
		params.put("isPayComingForm", req.getIsPayComingForm());
		params.put("appid", req.getAppid());
		params.put("orderId", req.getCommomcashierOrderId());
		NetRequest.getInstance().addRequest(serviceCode, params, new ResponseListener() {

			@Override
			public void onSuccess(Response response) {
				JSONObject robj = (JSONObject) response.getResult();
				JSONObject orderDefJson = (JSONObject) robj.get("orderDef");
				transactionInfo.setTranId(orderDefJson.getString("orderNo"));
				transactionInfo.setTranLogId(orderDefJson.getString("id"));
				try {//XXX 解析混合支付的主流水号
					String tempMixTranlogId = orderDefJson.getString("masterTranLogId");
					transactionInfo.setMixTranLogId(tempMixTranlogId);
				} catch (Exception e) {
				}
				listener.onSuccess(new Response(0, "success"));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	@Override
	public void checkOrder(String tranId, final BasePresenter.ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", tranId);
		NetRequest.getInstance().addRequest(Constants.SC_820_ORDER_DEF_DETAIL, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						if (response.result == null) {
							response.code = 1;
							response.msg = "数据解析失败";
							listener.onFaild(response);
							return;
						}
						OrderDef order = JSONObject.parseObject(response.result.toString(), OrderDef.class);
						if(StringUtil.isSameString(order.getTimeOutFlag(),OrderDef.TIME_OUT))
						{
							order.setState(OrderDef.STATE_TIME_OUT);
						}
						Logger2.debug("查询到订单状态" + OrderDef.getOrderStateDes(order.getState()));
						listener.onSuccess(new Response(0, OrderDef.getOrderStateDes(order.getState()), order));
					}

					@Override
					public void onFaild(Response response) {
						Logger2.debug("无法轮询到订单状态,继续轮询");
						listener.onFaild(new Response(1, response.msg));
					}
				});
	}

	@Override
	public void revokeTrans(String code, String tranLogId, final BasePresenter.ResultListener listener) {
		Logger2.debug("撤销线上支付");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tranLogId", tranLogId);
		NetRequest.getInstance().addRequest(code, params, new ResponseListener() {
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

	@Override
	public void printRevokeCode() {
		if (transactionInfo == null || TextUtils.isEmpty(transactionInfo.getTranLogId())) { return; }
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		String tranLogId = Tools.deleteMidTranLog(transactionInfo.getTranLogId(),
				AppConfigHelper.getConfig(AppConfigDef.mid));
		tranLogId = tranLogId.replace("P", "");
		Bitmap bitmap = ZXingUtil.genQRCodeTow(tranLogId);
		controller.print(bitmap);
		Q1PrintBuilder pb = new Q1PrintBuilder();
		String printString = "";
		printString += pb.endPrint();
		controller.print(printString);
	}

	/**
	 * 打包返回结果
	 */
	public Intent bundleResult() {
		Intent intent = new Intent();
		intent.putExtra("tranId", transactionInfo.getTranId());// 订单号
		CharSequence giftPoints = transactionInfo.getTranId();
		if (!TextUtils.isEmpty(giftPoints) && !giftPoints.equals("0")) {// 积分
			intent.putExtra("giftPoints", giftPoints);
		}
		// intent.putExtra("cardNo", cardNo);
		// intent.putExtra("cardType", cardType);
		intent.putExtra("shouldAmout", transactionInfo.getShouldAmount());// 应付金额
		intent.putExtra("initAmount", transactionInfo.getInitAmount());// 初始金额
		intent.putExtra("realAmount", transactionInfo.getRealAmount());// 实付金额
		intent.putExtra("reduceAmount", transactionInfo.getReduceAmount());// 扣减金额
		intent.putExtra("changeAmount", transactionInfo.getChangeAmount());// 找零金额
		intent.putExtra("TRANSACTION_TYPE", transactionInfo.getTransactionType());// function
		intent.putExtra("payType", transactionInfo.getPayType());// 移动支付的支付类型"BATW"
		intent.putExtra("offline", transactionInfo.isOffline() ? Constants.TRUE : Constants.FALSE);// 是否离线交易
		intent.putExtra("tranLogId", transactionInfo.getTranLogId());// 流水号
		intent.putExtra("mixFlag",transactionInfo.getTransactionType() == TransactionTemsController.TRANSACTION_TYPE_MIXPAY ? "1" : "0");// 混合支付flag
		intent.putExtra("mixTranLogId", transactionInfo.getMixTranLogId());// 混合支付主流水号
		intent.putExtra("ticketReduceAmount", transactionInfo.getTicketTotalAomount());//券扣减总计
		return intent;
	}

	/**
	 * 定义支付方式
	 *
	 * @param type
	 */
	protected void defTransactionType(int type) {
		if (transactionInfo.getTransactionType() == -1) {
			transactionInfo.setTransactionType(type);
		}
	}

	@Override
	public boolean isMixTransaction() {
		return transactionInfo.getTransactionType() == TransactionTemsController.TRANSACTION_TYPE_MIXPAY;
	}

	protected void revokeMixTranLog(String materTranLogId, final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("masterTranLogId", materTranLogId);
		NetRequest.getInstance().addRequest(Constants.SC_805_CANCEL_MIX_TRANLOG, params, new ResponseListener() {
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
}
