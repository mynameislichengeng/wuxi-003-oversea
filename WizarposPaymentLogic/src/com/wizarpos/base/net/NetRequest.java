package com.wizarpos.base.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.utils.BeanToMapUtil;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wu on 16/3/13.
 */
public class NetRequest {
    public static final String LOG_TAG = "NetRequest";

    private static NetRequest instance = new NetRequest();

    public static NetRequest getInstance() {
        return instance;
    }

    private NetBundler netBundler;
    private Handler deliver;
    private OkHttpClient okHttpClient;
    private CallManager callManager;

    private ExExecutor exExecutor;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private String[] hijackHead = {"<!DOCTYPE", "<html", "<head", "<title", "<link", "<script"};

    public static final int NETWORK_ERR = 998899;
    public static final int NETWORK_HIJACKHEAD = -998899;

    private NetRequest() {
        netBundler = new NetBundler();
        deliver = new Handler(Looper.myLooper());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES);
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        okHttpClient = builder.build();
        callManager = new CallManager();
    }

    public void addRequest(String serverCode, Map<String, Object> params, ThreadResponseListener listener) {
        addRequest(serverCode, params, null, listener);
    }

    public void addRequest(String serverCode, Object paramsObject, ThreadResponseListener listener) {
        Map<String, Object> params = null;
        try {
            params = BeanToMapUtil.objectToMap(paramsObject);
        } catch (Exception e) {
            listener.onFaild(new Response(-1, "json转换错误"));
            e.printStackTrace();
            return;
        }
        addRequest(serverCode, params, null, listener);
//        addRequest(serviceCode, params, listener);
    }

    public void addRequest(final String serverCode, final Object params, final Object tag, final ThreadResponseListener listener) {
        ExRunnable runnable = new ExRunnable(new ExRunnable.ExRunnableCallback() {
            @Override
            public void doInBackground() {
                final String url = netBundler.getUrl();
                Log.d(LOG_TAG, "url: " + url);
                final String json = netBundler.bundleMsgRequest(serverCode, params);
                final Headers headers = netBundler.bundleHeader();
                Log.d(LOG_TAG, "\nheader:" + headers.toString()+"\n");
                Log.d(LOG_TAG, "\nserviceCode:" + serverCode+"\n");
                Log.d(LOG_TAG, "\nreq:" + json+"\n");
                addPostRequest(url, json, headers, tag, new SessionNetListener(serverCode, params, tag, listener));
            }
        });
        exExecutor().enqueue(runnable);
    }


    /**
     * 单独调用使用，不作为通用的方法
     *
     * @param url
     * @param params
     * @param listener
     */
    public void addNewRequest(final String url, final Object params, final ThreadResponseListener listener) {
        ExRunnable runnable = new ExRunnable(new ExRunnable.ExRunnableCallback() {
            @Override
            public void doInBackground() {
                Log.d(LOG_TAG, "url: " + url);
                String json = com.alibaba.fastjson.JSON.toJSONString(params, SerializerFeature.WriteDateUseDateFormat);
                Log.d(LOG_TAG, "json: " + json);
                Headers.Builder builder = new Headers.Builder();
                builder.add("Content-Type", "application/json;charset=UTF-8");
                Headers headers = builder.build();
                Log.d(LOG_TAG, "headers: " + headers);
                addNewPostRequest(url, json, headers, null, listener);
            }
        });
        exExecutor().enqueue(runnable);
    }


    public Call addPostRequest(String serverCode, Map<String, Object> params, ThreadResponseListener listener) {
        return addPostRequest(serverCode, params, null, listener);
    }

    public Call addPostRequest(String serverCode, Map<String, Object> params, Object tag, ThreadResponseListener listener) {
        String url = netBundler.getUrl();
        String json = netBundler.bundleMsgRequest(serverCode, params);
        Headers headers = netBundler.bundleHeader();
        return addPostRequest(url, json, headers, tag, listener);
    }

    public Call addPostRequest(String url, String json, Headers headers, Object tag, final ThreadResponseListener listener) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .tag(tag)
                .build();
        return doRequestAction(request, listener);
    }

    /**
     * 单独调用使用，不作为通用的方法
     *
     * @param url
     * @param json
     * @param headers
     * @param tag
     * @param listener
     * @return
     */
    public Call addNewPostRequest(String url, String json, Headers headers, Object tag, final ThreadResponseListener listener) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        return doRequestAction(request, listener);
    }

    /**
     * 第三方 post 请求
     */
    public Call addThirdPostRequest(String url, String json, Headers headers, Object tag, Callback responseCallback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .tag(tag)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(responseCallback);
        return call;
    }

    public Call addGetRequest(String url, final ThreadResponseListener listener) {
        return addGetRequest(url, null, listener);
    }

    public Call addGetRequest(String url, Object tag, final ThreadResponseListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        return doRequestAction(request, listener);
    }

    public Call doRequestAction(final Request request, final ThreadResponseListener listener) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                callManager.remove(call);
                if (call.isCanceled()) {
                    onCanceled(request.tag(), listener);
                } else {
                    deliver.post(new Runnable() {
                        @Override
                        public void run() {
                            Response purpose = new Response(NETWORK_ERR, PaymentApplication.getInstance().getResources().getString(R.string.request_failed), e);
                            purpose.setTag(request.tag());
                            listener.onFaild(purpose);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (call.isCanceled()) {
                    onCanceled(request.tag(), listener);
                } else {
                    final Response purpose = listener.doInBackgroud(parseResponse(response));
                    purpose.setTag(request.tag());
                    callManager.remove(call);
                    if (call.isCanceled()) { //doInBackgroud 可能包含耗时操作, 所以再次判断是否已经取消
                        onCanceled(request.tag(), listener);
                    } else {
                        if (purpose.getCode() != 0) {
                            deliver.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onFaild(purpose);

                                }
                            });
                        } else {
                            deliver.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(purpose);

                                }
                            });
                        }
                    }
                }
            }
        });
        if (call.request().tag() != null) {
            callManager.add(call);
        }
        return call;
    }

    private Response parseResponse(okhttp3.Response response) {
        try {
            String result = response.body().string();
            if (TextUtils.isEmpty(result)) {
                return new com.wizarpos.base.net.Response(-1, PaymentApplication.getInstance().getResources().getString(R.string.data_parse_error));
            }
            Log.d("NetRequest", "\nresp: " + result + "\n");
            for (String aHijackHead : hijackHead) {
                if (result.startsWith(aHijackHead)) {
                    String msg = PaymentApplication.getInstance().getResources().getString(R.string.network_hijackhead);
                    if (NetTools.isWifiConnected(PaymentApplication.getInstance())) {
                        msg += "[wifi]";
                    }
                    return new com.wizarpos.base.net.Response(NETWORK_HIJACKHEAD, msg);
                }
            }
            com.wizarpos.base.net.Response resp = new com.wizarpos.base.net.Response();
            JSONObject rjobj = com.alibaba.fastjson.JSON.parseObject(result);
            if (rjobj == null || !rjobj.containsKey("code")) {
                return new com.wizarpos.base.net.Response(-1, PaymentApplication.getInstance().getResources().getString(R.string.data_parse_error));
            }
            int code = rjobj.containsKey("code") ? rjobj.getIntValue("code") : 1;
            String msg = rjobj.getString("msg");
            Object obj = rjobj.containsKey("result") ? rjobj.get("result") : null;
            resp.setCode(code);
            resp.setMsg(msg);
            resp.setResult(obj);
            if (rjobj.containsKey("sessionId")) {
                resp.setSessionId(rjobj.getString("sessionId"));
            }
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new com.wizarpos.base.net.Response(-1, PaymentApplication.getInstance().getResources().getString(R.string.data_parse_error));
    }

    private void onCanceled(final Object tag, final ThreadResponseListener listener) {
        Log.d(LOG_TAG, "canceled");
        deliver.post(new Runnable() {
            @Override
            public void run() {
                Response purpose = new Response(-2, "请求取消");
                purpose.setTag(tag);
                listener.onCanceled(purpose);
            }
        });
    }

    public void cancelFirst(Object tag) {
        callManager.cancel(tag);
    }

    public void cancelAll(Object tag) {
        callManager.cancelAll(tag);
    }

    private ExExecutor exExecutor() {
        if (exExecutor == null) {
            exExecutor = new ExExecutor();
        }
        return exExecutor;
    }
}
