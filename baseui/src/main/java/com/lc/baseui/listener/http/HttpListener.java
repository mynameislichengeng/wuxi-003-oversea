package com.lc.baseui.listener.http;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface HttpListener {

    public void onSuccess(String msg);

    public void onError(String msg);
}
