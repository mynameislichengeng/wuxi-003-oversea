package com.lc.baseui.fragment.impl;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.fragment.base.BaseViewPagerFragment;
import com.lc.baseui.listener.http.HttpClientImplListener;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

import java.util.ArrayList;

/**
 * 需要进行的步骤：
 * 1:initData
 * 2:firstview是用来做 除listview以外的其他view
 * 3:doGetData()  请求数据
 * 4：operateOnSuccessCallback 只做了简单的listview,如果还有其他view则可以进行复写
 * 5:onItemClick,每一项的单击事件
 * Created by Administrator on 2017/4/17.
 */

public abstract class AbstractSimpleListFragment<T> extends BaseViewPagerFragment<T> implements BaseRecycleAdapter.OnItemClick<T>, HttpClientImplListener<T> {
    private String TAG = AbstractSimpleListFragment.class.getSimpleName();

    protected abstract void initData();

    protected abstract void firstView(View view);

    protected abstract BaseRecycleAdapter getAdapter();

    /**
     * 请求数据
     **/
    public abstract void doGetData();

    @Override
    protected int getLayout() {
        return R.layout.layout_linear_listview;
    }

    //控件
    protected RecyclerView recyclerView;
    //adapter
    protected BaseRecycleAdapter adapter;
    protected ArrayList<T> listDatas;
    protected Context context;

    @Override
    protected void initView(View view) {
        initData();
        init();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        firstView(view);
        doGetData();
    }


    private void init() {
        if (listDatas == null) {
            listDatas = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = getAdapter();
        }
        if (adapter != null) {
            adapter.setLists(listDatas);
            adapter.setOnItemClick(this);
        }
    }


    /**
     * 添加数据
     **/
    private void addItem(ArrayList<T> otherLists) {
        if (otherLists != null) {
            if (this.listDatas == null) {
                init();
            }
            this.listDatas.addAll(otherLists);
        }
    }

    /**
     * 清除数据
     **/
    public void clearItem() {
        if (listDatas != null) {
            listDatas.clear();
        }
    }

    /**
     * 改变adapter
     **/
    protected void setLayoutChangeAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 获得成功后的回调
     **/
    private void operateOnSuccessCallback(ArrayList<T> otherLists) {
        addItem(otherLists);
        setLayoutChangeAdapter();

    }

    /**
     * 请求失败的回调
     **/
    private void operateOnErrorCallback(String msg) {
        toast(msg);
    }

    /**
     * 接受主界面传递activity传递过来的数据
     **/
    @Override
    public void onActivitySendEvent(ArrayList<T> lists) {
        operateOnSuccessCallback(lists);
    }

    /**
     * 请求成功,回调
     **/
    @Override
    public void onSuccess(final ArrayList<T> lists) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnSuccessCallback(lists);
            }
        });
    }

    /**
     * 请求失败，回调
     **/
    @Override
    public void onSuccess(T bean) {

    }

    @Override
    public void onError(final String msg) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hiddenProgressDialog();
                operateOnErrorCallback(msg);
            }
        });
    }


}
