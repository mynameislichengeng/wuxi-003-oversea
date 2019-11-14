package com.wizarpos.pay.setting.presenter;

import android.text.TextUtils;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * 支付宝配置
 * 
 * @author wu
 */
public class AlipayConfig {

	private static AlipayConfig alipayConfig;

	private AlipayConfig() {
	}

	public static AlipayConfig getInstance() {
		if (alipayConfig == null) {
			alipayConfig = new AlipayConfig();
		}
		return alipayConfig;
	}

	/**
	 * 修改支付宝配置
	 * 
	 * @param partnerId
	 * @param key
	 * @param agentId
	 */
	public void modifyAlipayConfig(String partnerId, String key, String agentId) {
		if (TextUtils.isEmpty(partnerId) || TextUtils.isEmpty(key)) {
			return;
		}
		AppConfigHelper.setConfig(AppConfigDef.alipay_pattern, partnerId);
		AppConfigHelper.setConfig(AppConfigDef.alipay_key, key);
		AppConfigHelper.setConfig(AppConfigDef.alipay_agent_id, agentId);
	}

	/**
	 * 配置支付宝支付是否可用
	 * 
	 * @param isUseable
	 */
	public void modifyAlipayUseable(boolean isUseable) {
		String result = (isUseable ? "true" : "false");
		AppConfigHelper.setConfig(AppConfigDef.use_alipay, result);
	}

	/**
	 * 配置支付宝卡券核销是否可用
	 */
	public void modifyAlipayTicketCheck(boolean isUseable) {
		String result = (isUseable ? "true" : "false");
		AppConfigHelper.setConfig(AppConfigDef.use_alipay_ticket_checked,
				result);
	}

}
