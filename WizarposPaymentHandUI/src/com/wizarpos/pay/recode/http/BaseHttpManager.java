package com.wizarpos.pay.recode.http;

import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;

import java.util.Map;

public class BaseHttpManager {

    public static void doPost(String url, Map<String, Object> params, final HttpNewCallback httpNewCallback) {
        NetRequest.getInstance().addRequest(url, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                int code = response.code;
                if (code == 0) {
                    if (httpNewCallback != null) {
                        httpNewCallback.onSuccess(response.result.toString());
                    }
                } else {
                    onFaild(response);
                }
            }

            @Override
            public void onFaild(Response response) {
                if (httpNewCallback != null) {
                    httpNewCallback.onError(response.code, response.msg);
                }
            }
        });
    }

    public static void doPost(String url, Object paramsObject, final HttpNewCallback httpNewCallback) {
        NetRequest.getInstance().addRequest(url, paramsObject, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                int code = response.code;
                if (code == 0) {
                    if (httpNewCallback != null) {
                        httpNewCallback.onSuccess(response.result.toString());
                    }
                } else {
                    onFaild(response);
                }
            }

            @Override
            public void onFaild(Response response) {
                if (httpNewCallback != null) {
                    httpNewCallback.onError(response.code, response.msg);
                }
            }
        });
    }
}
