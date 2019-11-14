package com.wizarpos.pay.setting.presenter;

import android.text.TextUtils;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class WepayConfig {

	private static WepayConfig wepayConfig;

	private WepayConfig() {
	}

	public static WepayConfig getInstance() {
		if (wepayConfig == null) {
			wepayConfig = new WepayConfig();
		}
		return wepayConfig;
	}

	/**
	 * 修改微信配置
	 * 
	 * @param partnerId
	 * @param key
	 * @param agentId
	 */
	public void modifyWepayConfig(String appId, String key, String mchid) {
		if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(key)
				|| TextUtils.isEmpty(mchid)) {
			return;
		}
		AppConfigHelper.setConfig(AppConfigDef.weixin_app_id, appId);
		AppConfigHelper.setConfig(AppConfigDef.weixin_app_key, key);
		AppConfigHelper.setConfig(AppConfigDef.weixin_mchid_id, mchid);
	}

	/**
	 * 配置微信支付是否可用
	 * 
	 * @param isUseable
	 */
	public void modifyWepayUseable(boolean isUseable) {
		String result = (isUseable ? "true" : "false");
		AppConfigHelper.setConfig(AppConfigDef.use_weixin_pay, result);
	}

}
