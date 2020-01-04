package com.lc.baseui.listener.http;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface HttpClientImplListener<T> {

    void onSuccess(ArrayList<T> lists);

    void onSuccess(T bean);

    void onError(String msg);

}
