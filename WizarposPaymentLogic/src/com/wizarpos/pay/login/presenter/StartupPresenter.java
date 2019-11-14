package com.wizarpos.pay.login.presenter;

import android.content.Context;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.setting.presenter.AppConfiger;

public class StartupPresenter extends BasePresenter {

	private AppConfiger appManager;

	public StartupPresenter(Context context) {
		super(context);
		appManager = new AppConfiger(context);
	}

	public void init(final ResultListener listener) {
//		if (Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.test_load_safe_mode))) {
			getPubCertificate(new ResultListener() {
				@Override
				public void onSuccess(Response response) {
					ping(listener);
				}

				@Override
				public void onFaild(Response response) {
					listener.onFaild(response);
				}
			});
//		} else {
//			ping(listener);
//		}
	}

	/**
	 * 取公钥证书
	 */
	public void getPubCertificate(ResultListener listener) {
		DeviceManager.getInstance().getPubCertificate(listener);
	}

	/**
	 * 测试网络环境
	 * 
	 * @param listener
	 */
	public void ping(final ResultListener listener) {
		appManager.ping(listener);
	}

}
