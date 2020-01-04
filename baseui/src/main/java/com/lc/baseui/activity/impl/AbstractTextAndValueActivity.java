package com.lc.baseui.activity.impl;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.lc.baseui.listener.http.HttpClientImplListener;
import com.lc.baseui.widget.button.ImageAndTextButton;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

import java.util.ArrayList;

/**
 * 需要做的：
 * initData
 * 1: getAdapter() 一定要实例化
 * 2：getTextTitle() 设置显示标题
 * 3：firstEnter() 如果有额外的view需要初始化，那么就要可以放在这一步里
 * 5：doGetData() 获得数据，比如查询数据库和http请求
 * <p>
 * 如果需要底部按钮的事件，那么就需要复写
 * Created by Administrator on 2017/4/27.
 */

public abstract class AbstractTextAndValueActivity<T> extends TitleFragmentActivity implements BaseRecycleAdapter.OnItemClick, View.OnClickListener, HttpClientImplListener<T> {


    public abstract void firstEnter(View view);

    public abstract BaseRecycleAdapter getAdapter();

    public abstract String getTextTitle();

    public abstract T getEntity();

    public abstract void doGetData();

    //控件
    protected RecyclerView recyclerView;
    protected ImageAndTextButton btn;
    protected BaseRecycleAdapter adapter;
    protected T t;
    //btn显示状态
    protected int visibleStatusBtn = View.GONE;

    @Override
    public int getContentLayout() {
        return R.layout.layout_listview_and_bottom_btn;
    }

    @Override
    public void init(View view) {
        if (!TextUtils.isEmpty(getTextTitle())) {
            setTitleCenter(getTextTitle());
        }
        init();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        btn = (ImageAndTextButton) view.findViewById(R.id.btn_nums_1);
        btn.setImageViewVisible(View.GONE);
        if (btn != null) {
            btn.setVisibility(visibleStatusBtn);
            btn.setClickEvent(this);
        }
        firstEnter(view);
        doGetData();
    }

    private void init() {
        adapter = getAdapter();
        if (adapter != null) {
            t = getEntity();
            adapter.setT(t);
            adapter.setOnItemClick(this);
        }
    }

    /**
     * 更新数据
     **/
    public void addData(T t) {
        this.t = t;
    }

    /**
     * 更新adapter
     **/
    public void setLayoutChangeAdapter(T t) {
        if (adapter != null) {
            adapter.setT(t);
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 获取数据成功后的操作
     **/
    public void operateOnSuccess(T t) {
        addData(t);
        setLayoutChangeAdapter(t);
    }

    /**
     * 获取数据失败后的操作
     **/
    public void operateOnError(String msg) {

        toast(msg);
    }

    /**
     * 单击底部button响应事件
     **/
    @Override
    public void onClick(View v) {

    }


    @Override
    public void onSuccess(final T t) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnSuccess(t);
            }
        });
    }

    @Override
    public void onError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnError(msg);
            }
        });
    }

    @Override
    public void onSuccess(ArrayList lists) {

    }
}
