package com.lc.librefreshlistview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.listener.RefreshEventListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/13.
 */

public  abstract class AbstractBaseRecycleView<T> extends RelativeLayout {

    public AbstractBaseRecycleView(Context context) {
        super(context);
        init(context);
    }

    public AbstractBaseRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractBaseRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    protected Context context;

    protected abstract View getView();

    protected abstract void initView();

    protected abstract int getLayout();

    //刷新的refreshView
    protected XRefreshView xRefreshView;
    //列表view
    protected RecyclerView recyclerView;
    //刷新响应事件
    protected RefreshEventListener refreshEventListener;
    protected boolean isTop;


    private void init(Context context) {
        this.context = context;
        initView();
        addView(getView());
    }

    /**
     * 设置监听回调
     **/
    public void setRefreshEventListener(RefreshEventListener refreshEventListener) {
        this.refreshEventListener = refreshEventListener;
    }


    /**
     * 设置recycleview的关系
     * **/
    public void setRecycleViewAdapter(BaseRecycleAdapter adapter){
        if(adapter!=null){
            recyclerView.setAdapter(adapter);
        }
    }
    /**
     * 获得recycleView
     * **/
    public RecyclerView getRecycleView(){
        return recyclerView;
    }
    protected XRefreshView.XRefreshViewListener listener = new XRefreshView.XRefreshViewListener() {
        @Override
        public void onRefresh() {

        }

        /**
         * 下拉刷新，触发的回调
         * **/
        @Override
        public void onRefresh(boolean b) {
            operateTopRefresh(b);
        }

        /**
         * 加载更多，触发
         * **/
        @Override
        public void onLoadMore(boolean b) {
            operateBottomLoad(b);
        }

        @Override
        public void onRelease(float v) {

        }

        @Override
        public void onHeaderMove(double v, int i) {

        }
    };

    /**
     * 顶部下拉刷新
     **/
    private void operateTopRefresh(boolean isManual) {
        setIsTop(true);
        if (refreshEventListener != null) {
            refreshEventListener.onTopDownRefresh(isManual);
        }
    }

    /**
     * 底部上拉加载更多
     **/
    private void operateBottomLoad(boolean isSilence) {
        setIsTop(false);
        if (refreshEventListener != null) {
            refreshEventListener.onBottomLoadMore(isSilence);
        }
    }
    /**
     * 刷新完成
     * **/
    protected void operateRefreshComplete(){
        if (refreshEventListener != null) {
            refreshEventListener.onCompleteRefresh();
        }
    }

    private void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }
}
