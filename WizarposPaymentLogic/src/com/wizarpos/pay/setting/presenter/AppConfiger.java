package com.wizarpos.pay.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.db.AppConfig;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用程序管理类
 *
 * @author wu
 */
public class AppConfiger extends BasePresenter {
    // 微信配置
    private WepayConfig wepayConfig;
    // 支付宝配置
    private AlipayConfig alipayConfig;

    public AppConfiger(Context context) {
        super(context);
        wepayConfig = WepayConfig.getInstance();
        alipayConfig = AlipayConfig.getInstance();
    }

    public String getIp() {
        return AppConfigHelper.getConfig(AppConfigDef.ip);
    }

    public String getPort() {
        return AppConfigHelper.getConfig(AppConfigDef.port);
    }

    public boolean checkSecurityPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        String nowPassword = AppConfigHelper
                .getConfig(AppConfigDef.secure_password);
        String commonPassword = AppConfigHelper.getConfig(AppConfigDef.common_secure_password);
        if (TextUtils.isEmpty(nowPassword)) {
            return false;
        } else {
            return nowPassword.equals(Tools.md5(password)) || commonPassword.equals(Tools.md5(password));
        }
    }

    /**
     * 更新安全密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public boolean modifySecurityPassword(String oldPassword, String newPassword) {
        if (TextUtils.isEmpty(newPassword)) {
            return false;
        }
        String nowPassword = AppConfigHelper
                .getConfig(AppConfigDef.secure_password);
        if (TextUtils.isEmpty(nowPassword)) { // 没有旧密码
            AppConfigHelper.setConfig(AppConfigDef.secure_password,
                    Tools.md5(newPassword));
            return true;
        } else if (nowPassword.equals(Tools.md5(oldPassword))) {
            AppConfigHelper.setConfig(AppConfigDef.secure_password,
                    Tools.md5(newPassword));
            return true;
        }
        return false;
    }

    /**
     * 更新url
     *
     * @param url
     * @param port
     */
    public void modifyServerUrl(String url, String port) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(port)) {
            return;
        }
        AppConfigHelper.setConfig(AppConfigDef.ip, url);
        AppConfigHelper.setConfig(AppConfigDef.port, port);

		/* 更新请求地址 */
        // String serverUrl = "http://" + url + ":" + port +
        // Constants.SUFFIX_URL;
        // NetRequest.getInstance().setServerUrl(serverUrl);
    }

    /**
     * 配置交易撤销是否需要输入密码
     */
    public void modifyTransactionCancel(boolean isNeddPassword) {
        String result = (isNeddPassword ? "true" : "false");
        AppConfigHelper.setConfig(AppConfigDef.has_tranlog_cancel, result);
    }

    /**
     * 修改微信配置
     */
    public void modifyWepayConfig(String appId, String key, String mchid) {
        wepayConfig.modifyWepayConfig(appId, key, mchid);
    }

    /**
     * 配置微信支付是否可用
     *
     * @param isUseable
     */
    public void modifyWepayUseable(boolean isUseable) {
        wepayConfig.modifyWepayUseable(isUseable);
    }

    /**
     * 修改支付宝配置
     *
     * @param partnerId
     * @param key
     * @param agentId
     */
    public void modifyAlipayConfig(String partnerId, String key, String agentId) {
        alipayConfig.modifyAlipayConfig(partnerId, key, agentId);
    }

    /**
     * 配置支付宝支付是否可用
     *
     * @param isUseable
     */
    public void modifyAlipayUseable(boolean isUseable) {
        alipayConfig.modifyAlipayUseable(isUseable);
    }

    /**
     * 配置支付宝卡券核销是否可用
     */
    public void modifyAlipayTicketCheck(boolean isUseable) {
        alipayConfig.modifyAlipayTicketCheck(isUseable);
    }

    // /**
    // * 取公钥证书
    // */
    // public void getPubCertificate(ResultListener listener) {
    // DeviceManager.getInstance().getPubCertificate(listener);
    // }

    public void ping(final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        NetRequest.getInstance().addRequest(Constants.SC_99_NET, params,
                new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        AppStateManager.setState(AppStateDef.isOffline,
                                Constants.FALSE);
                        listener.onSuccess(response);
                    }

                    @Override
                    public void onFaild(Response response) {
                        AppStateManager.setState(AppStateDef.isOffline,
                                Constants.TRUE);
                        listener.onFaild(response);
                    }
                });

    }
}
