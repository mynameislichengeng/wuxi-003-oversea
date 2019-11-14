package com.wizarpos.pay.cashier.pay_tems.wepay;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.AlipayGetOrderNoReq;
import com.wizarpos.pay.cashier.pay_tems.wepay.entities.WechatpayMicroReq;
import com.wizarpos.pay.cashier.pay_tems.wepay.inf.WepayNativeTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class WepayNativeTransactionImpl extends WePayTransactionImp implements WepayNativeTransaction {

	public WepayNativeTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void getBarCode(final ResultListener listener) {
		// transactionInfo.setRealAmount(transactionInfo.getInitAmount());//除bug 不能这么直接设置 wu
		try {
			AlipayGetOrderNoReq req = new AlipayGetOrderNoReq();
			req.setAmount(transactionInfo.getRealAmount());
			req.setExtraAmount(transactionInfo.getReduceAmount());
			req.setCardNo(transactionInfo.getCardNo());
			req.setRechargeOn(transactionInfo.getRechargeOn());
			req.setMasterPayAmount(transactionInfo.getMixInitAmount());// 混合支付总金额 wu@[20150923]
			// req.setInputAmount(transactionInfo.getInitAmount());//第三方应用带过来的数据 wu@[20150923]
			if (isMixTransaction()) {
				req.setInputAmount(transactionInfo.getMixInitAmount());// 组合支付总金额
			} else {
				req.setInputAmount(transactionInfo.getInitAmount());
			}
			req.setPatternId(AppConfigHelper.getConfig(AppConfigDef.weixin_mchid_id));
			req.setQQKey(getKey());
			req.setAppid(AppConfigHelper.getConfig(AppConfigDef.weixin_app_id));
			req.setIsPayComingForm(transactionInfo.getIsPayComingForm());// XXX 组合支付增加
			req.setMixFlag(transactionInfo.getMixFlag());
			req.setMasterTranLogId(transactionInfo.getMixTranLogId());
			req.setCommomcashierOrderId(transactionInfo.getCommoncashierOrderId()); // 销售单id wu@[20151012]
			getWechatpayOrderNo(req, Constants.SC_816_WEIXIN_ORDER_NO, new BasePresenter.ResultListener() {
				@Override
				public void onSuccess(Response response) {
					Logger2.debug("获取微信订单号成功！");
					createOrder(null, listener);
				}

				@Override
				public void onFaild(final Response response) {
					Logger2.debug("获取微信订单号失败！");
					listener.onFaild(response);
				}
			});

		} catch (Exception e) {
			listener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}

	}

	protected void createOrder(String authCode, final ResultListener listener) {
		WechatpayMicroReq req = new WechatpayMicroReq();
		req.setBody(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付" : transactionInfo.getBody());
		req.setOut_trade_no(transactionInfo.getTranId());
		req.setTotal_fee(transactionInfo.getRealAmount());
		req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
		req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
		// String nativeReqJson = JSONObject.toJSONString(req);
		Gson gson = new Gson();
		String nativeReqJson = gson.toJson(req);
		createOrder(FLAG_WECHATPAY_NATIVE, nativeReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("微信下单成功" + response.result);
					parseNative(listener, response);
				} catch (Exception e) {
					e.printStackTrace();
					listener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("微信下单失败" + response.result);
				if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
					revokeMixTranLog(transactionInfo.getMixTranLogId(), new ResultListener() {

						@Override
						public void onSuccess(Response response1) {
							listener.onFaild(response);
						}

						@Override
						public void onFaild(Response response1) {
							listener.onFaild(response);
						}
					});
				} else {
					listener.onFaild(response);
				}
			}
		});

	}

	protected void parseNative(ResultListener listener, Response response) {
		JSONObject json = JSONObject.parseObject(response.getResult().toString());
		Logger2.debug(response.getResult().toString());
		String url = json.getString("code_url");
		if (TextUtils.isEmpty(url)) {
			listener.onFaild(new Response(1, "数据解析异常"));
			return;
		}
		listener.onSuccess(new Response(0, "获取二维码成功", url));
		Logger2.debug("微信扫码支付获取二维码成功,开始轮询");

	}

	@Override
	public void listenResult(ResultListener listener) {
		looperQuery(listener);
	}

}
