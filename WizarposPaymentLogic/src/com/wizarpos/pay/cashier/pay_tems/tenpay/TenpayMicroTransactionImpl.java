package com.wizarpos.pay.cashier.pay_tems.tenpay;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.tenpay.entities.TenpayMicroRsp;
import com.wizarpos.pay.cashier.pay_tems.tenpay.entities.TenpayReq;
import com.wizarpos.pay.cashier.pay_tems.tenpay.inf.TenpayMicroTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class TenpayMicroTransactionImpl extends TenpayTransactionImpl implements TenpayMicroTransaction {

	public TenpayMicroTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void pay(final String authCode, final ResultListener resultListener) {
//		transactionInfo.setRealAmount(transactionInfo.getInitAmount());// 获取支付金额//除bug 不能这么直接设置 wu
		try {
			String key = getKey(AppConfigHelper.getConfig(AppConfigDef.tenpay_key));
			String patternId = AppConfigHelper.getConfig((AppConfigDef.tenpay_bargainor_id));
//			AppConfigHelper.setConfig(AppConfigDef.tenpay_op_user_id, TenpayConstants.BARGAINOR_ID);
			String userId = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_id);
			String userPasswd = getKey(AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_passwd));
			getTenpayOrderNo(key, patternId, Constants.SC_840_TENPAY_ORDER_NO, userId, userPasswd,
					new ResultListener() {
						@Override
						public void onSuccess(Response response) {
							Logger2.debug("获取QQ钱包订单号成功！");
							createOrder(authCode, resultListener);
						}

						@Override
						public void onFaild(Response response) {
							Logger2.debug("获取QQ钱包订单号失败！");
							resultListener.onFaild(response);
						}
					});
		} catch (Exception e) {
			resultListener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}

	}

	protected void createOrder(String authCode, final ResultListener resultListener) {
		TenpayReq req = new TenpayReq();
		req.setAuthCode(authCode);
		req.setReChargeOn(transactionInfo.getRechargeOn());//标记会员卡充值
		req.setBody(TextUtils.isEmpty(transactionInfo.getBody()) ? "QQ钱包支付" : transactionInfo.getBody());
		req.setOut_trade_no(transactionInfo.getTranId());
		req.setSp_device_id(AppConfigHelper.getConfig(AppConfigDef.sn));
//		req.setSp_device_id(TenpayConstants.DEVICE_ID);// XXX 测试用，发布时需去掉
		req.setTotal_fee(transactionInfo.getRealAmount());
		req.setOrderId(transactionInfo.getCommoncashierOrderId()); //销售单id wu@[20151012]
//		String microReqJson = JSONObject.toJSONString(req);
		Gson gson = new Gson();
		String microReqJson = gson.toJson(req);
		createOrder(FLAG_TENPAY_MICRO, microReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("QQ钱包下单成功" + response.result);
					parseMicro(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}
			}

			@Override
			public void onFaild(Response response) {
				Logger2.debug("QQ钱包下单失败" + response.result);
				resultListener.onFaild(response);
			}
		});

	}

	protected void parseMicro(ResultListener resultListener, Response response) {
		if (response.result != null && !TextUtils.isEmpty(response.getResult().toString())) {
			TenpayMicroRsp rsp = JSONObject.parseObject(response.result.toString(), TenpayMicroRsp.class);
			if (TenpayMicroRsp.CODE_SUCCESS.equals(rsp.getRet())) {
				Logger2.debug("QQ钱包被扫支付下单成功:支付成功");
				transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
				printTransInfo();
				resultListener.onSuccess(new Response(0, "支付成功", bundleResult()));
			} else if (TenpayMicroRsp.CODE_WAITE.equals(rsp.getRet())) {
				Logger2.debug("QQ钱包被扫支付下单成功,进入轮询");
				transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
				resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
				looperQuery(resultListener);
			} else {
				Logger2.debug("QQ钱包被扫支付下单成功:支付失败");
				resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
			}
		}
	}


}
