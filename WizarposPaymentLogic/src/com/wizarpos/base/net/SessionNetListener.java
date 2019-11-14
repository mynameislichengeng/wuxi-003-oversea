package com.wizarpos.base.net;

import android.text.TextUtils;

import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UuidUitl;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.UserEntityDao;

import java.util.HashMap;
import java.util.Map;

import static com.wizarpos.base.net.SessionListener.SESSION_ERROR_CODE;

/**
 * Created by Song on 2016/10/31.
 */

public class SessionNetListener extends ResponseListener {
    private String serviceCode;
    private Object params;
    private Object tag;
    private ThreadResponseListener listener;

    public SessionNetListener(String serviceCode, Object params, Object tag, ThreadResponseListener listener) {
        this.serviceCode = serviceCode;
        this.params = params;
        this.tag = tag;
        this.listener = listener;
    }

    @Override
    public void onSuccess(Response response) {
        if (Constants.SESSION_FLAG) {
            String sessionID = response.sessionId;
            if (!TextUtils.isEmpty(sessionID)) {
                AppConfigHelper.setConfig(AppConfigDef.sessionId, sessionID);
            }
        }
        listener.onSuccess(response);
    }

    @Override
    public void onFaild(Response response) {
        if (Constants.SESSION_FLAG) {
            if (response.code == SESSION_ERROR_CODE) {
                //session失效
                if (Constants.SC_100_MERCHANT_INFO_SUBMIT != serviceCode) {
                    reSendRequest(serviceCode, params, tag, listener);
                    return;//此处不进行失败处理
                }
            }
        }
        listener.onFaild(response);
    }

    /**
     * session失效时调登陆后重调上次请求
     *
     * @param serviceCode
     * @param params
     * @param listener
     */
    void reSendRequest(final String serviceCode, final Object params, final Object tag, final ThreadResponseListener listener) {
        Map<String, Object> loginParams = new HashMap<String, Object>();
        String loginOptNo = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
        loginParams.put("loginName", loginOptNo);
        loginParams.put("passwd", UserEntityDao.getInstance().getUser(loginOptNo).getPassword());
        if (null != PaymentApplication.getInstance().getDbController()) {
            loginParams.put("lastTime", AppConfigHelper.getConfig(AppConfigDef.lastOpreatorUpateTime, "0"));
            loginParams.put("mid", AppConfigHelper.getConfig(AppConfigDef.mid));
        }
        String terminalUniqNo;
        if (DeviceManager.getInstance().isWizarDevice() || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_SHENGTENG_M10) {
            terminalUniqNo = android.os.Build.SERIAL;//终端序列号
        } else {
            terminalUniqNo = DeviceManager.getImei(PaymentApplication.getInstance());//IMEI地址
        }
        if (TextUtils.isEmpty(terminalUniqNo)){
            terminalUniqNo = UuidUitl.getUuid();
        }
        loginParams.put("terminalUniqNo", terminalUniqNo);
        NetRequest.getInstance().addRequest(
                Constants.SC_100_MERCHANT_INFO_SUBMIT, loginParams,
                new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        NetRequest.getInstance().addRequest(serviceCode, params, tag, listener);
                    }

                    @Override
                    public void onFaild(Response response) {
                        listener.onFaild(new Response(-1, "登陆已超时，自动登录失败，请重新访问"));
                    }
                });
    }
}
