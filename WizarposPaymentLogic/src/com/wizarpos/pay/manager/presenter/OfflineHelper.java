package com.wizarpos.pay.manager.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.CashPayRepair;
import com.wizarpos.pay.db.TransactionManager;
import com.wizarpos.pay.db.UserEntityDao;
import com.wizarpos.pay.model.OfflineSave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfflineHelper extends BasePresenter {
	private OfflineSave offlineSave;

	private JSONObject merchantInfo = null;
	/**
	 * 慧商户号
	 */
	protected String mid = null;
	/**
	 * 慧商户联盟号
	 */
	protected String fid = null;
	/**
	 * 慧商户操作员
	 */
	protected String operatorNo = null;

	public OfflineHelper(Context context) {
		super(context);
	}

	/**
	 * 获取离线交易数据
	 */
	public List<CashPayRepair> getOfflineTransaction() {
		return TransactionManager.getInstance().getOfflineTransacton();
	}

	/**
	 * 更新
	 */
	public void update(final ResultListener listener) {

		Logger2.debug("更新商户信息");
		Map<String, Object> params = new HashMap<String, Object>();
		if (Constants.UNIFIEDLOGIN_FLAG){
			String loginOptNo = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
			params.put("loginName",loginOptNo);
			params.put("passwd",UserEntityDao.getInstance().getUser(loginOptNo).getPassword());
			params.put("lastTime",AppConfigHelper.getConfig(AppConfigDef.lastOpreatorUpateTime, "0"));
			params.put("mid",AppConfigHelper.getConfig(AppConfigDef.mid));
		} else {
            params.put("weixinMemberCardUsable", true);
            params.put("bankCardUsable", true);
            params.put("sysPosTimeStamp", "");
		}
		NetRequest.getInstance().addRequest(
				Constants.SC_100_MERCHANT_INFO_SUBMIT, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						merchantInfo = (JSONObject) response.getResult();
						JSONObject _merchant = merchantInfo
								.getJSONObject("merchant");
						JSONObject _terminal = merchantInfo
								.getJSONObject("terminal");
						if (_merchant == null || _terminal == null) {
							listener.onFaild(new Response(1, "获取商户信息失败"));
							return;
						}

						mid = _merchant.getString("mid");
						fid = _terminal.getString("fid");
						parseAndSaveMerchantInfoResp(listener);
					}

					@Override
					public void onFaild(Response response) {
						response.code = 1;
						response.msg = "调用登陆接口失败" + response.msg;
						listener.onFaild(response);
					}
				});

	}

	/**
	 * 解析并保存商户信息
	 */
	private void parseAndSaveMerchantInfoResp(ResultListener listener) {
		Logger2.debug("解析并保存商户信息");
		try {
			offlineSave = new OfflineSave();
			offlineSave.setJsonbject_1(merchantInfo.toString());
			JSONObject _merchant = merchantInfo.getJSONObject("merchant");
			if (_merchant.isEmpty()) {
				_merchant = Tools.jsonObjectToJavaBean(
						JSONObject.parseObject(offlineSave.getJsonbject_2()),
						JSONObject.class);
			} else {
				offlineSave.setJsonbject_2(_merchant.toJSONString());// 存储merchant信息
			}
			JSONObject _terminal = merchantInfo.getJSONObject("terminal");
			if (_terminal.isEmpty()) {
				_terminal = Tools.jsonObjectToJavaBean(
						JSONObject.parseObject(offlineSave.getJsonbject_3()),
						JSONObject.class);
			} else {
				offlineSave.setJsonbject_3(_terminal.toJSONString());// 存储terminal信息
			}

			String ip = "";
			String port = "";
			JSONArray jsonAry = merchantInfo.getJSONArray("sysParams");
			for (int i = 0; i < jsonAry.size(); i++) {
				JSONObject jsonSys = jsonAry.getJSONObject(i);
				if (jsonSys.getString("sysKey").equals("app_service_addr")) {
					ip = jsonSys.getString("sysValue");
				} else if (jsonSys.getString("sysKey").equals(
						"app_service_port")) {
					port = jsonSys.getString("sysValue");
				}
			}

			offlineSave.setMid(mid);
			offlineSave.setFid(fid);

			//TODO 确认这几个字段可以删除？ 商户终端体系改造
//			long _currentTime = 0L;
//			long _symTime = 0L;
			String _sysPosTimeStamp = "";
			String _merchant_has_weixin = "";
			if (!Constants.UNIFIEDLOGIN_FLAG){
//    			_currentTime = merchantInfo.getLongValue("currentTime");// ?
//    			_symTime = merchantInfo.getLongValue("symTime");// ?
    			_sysPosTimeStamp = merchantInfo.getString("sysPosTimeStamp");
				_merchant_has_weixin = merchantInfo.getBoolean("merchant_has_weixin") + "";
			}
			String _merchantName = _merchant.getString("merchantName");

			// 获取支付路由状态
			// paymentRouter = _merchant.getString("usePayRouteConfig");
			// 切换内嵌调用和外部调用支付路由
			String payRouterConfig = _merchant.getString("usePayRouteConfig");

			int payId = Integer.parseInt(_merchant.getString("payId"));

			// 存储慧商户信息
			AppConfigHelper.setConfig(AppConfigDef.pay_id,
					String.valueOf(payId));// 支付渠道
			AppConfigHelper.setConfig(AppConfigDef.mid, mid);// 慧商户号
			AppConfigHelper.setConfig(AppConfigDef.fid, fid);// 慧商户联盟号
			AppConfigHelper.setConfig(AppConfigDef.merchantName, _merchantName);// 收单商户名称
			AppConfigHelper.setConfig(AppConfigDef.use_pay_route_config,
					payRouterConfig);
			if (!Constants.UNIFIEDLOGIN_FLAG){
                AppConfigHelper.setConfig(AppConfigDef.sysPosTimeStamp,
                        _sysPosTimeStamp);
                AppConfigHelper.setConfig(AppConfigDef.merchant_has_weixin,
    					_merchant_has_weixin);
			}
			AppConfigHelper.setConfig(AppConfigDef.ip, ip);
			AppConfigHelper.setConfig(AppConfigDef.port, port);
			AppConfigHelper.setConfig(AppConfigDef.offline_save,
					JSONObject.toJSONString(offlineSave));
			AppConfigHelper.setConfig(AppConfigDef.operatorNo, operatorNo);
			listener.onSuccess(new Response(0, "更新成功"));
		} catch (Exception e) {
			e.printStackTrace();
			listener.onFaild(new Response(1, "更新商户信息失败"));
		}
	}

	/**
	 * 上传
	 */
	public void upload(ResultListener listener) {
		TransactionManager.getInstance().uploadTransaction(listener);
	}
}
