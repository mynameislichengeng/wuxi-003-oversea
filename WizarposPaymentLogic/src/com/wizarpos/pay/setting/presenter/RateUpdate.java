package com.wizarpos.pay.setting.presenter;

import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by blue_sky on 2016/11/5.
 */

public class RateUpdate {
    public void rateUpdate(final ResponseListener listener){
        Map<String,Object>params = new HashMap<>();
        NetRequest.getInstance().addRequest(Constants.SC_957_RATE_UPDATE, params, new ResponseListener() {
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
