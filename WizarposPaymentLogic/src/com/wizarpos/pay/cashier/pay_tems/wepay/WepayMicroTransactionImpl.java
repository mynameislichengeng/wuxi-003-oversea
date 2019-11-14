package com.wizarpos.pay.cashier.pay_tems.wepay;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.AlipayGetOrderNoReq;
import com.wizarpos.pay.cashier.pay_tems.wepay.entities.WechatpayMicroReq;
import com.wizarpos.pay.cashier.pay_tems.wepay.entities.WechatpayMicroRsp;
import com.wizarpos.pay.cashier.pay_tems.wepay.inf.WepayMicroTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class WepayMicroTransactionImpl extends WePayTransactionImp implements WepayMicroTransaction {

	public WepayMicroTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void pay(final String authCode, final ResultListener resultListener) {
//		transactionInfo.setRealAmount(transactionInfo.getInitAmount());// 获取支付金额//除bug 不能这么直接设置 wu
		try {
			AlipayGetOrderNoReq req = new AlipayGetOrderNoReq();
			req.setAmount(transactionInfo.getRealAmount());
			req.setExtraAmount(transactionInfo.getReduceAmount());
			req.setCardNo(transactionInfo.getCardNo());
			req.setRechargeOn(transactionInfo.getRechargeOn());
			if(isMixTransaction()){
				req.setInputAmount(transactionInfo.getMixInitAmount());//组合支付总金额
			}else{
				req.setInputAmount(transactionInfo.getInitAmount());
			}
			req.setMasterPayAmount(transactionInfo.getMixInitAmount());//传入混合支付的总金额 wu@[20150923]
			req.setPatternId(AppConfigHelper.getConfig(AppConfigDef.weixin_mchid_id));
			req.setQQKey(getKey());
			req.setAppid(AppConfigHelper.getConfig(AppConfigDef.weixin_app_id));
			req.setIsPayComingForm(transactionInfo.getIsPayComingForm());//XXX 组合支付增加
			req.setMixFlag(transactionInfo.getMixFlag());
			req.setMasterTranLogId(transactionInfo.getMixTranLogId());
			req.setCommomcashierOrderId(transactionInfo.getCommoncashierOrderId()); //销售单id wu@[20151012]
			getWechatpayOrderNo(req, Constants.SC_816_WEIXIN_ORDER_NO, new BasePresenter.ResultListener() {
				@Override
				public void onSuccess(Response response) {
					Logger2.debug("获取微信订单号成功！");
					createOrder(authCode, resultListener);
				}

				@Override
				public void onFaild(Response response) {
					Logger2.debug("获取微信订单号失败！");
					resultListener.onFaild(response);
				}
			});

		} catch (Exception e) {
			resultListener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}

	}

	protected void createOrder(String authCode, final ResultListener resultListener) {
		WechatpayMicroReq req = new WechatpayMicroReq();
		req.setBody(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付" : transactionInfo.getBody());
		req.setOut_trade_no(transactionInfo.getTranId());
		req.setTotal_fee(transactionInfo.getRealAmount());
		req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
		req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
		if (!TextUtils.isEmpty(authCode)) {
			req.setAuthCode(authCode);
		}
//		String microReqJson = JSONObject.toJSONString(req);
		Gson gson = new Gson();
		String microReqJson = gson.toJson(req);
		createOrder(FLAG_WECHATPAY_MICRO, microReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("微信下单成功" + response.result);
					parseMicro(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("微信下单失败" + response.result);
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

	protected void parseMicro(ResultListener resultListener, Response response) {
		if (response.result != null && !TextUtils.isEmpty(response.getResult().toString())) {
			int code = response.code;
			JSONObject object = JSONObject.parseObject(response.result.toString());

			if (code == 0) {
				if (object.containsKey("err_code")
						&& WechatpayMicroRsp.CODE_WAITE.equals(object.get("err_code").toString())) {// 需要輸入密碼
					WechatpayMicroRsp rsp = new WechatpayMicroRsp();
					rsp.setRet(WechatpayMicroRsp.CODE_WAITE);
					rsp.setMsg("请用户输入密码");
					Logger2.debug("微信被扫支付下单成功,进入轮询");
					resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
					looperQuery(resultListener);
				} else {
					WechatpayMicroRsp wechatpayMicroRsp = JSONObject.parseObject(response.result.toString(),
							WechatpayMicroRsp.class);
					Logger2.debug("微信被扫支付下单成功:支付成功");
					transactionInfo.setThirdTradeNo(wechatpayMicroRsp.getThirdTradeNo());
					printTransInfo();
					resultListener.onSuccess(new Response(0, "支付成功", bundleResult()));
				}
			} else {
				Logger2.debug("微信被扫支付下单成功:支付失败");
				WechatpayMicroRsp wechatpayMicroRsp = JSONObject.parseObject(response.result.toString(),
						WechatpayMicroRsp.class);
				resultListener.onFaild(new Response(1, wechatpayMicroRsp.getMsg() + "[" + wechatpayMicroRsp.getRet()
						+ "]"));
			}
		}

	}

}
