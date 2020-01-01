package com.lc.librefreshlistview.listener;

/**
 * 刷新事件回调
 * Created by Administrator on 2017/4/13.
 */

public interface RefreshEventListener {
    /**
     * 在顶部，下拉刷新，触发回调
     * @param isManual true表示人为的，false表示是自动
     **/
    public void onTopDownRefresh(boolean isManual);

    /**
     * 在底部，上拉加载，触发回调
     * @param isSilence 是不是静默加载，静默加载即不显示footerview，自动监听滚动到底部并触发此回调
     **/
    public void onBottomLoadMore(boolean isSilence);

    /**
     * 表示刷新或者加载更多完成，回调
     **/
    public void onCompleteRefresh();
}
