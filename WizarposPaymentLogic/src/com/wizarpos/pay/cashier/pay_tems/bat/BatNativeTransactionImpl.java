package com.wizarpos.pay.cashier.pay_tems.bat;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatCreateOrder;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatNewReq;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatReq;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.NativeTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.NetWorkUtils;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;

public class BatNativeTransactionImpl extends BatTransation implements
		NativeTransaction {

	private CommonTicketManager commonTicketManager;
//	private TicketInfo wemengTicketInfo;

	public BatNativeTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void getBarCode(final ResultListener listener) {

	}

	protected void createOrder(String authCode,
			final BasePresenter.ResultListener resultListener) {
		BatReq req = new BatReq();
		String transType = transactionInfo.getPayTypeFlag();
		String payFlag = "";
		if (!TextUtils.isEmpty(transType)) {
			if (transType.equals(Constants.ALIPAY_BAT)) {
				req.setPayeeTermId(AppConfigHelper
						.getConfig(AppConfigDef.alipay_payeeTermId));
				req.setOutTradeNo(transactionInfo.getTranId());
				req.setSubject(TextUtils.isEmpty(transactionInfo.getBody()) ? "支付宝支付"
						: transactionInfo.getBody());
				// req.setTERMINAL_ID(Build.SERIAL);
				req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
				req.setTotalFee(transactionInfo.getRealAmount());
				payFlag = FLAG_ALIPAY_NATIVE;
			} else if (transType.equals(Constants.WEPAY__BAT)) {
				req.setBody(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付"
						: transactionInfo.getBody());
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setTotal_fee(transactionInfo.getRealAmount());
				req.setSpbill_create_ip(NetWorkUtils
						.getLocalIpAddress(PaymentApplication.getInstance()));
				req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
				payFlag = FLAG_WECHATPAY_NATIVE;
			} else if (transType.equals(Constants.BAIDUPAY_BAT)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				Date orderCreateDate = new Date(System.currentTimeMillis());
				String orderCreateTime = format.format(orderCreateDate);
				req.setOrder_create_time(orderCreateTime);
				req.setOrder_no(transactionInfo.getTranId());
				req.setGoods_name(TextUtils.isEmpty(transactionInfo.getBody()) ? "百度钱包支付"
						: transactionInfo.getBody());
				req.setTotal_amount(transactionInfo.getRealAmount());
				payFlag = FLAG_BAIDU_NATIVE;
			}
		}
		req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
		req.setCaptcha(AppConfigHelper.getConfig(AppConfigDef.auth_code));
		// String nativeReqJson = JSONObject.toJSONString(req);
		if (!TextUtils.isEmpty(authCode)) {
			req.setAuthCode(authCode);
		}
		Gson gson = new Gson();
		String nativeReqJson = gson.toJson(req);
		pay(payFlag, nativeReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("下单成功" + response.result);
					parseNative(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("下单失败" + response.result);
				if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
					revokeMixTranLog(transactionInfo.getMixTranLogId(), new ResultListener() {

						@Override
						public void onSuccess(Response response1) {
							resultListener.onFaild(response);
						}

						@Override
						public void onFaild(Response response1) {
							resultListener.onFaild(response);
						}
					});
				}else{
					resultListener.onFaild(response);
				}
			}
		});

	}
	
	/**
	 * 
	 * @param authCode
	 * @param resultListener
	 */
	protected void createOrderNew(String authCode,
			final BasePresenter.ResultListener resultListener) {
		BatNewReq req = new BatNewReq();
		String transType=transactionInfo.getPayTypeFlag();
		String payFlag="";
		if (!TextUtils.isEmpty(transType)) {
			if (!TextUtils.isEmpty(authCode)) {
				req.setAuth_code(authCode);
			}
			if (transType.equals(Constants.ALIPAY_BAT)) {
				payFlag=FLAG_ALIPAY_NATIVE;
//				req.setStore_id(store_id);//暂无门店id
				req.setPayee_term_id(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));//收款方终端号
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));//终端设备号
				req.setOut_trade_no(transactionInfo.getTranId());//商户订单号
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "支付宝支付" : transactionInfo.getBody()); //商品名称
				req.setTotal_fee(transactionInfo.getRealAmount());//支付金额
			}else if (transType.equals(Constants.WEPAY__BAT)) {
				payFlag=FLAG_WECHATPAY_NATIVE;
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付" : transactionInfo.getBody());
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setTotal_fee(transactionInfo.getRealAmount());
//				req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));//终端设备号
//			}else if (transType.equals(Constants.TENPAY_BAT)) {//QQ钱包无主扫
//				payFlag=FLAG_;
//				req.setOut_trade_no(transactionInfo.getTranId());
//				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "QQ钱包支付" : transactionInfo.getBody());
//				req.setTotal_fee(transactionInfo.getRealAmount());
//				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
			}else if (transType.equals(Constants.BAIDUPAY_BAT)) {
				payFlag=FLAG_BAIDU_NATIVE;
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "百度钱包支付"
						: transactionInfo.getBody());
				req.setTotal_fee(transactionInfo.getRealAmount());
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
			}
		}
		req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
		Gson gson = new Gson();
		String nativeReqJson = gson.toJson(req);
		payNew(payFlag, nativeReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("下单成功" + response.result);
					parseNative(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("下单失败" + response.result);
				if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
					revokeMixTranLog(transactionInfo.getMixTranLogId(), new ResultListener() {

						@Override
						public void onSuccess(Response response1) {
							resultListener.onFaild(response);
						}

						@Override
						public void onFaild(Response response1) {
							resultListener.onFaild(response);
						}
					});
				}else{
					resultListener.onFaild(response);
				}
			}
		});

	}

	protected void parseNative(ResultListener resultListener, Response response) {
		JSONObject json = JSONObject.parseObject(response.getResult()
				.toString());
		Logger2.debug(response.getResult().toString());
		if (transactionInfo.getPayTypeFlag().equals(Constants.BAIDUPAY_BAT)||transactionInfo.getPayTypeFlag().equals(Constants.ALIPAY_BAT)) {
			String realPath = json.getString("realPath");
			if (TextUtils.isEmpty(realPath)) {
				resultListener.onFaild(new Response(1, "数据解析异常"));
				return;
			}
			resultListener.onSuccess(new Response(0, "获取二维码成功", realPath));
		}else {
			String url = json.getString("code_url");
			if (TextUtils.isEmpty(url)) {
				resultListener.onFaild(new Response(1, "数据解析异常"));
				return;
			}
			resultListener.onSuccess(new Response(0, "获取二维码成功", url));
			Logger2.debug("扫码支付获取二维码成功,开始轮询");
		}
	}

	@Override
	public void listenResult(ResultListener listener) {
		looperQuery(listener);
	}

	@Override
	public void batPay(String payBatType,
					   final ResultListener resultListener) {
//		transactionInfo.setRealAmount(transactionInfo.getInitAmount()); //bugfix wu
		try {
			BatCreateOrder req = new BatCreateOrder();
			req.setAmount(transactionInfo.getRealAmount());
			req.setExtraAmount(transactionInfo.getReduceAmount());
			req.setCardNo(transactionInfo.getCardNo());
			req.setRechargeOn(transactionInfo.getRechargeOn());
//			req.setInputAmount(transactionInfo.getInitAmount());
			if (TextUtils.isEmpty(transactionInfo.getSaleInputAmount())) {
            	if(isMixTransaction()){
                    req.setInputAmount(transactionInfo.getMixInitAmount());//组合支付总金额
                }else{
                    req.setInputAmount(transactionInfo.getInitAmount());
                }
			}else {
				req.setInputAmount(transactionInfo.getSaleInputAmount());
			}
			req.setIsPayComingForm(transactionInfo.getIsPayComingForm());// XXX
		    req.setMasterPayAmount(transactionInfo.getMixInitAmount());//传入组合支付总金额 @hong[2016-1-26]
			req.setMixFlag(transactionInfo.getMixFlag());
			req.setMasterTranLogId(transactionInfo.getMixTranLogId());
			req.setPayBatType(payBatType);
			req.setCaptcha(AppConfigHelper.getConfig(AppConfigDef.auth_code));
			req.setCommoncashierOrderId(transactionInfo.getCommoncashierOrderId());
			req.setWmHxInfo(transactionInfo.getBatTicket());
			req.setIds(transactionInfo.getIds());
			req.setMnFlag(BatCreateOrder.MN_FLAG_NATIVE);
			if (Constants.BAT_V1_4_FLAG) {

				createBatOrderNew(req, Constants.SC_873_ONLINE_CREAT_ORDER,
						new BasePresenter.ResultListener() {
					@Override
					public void onSuccess(Response response) {
						Logger2.debug("获取订单号成功！");
						createOrderNew(null, resultListener);
					}
					
					@Override
					public void onFaild(Response response) {
						Logger2.debug("获取订单号失败！");
						resultListener.onFaild(response);
					}
				});
			} else {
				
				createBatOrder(req, Constants.SC_870_ONLINE_CREAT_ORDER,
						new BasePresenter.ResultListener() {
					@Override
					public void onSuccess(Response response) {
						Logger2.debug("获取订单号成功！");
						createOrder(null, resultListener);
					}
					
					@Override
					public void onFaild(Response response) {
						Logger2.debug("获取订单号失败！");
						resultListener.onFaild(response);
					}
				});
			}

		} catch (Exception e) {
			resultListener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}

	}

	@Override
	public void getTicketInfo(String ticketNum, final ResultListener resultListener) {
		if(commonTicketManager == null)
		{
			commonTicketManager = TicketManagerFactory.createCommonTicketManager(context);
		}
		commonTicketManager.getTicketInfo(ticketNum, transactionInfo.getInitAmount(), new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
//				if (commonTicketInfoResp.getWemengTicket() != null) {
//					wemengTicketInfo = commonTicketInfoResp.getWemengTicket();
//				}
				resultListener.onSuccess(new Response(0, "获取券信息成功", commonTicketInfoResp.getTicketInfo()));
			}
			
			@Override
			public void onFaild(Response response) {
				resultListener.onFaild(response);
			}
		});
	}

}
