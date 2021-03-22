package com.wizarpos.pay.recode.http;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;

import java.util.Map;

public class BaseHttpManager {


    protected static void doPost(String url, Map<String, Object> params, final HttpNewCallback callback) {
        NetRequest.getInstance().addRequest(url, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {

                int code = response.code;
                if (code == 0) {
                    if (callback != null) {
                        JSONObject resultObj = (JSONObject) response.result;
                        callback.onSuccess(resultObj != null ? resultObj.toJSONString() : "");
                    }
                } else {
                    onFaild(response);
                }
            }

            @Override
            public void onFaild(final Response response) {
                if (callback != null) {
                    callback.onError(response.getMsg());
                }
            }
        });
    }

    protected static void doPost(String url, Object paramsObject, final HttpNewCallback callback) {
        NetRequest.getInstance().addRequest(url, paramsObject, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {

                int code = response.code;
                if (code == 0) {
                    if (callback != null) {
                        JSONObject resultObj = (JSONObject) response.result;
                        callback.onSuccess(resultObj != null ? resultObj.toJSONString() : "");
                    }
                } else {
                    onFaild(response);
                }
            }

            @Override
            public void onFaild(final Response response) {
                if (callback != null) {
                    callback.onError(response.code,response.getMsg());
                }
            }
        });
    }

}
