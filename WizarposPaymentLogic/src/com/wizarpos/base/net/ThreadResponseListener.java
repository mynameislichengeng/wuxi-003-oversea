package com.wizarpos.base.net;

/**
 * Created by wu on 16/3/13.
 */
public interface ThreadResponseListener {

    Response doInBackgroud(Response response);

    void onSuccess(Response response);

    void onFaild(Response response);

    void onCanceled(Response response);
}
