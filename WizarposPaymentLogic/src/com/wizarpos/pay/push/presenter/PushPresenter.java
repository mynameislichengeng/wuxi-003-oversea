package com.wizarpos.pay.push.presenter;

import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.utils.MD5Util;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 苏震 on 2017/10/30.
 */

public class PushPresenter {
    public void registerTerminal(String clientId, final ResponseListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mid", AppConfigHelper.getConfig(AppConfigDef.mid));
        params.put("sn", AppConfigHelper.getConfig(AppConfigDef.sn));
        params.put("pushServiceId", "1");
        params.put("clientId", clientId);
        NetRequest.getInstance().addNewRequest("http://192.168.2.130:7080/push-server/push/registerTerminal/", params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }
}
