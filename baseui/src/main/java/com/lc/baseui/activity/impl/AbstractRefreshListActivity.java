package com.lc.baseui.activity.impl;

import android.text.TextUtils;
import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.activity.base.TitleFragmentActivity;
import com.lc.baseui.listener.http.HttpClientRefreshListener;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.linear.SimpleLinearRecycleView;
import com.lc.librefreshlistview.listener.RefreshEventListener;

import java.util.ArrayList;

/**
 * 需要实现以下方法：
 * initData:
 * getTextTitle(): 标题
 * getAdapter: adapter实例化
 * firstEnter：如果还有额外的view需要实例化，就可以在这个方法里面
 * doGetData()：将获得数据的方法加进来
 * onItemClick:单击每一项的响应事件
 * 一般的刷新下拉列表，并且是带分页的
 */

public abstract class AbstractRefreshListActivity<T> extends TitleFragmentActivity implements RefreshEventListener, HttpClientRefreshListener, BaseRecycleAdapter.OnItemClick {

    protected abstract void firstEnter(View view);

    protected abstract void doGetData();

    protected abstract String getTextTitle();

    protected abstract BaseRecycleAdapter getAdapter();

    //数据
    protected String currentPage;//当前页
    protected String totalPage;//总页数
    protected BaseRecycleAdapter adapter;
    protected ArrayList<T> listDatas;
    protected final String pageNumbers = PAGENUMS;//每页显示的总数
    //控件
    protected SimpleLinearRecycleView recycleView;


    protected void doNext() {
        if (checkIsLast()) {
            return;
        } else {
            hiddenProgressDialog();
            doGetData();
        }

    }

    @Override
    public int getContentLayout() {
        return R.layout.layout_linear_refrsh_listview;
    }

    @Override
    public void init(View view) {
        init();
        if (!TextUtils.isEmpty(getTextTitle())) {
            setTitleCenter(getTextTitle());
        }
        recycleView = (SimpleLinearRecycleView) view.findViewById(R.id.rel_list);
        recycleView.setRefreshEventListener(this);
        recycleView.setRecycleViewAdapter(adapter);
        firstEnter(view);
        doNext();
    }

    private void init() {
        listDatas = new ArrayList<>();
        adapter = getAdapter();
        if (adapter != null) {
            adapter.setLists(listDatas);
            adapter.setOnItemClick(this);
        }

    }

    /**
     * 判断是否到了最后一项
     **/
    protected boolean checkIsLast() {
        if (TextUtils.isEmpty(getCurrentPage())) {
            setCurrentPage("0");
            return false;
        }
        if (TextUtils.isEmpty(getTotalPage())) {
            return false;
        }
        int cur = Integer.valueOf(getCurrentPage());
        int total = Integer.valueOf(getTotalPage());
        if (cur < total) {
            cur++;
            setCurrentPage(String.valueOf(cur));
            return false;
        } else {
            operateLastPage();
            return true;
        }
    }

    /**
     * 更新布局
     **/
    public void setLayoutChangeAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 停止刷新
     **/
    public void stopRefresh() {
        recycleView.stopRefresh();
    }

    /**
     * 添加项目
     */
    public void addItem(ArrayList list) {
        if (list != null) {
            listDatas.addAll(list);
        }
    }

    /**
     * 清除所有的项目
     * **/
    protected void clearItem(){
        listDatas.clear();
    }

    /**
     * 添加成功，回调操作
     **/
    protected void operateOnSuccess(ArrayList list) {
        hiddenProgressDialog();
        addItem(list);
        stopRefresh();
        setLayoutChangeAdapter();
    }

    /**
     * 添加失败的 回调操作
     **/
    protected void operateOnError(String msg) {
        hiddenProgressDialog();
        toast(msg);
        stopRefresh();
    }

    /**
     * 当到最后一页时
     **/
    protected void operateLastPage() {
        hiddenProgressDialog();
        toast(R.string.to_last_page);
        stopRefresh();
    }

    /**
     * 下拉刷新
     **/
    @Override
    public void onTopDownRefresh(boolean isManual) {
        doNext();
    }

    /**
     * 上拉加载
     **/
    @Override
    public void onBottomLoadMore(boolean isSilence) {
        doNext();
    }

    @Override
    public void onCompleteRefresh() {

    }

    /**
     * 请求成功的回调
     **/
    @Override
    public void onSuccess(final ArrayList lists) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operateOnSuccess(lists);
            }
        });
    }

    /**
     * 请求成功的回调
     **/
    @Override
    public void onSuccess(Object bean) {

    }

    /**
     * 设置最后一页
     **/
    @Override
    public void onTotalPage(String pag) {
        if (!TextUtils.isEmpty(pag)) {
            setTotalPage(pag);
        }

    }

    /**
     * 设置当前页
     **/
    public void onCurrenPage(String pag) {
        if (!TextUtils.isEmpty(pag)) {
            setCurrentPage(pag);
        }
    }

    /**
     * 请求失败的回调
     **/
    @Override
    public void onError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operateOnError(msg);
            }
        });
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }
}
