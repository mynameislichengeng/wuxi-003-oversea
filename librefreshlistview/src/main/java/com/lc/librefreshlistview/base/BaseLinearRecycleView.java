package com.lc.librefreshlistview.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.lc.librefreshlistview.base.AbstractBaseRecycleView;


/**
 * Created by Administrator on 2017/4/13.
 */

public abstract class BaseLinearRecycleView<T> extends AbstractBaseRecycleView<T> {

    public BaseLinearRecycleView(Context context) {
        super(context);
    }

    public BaseLinearRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLinearRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置上拉加载
     */
    public void settingEnablePullUpLoad(boolean flag) {
        if (xRefreshView != null) {
            xRefreshView.setPullLoadEnable(flag);
        }
    }

    /**
     * 设置下拉刷新
     */
    public void settingEnablePullToRefresh(boolean flag) {
        if (xRefreshView != null) {
            xRefreshView.setPullRefreshEnable(flag);
        }
    }

    /**
     * 加载完成
     **/
    public void stopRefresh() {
        stopRefresh(true);
    }

    /**
     * 加载完成
     *
     * @param operate 是否有隐藏头部和底部布局
     *                当时top时，true为隐藏，false不隐藏
     *                当为bottom时，true为隐藏,false为不隐藏
     **/
    public void stopRefresh(boolean operate) {
        if (xRefreshView != null) {
            if (isTop) {
                xRefreshView.stopRefresh(operate);
                operateRefreshComplete();
            } else {
                xRefreshView.stopLoadMore(operate);
                operateRefreshComplete();
            }
        }
    }

    /**
     * 设置refreshview的一些基本设置
     **/
    protected void setRefreshView() {
        if (xRefreshView != null) {
            //设置刷新完成以后，headerview固定的时间
            xRefreshView.setPinnedTime(500);
            xRefreshView.setMoveForHorizontal(true);

            xRefreshView.setPullLoadEnable(true);
            xRefreshView.setAutoLoadMore(false);

            xRefreshView.enableReleaseToLoadMore(true);
            xRefreshView.enableRecyclerViewPullUp(true);
            xRefreshView.enablePullUpWhenLoadCompleted(true);
            xRefreshView.setXRefreshViewListener(listener);


        }
    }


    /**
     * 设置recycleview
     **/
    protected void setRecyclerView() {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        }
    }


}
