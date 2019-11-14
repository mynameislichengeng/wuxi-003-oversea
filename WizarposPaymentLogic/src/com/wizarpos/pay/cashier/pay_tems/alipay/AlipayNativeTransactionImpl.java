package com.wizarpos.pay.cashier.pay_tems.alipay;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.AlipayGetOrderNoReq;
import com.wizarpos.pay.cashier.pay_tems.alipay.entity.AlipayMicroReq;
import com.wizarpos.pay.cashier.pay_tems.alipay.inf.AlipayNativeTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class AlipayNativeTransactionImpl extends AlipayTransaction implements AlipayNativeTransaction {

	public AlipayNativeTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void getBarCode(final ResultListener listener) {
//		transactionInfo.setRealAmount(transactionInfo.getInitAmount());//除bug 不能这么直接设置 wu
		try {
			AlipayGetOrderNoReq req = new AlipayGetOrderNoReq();
			req.setAmount(transactionInfo.getRealAmount());
			req.setExtraAmount(transactionInfo.getReduceAmount());
			req.setCardNo(transactionInfo.getCardNo());
			req.setRechargeOn(transactionInfo.getRechargeOn());
			req.setInputAmount(transactionInfo.getInitAmount());//第三方应用调用时传入的初始金额 
//			if(isMixTransaction()){
//				req.setInputAmount(transactionInfo.getMixInitAmount());//组合支付总金额
//			}else{
//				req.setInputAmount(transactionInfo.getInitAmount());
//			}
			req.setMasterPayAmount(transactionInfo.getMixInitAmount());//传入混合支付的总金额 wu@[20150923]
			req.setPatternId(AppConfigHelper.getConfig(AppConfigDef.alipay_pattern));
			req.setQQKey(getKey());
			req.setIsPayComingForm(transactionInfo.getIsPayComingForm());//XXX 组合支付增加
			req.setMixFlag(transactionInfo.getMixFlag());
			req.setMasterTranLogId(transactionInfo.getMixTranLogId());
			req.setCommomcashierOrderId(transactionInfo.getCommoncashierOrderId()); //销售单id wu@[20151012]
			getAlipayOrderNo(req, Constants.SC_815_ALIPAY_ORDER_NO, new BasePresenter.ResultListener() {
				@Override
				public void onSuccess(Response response) {
					Logger2.debug("获取支付宝订单号成功！");
					createAlipayOrder(null, listener);
				}

				@Override
				public void onFaild(Response response) {
					Logger2.debug("获取支付宝订单号失败！");
					listener.onFaild(response);
				}
			});

		} catch (Exception e) {
			listener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}
	}

	protected void createAlipayOrder(String authCode, final BasePresenter.ResultListener resultListener) {
		AlipayMicroReq req = new AlipayMicroReq();
		req.setAGENT_ID(AppConfigHelper.getConfig(AppConfigDef.alipay_agent_id));// 渠道商id
		req.setSTORE_ID(AppConfigHelper.getConfig(AppConfigDef.alipay_store_id));
		req.setPayeeTermId(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));
		req.setPartner(AppConfigHelper.getConfig(AppConfigDef.alipay_pattern));
		req.setOutTradeNo(transactionInfo.getTranId());
		req.setSubject(TextUtils.isEmpty(transactionInfo.getBody()) ? "支付宝支付" : transactionInfo.getBody());
//		req.setTERMINAL_ID(Build.SERIAL);
		req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
		req.setTotalFee(transactionInfo.getRealAmount());
		if (!TextUtils.isEmpty(authCode)) {
			req.setAuthCode(authCode);
		}
//		String microReqJson = JSONObject.toJSONString(req);
		Gson gson = new Gson();
		String nativeReqJson = gson.toJson(req);
		createOrder(FLAG_ALIPAY_NATIVE, nativeReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("支付宝下单成功" + response.result);
					parseNative(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("支付宝下单失败" + response.result);
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
		JSONObject json = JSONObject.parseObject(response.getResult().toString());
		String realPath = json.getString("realPath");
		if (TextUtils.isEmpty(realPath)) {
			resultListener.onFaild(new Response(1, "数据解析异常"));
			return;
		}
		resultListener.onSuccess(new Response(0, "获取二维码成功", realPath));
		Logger2.debug("支付宝扫码支付获取二维码成功,开始轮询");
	}

	@Override
	public void listenResult(ResultListener listener) {
		looperQuery(listener);
	}

}
