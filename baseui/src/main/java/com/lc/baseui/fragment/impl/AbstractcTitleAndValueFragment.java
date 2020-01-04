package com.lc.baseui.fragment.impl;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.lc.baseui.R;
import com.lc.baseui.adapter.TitleAndValueAdapter;
import com.lc.baseui.fragment.base.BaseViewPagerFragment;
import com.lc.baseui.listener.http.HttpClientImplListener;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

import java.util.ArrayList;

/**
 * 需要继承
 * 1:getEntityT(),设置实体的，一定要
 * 2：getAdapterTitlesName() ,设置列表每一项的title
 * 3: getAdapterLayout(),设置adapter的布局
 * 4：initAdapter(),这个是额外添加的view
 * 5：doGetData()请求数据(如果没有可以不要)
 * 6:getTitleContent()标题
 * 7:onActivitySendEvent()传递的数据可以通过这个界面
 * Created by licheng on 2017/4/28.
 */

public abstract class AbstractcTitleAndValueFragment<T> extends BaseViewPagerFragment<T> implements BaseRecycleAdapter.OnItemClick, HttpClientImplListener<T> {


    public abstract T getEntityT();

    public abstract String[] getAdapterTitlesName();

    public abstract int getAdapterLayout();

    public abstract void init(View view);

    public abstract void doGetData();

    public abstract String getTitleContent();

    protected TitleAndValueAdapter<T> adapter;
    //控件
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    protected T t;

    @Override
    protected int getLayout() {
        return R.layout.layout_scrollview_listview_bottom_fl;
    }

    @Override
    protected void initView(View view) {
        if (t == null) {
            t = getEntityT();
        }
        initAdapter();
        if (!TextUtils.isEmpty(getTitleContent())) {
            setTitle(getTitleContent());
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        frameLayout = (FrameLayout) view.findViewById(R.id.fl_add);
        init(frameLayout);
        doGetData();
    }

    protected void initAdapter() {
        if (adapter == null) {
            adapter = new TitleAndValueAdapter(getContext(), getAdapterTitlesName());
        }
        if (adapter != null) {
            adapter.setT(t);
            adapter.setOnItemClick(this);
            if (getAdapterLayout() != 0) {
                adapter.setLayout(getAdapterLayout());
            }
        }
    }

    /**
     * 设置数据
     **/
    public void setT(T t) {
        if (t != null) {
            this.t = t;
        }
    }

    /**
     * 当数据变化时，界面的变化
     **/
    public void setLayoutChangeAdapter() {
        if (adapter == null) {
            initAdapter();
        }
        adapter.setT(t);
        adapter.notifyDataSetChanged();
    }

    /**
     * 接受主界面传递activity传递过来的数据
     **/
    @Override
    public void onActivitySendEvent(T t) {
        operateOnSuccess(t);
    }


    /**
     * 请求成功后的回调
     **/
    @Override
    public void onSuccess(final ArrayList<T> lists) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
            }
        });
    }

    /**
     * 请求成功后的回调
     **/
    @Override
    public void onSuccess(final T bean) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnSuccess(bean);
            }
        });
    }

    /**
     * 请求失败后的回调
     **/
    @Override
    public void onError(final String msg) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnError(msg);
            }
        });
    }

    /**
     * 返回请求的回调方法
     **/
    public void operateOnSuccess(T t) {
        setT(t);
        setLayoutChangeAdapter();
    }

    /**
     * 返回请求的回调方法
     **/
    public void operateOnError(String msg) {
        toast(msg);
    }


}
