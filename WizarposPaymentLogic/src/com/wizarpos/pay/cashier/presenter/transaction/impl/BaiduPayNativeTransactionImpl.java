package com.wizarpos.pay.cashier.presenter.transaction.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.AlipayGetOrderNoReq;
import com.wizarpos.pay.cashier.presenter.transaction.inf.BaiduPayNativeTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.BaiduNativePayReq;

public class BaiduPayNativeTransactionImpl extends BaiduPayTransaction
		implements BaiduPayNativeTransaction {

	public BaiduPayNativeTransactionImpl(Context context) {
		super(context);
	}

	@Override
	public void getBarCode(final ResultListener resultListener) {
		Logger2.debug("获取百度支付的订单号");
//		transactionInfo.setRealAmount(transactionInfo.getInitAmount());//除bug 不能这么直接设置 wu
		try {
			String patternId=AppConfigHelper.getConfig(AppConfigDef.baidupay_id);
			AlipayGetOrderNoReq req = new AlipayGetOrderNoReq();
			req.setPatternId(patternId);
			req.setQQKey(sercrt());
			req.setRechargeOn(transactionInfo.getRechargeOn());
			req.setIsPayComingForm(transactionInfo.getIsPayComingForm());//XXX 组合支付增加
			req.setMixFlag(transactionInfo.getMixFlag());
			req.setMasterTranLogId(transactionInfo.getMixTranLogId());
			req.setCommomcashierOrderId(transactionInfo.getCommoncashierOrderId()); //销售单id wu@[20151012]
			getBaiduOrderNo(req, Constants.SC_843_BAIDU_PAY_ORDER_NO,
					new BasePresenter.ResultListener() {
						@Override
						public void onSuccess(Response response) {
							Logger2.debug("获取百度支付的订单号成功");
							createBaiudOrder(null, resultListener);
						}

						@Override
						public void onFaild(Response response) {
							Logger2.debug("获取百度支付的订单号失败");
							resultListener.onFaild(response);
						}
					});
		} catch (Exception e) {
			resultListener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}
	}

	/**
	 * 生成订单
	 */
	protected void createBaiudOrder(String authCode,
			final BasePresenter.ResultListener resultListener) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date orderCreateDate = new Date(System.currentTimeMillis());
		String orderCreateTime = format.format(orderCreateDate);
		BaiduNativePayReq req = new BaiduNativePayReq();
		req.setSp_no(AppConfigHelper.getConfig(AppConfigDef.baidupay_id));
		req.setOrder_create_time(orderCreateTime);
		req.setOrder_no(transactionInfo.getTranId());
		req.setGoods_name(TextUtils.isEmpty(transactionInfo.getBody()) ? "百度钱包支付"
				: transactionInfo.getBody());
		req.setTotal_amount(transactionInfo.getRealAmount());
		if (!TextUtils.isEmpty(authCode)) {
			req.setPay_code(authCode);
		}
		String nativeReqJson = JSONObject.toJSONString(req);
		Logger2.debug("百度支付请求参数:" + nativeReqJson);
		createOrder(FLAG_BAIDU_NATIVE, nativeReqJson,
				new BasePresenter.ResultListener() {
					@Override
					public void onSuccess(Response response) {
						try {
							Logger2.debug("百度支付下单成功:" + response.getResult());
							parseNative(resultListener, response);
						} catch (Exception e) {
							e.printStackTrace();
							resultListener.onFaild(new Response(1, "未知异常"));
						}
					}

					@Override
					public void onFaild(Response response) {
						Logger2.debug("百度支付下单失败");
						resultListener.onFaild(response);
					}
				});
	}

	private void parseNative(final BasePresenter.ResultListener resultListener,
			Response response) {
		JSONObject json = JSONObject.parseObject(response.getResult()
				.toString());
		String realPath = json.getString("realPath");
		if (TextUtils.isEmpty(realPath)) {
			resultListener.onFaild(new Response(1, "数据解析异常"));
			return;
		}
		resultListener.onSuccess(new Response(0, "获取二维码成功", realPath));
		Logger2.debug("百度扫码支付获取二维码成功,开始轮询");
	}

	public void listenResult(ResultListener resultListener) {
		looperQuery(resultListener);
	}

}
