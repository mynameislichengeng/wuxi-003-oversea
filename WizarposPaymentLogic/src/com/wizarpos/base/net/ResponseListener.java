package com.wizarpos.base.net;

/**
 * Created by wu on 16/3/13.
 */
public abstract class ResponseListener implements ThreadResponseListener {

    public com.wizarpos.base.net.Response doInBackgroud(com.wizarpos.base.net.Response response) {
        return response; //默认不处理
    }

    @Override
    public void onCanceled(com.wizarpos.base.net.Response response) {

    }
}
