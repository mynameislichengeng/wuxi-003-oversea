package com.lc.baseui.listener.http;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface HttpClientRefreshListener extends HttpClientImplListener {

    /**
     * 最后一页
     **/
    public void onTotalPage(String pag);

    /**
     * 设置当前页
     **/
    public void onCurrenPage(String pag);
}
