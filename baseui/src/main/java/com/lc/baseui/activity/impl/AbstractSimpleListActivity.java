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
 * 这是一个简单的请求的列表，
 * initData：一定要把adapter实现
 * firstEnter:如果需要在view后，继续初始化其他的控件那么就要实现了
 * getTextTitle():标题
 * getAdapter(): 实例话adapter
 * doGetData() 加载的数据
 * Created by Administrator on 2017/4/27.
 */

public abstract class AbstractSimpleListActivity<T> extends TitleFragmentActivity implements View.OnClickListener, BaseRecycleAdapter.OnItemClick<T>, HttpClientImplListener<T> {

    public abstract void firstEnter(View view);

    /**
     * 获得数据
     **/
    public abstract void doGetData();

    public abstract String getTextTitle();

    public abstract BaseRecycleAdapter getAdapter();

    //控件
    protected RecyclerView recyclerView;
    protected ImageAndTextButton btn;
    protected View viewline;
    protected BaseRecycleAdapter<T> adapter;
    protected ArrayList<T> lists;

    //btn的显示状态
    protected int visibleBtn = View.GONE;

    @Override
    public int getContentLayout() {
        return R.layout.layout_listview_and_bottom_btn;
    }

    @Override
    public void init(View view) {
        init();
        if (!TextUtils.isEmpty(getTextTitle())) {
            setTitleCenter(getTextTitle());
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        btn = (ImageAndTextButton) view.findViewById(R.id.btn_nums_1);
        viewline = view.findViewById(R.id.iv_bottom);
        if (btn != null) {
            btn.setVisibility(visibleBtn);
            btn.setClickEvent(this, this);
        }
        if (viewline != null) {
            viewline.setVisibility(visibleBtn);
        }
        firstEnter(view);
        doGetData();
    }

    private void init() {
        lists = new ArrayList<>();
        adapter = getAdapter();
        if (adapter != null) {
            adapter.setLists(lists);
            adapter.setOnItemClick(this);
        }

    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 更新数据
     **/
    public void setLaoutChangeAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onSuccess(final ArrayList<T> lists) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operateSuccessCallback(lists);
            }
        });
    }

    @Override
    public void onSuccess(final T t) {

    }

    @Override
    public void onError(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                operateErrorCallback(msg);
            }
        });
    }

    /**
     * 添加每一项的数据
     **/
    public void addItems(ArrayList<T> lists) {
        if (lists != null) {
            this.lists.addAll(lists);
        }
    }

    /**
     * 删除某一项
     *
     * @param position 删除的位置
     **/
    public void deleteItem(int position) {
        if (lists != null) {
            this.lists.remove(position);
        }
    }

    /**
     * 清除列表某一项
     **/
    public void clearData() {
        if (lists != null) {
            this.lists.clear();
        }
    }

    /**
     * 请求成功,后的操作
     **/
    public void operateSuccessCallback(final ArrayList<T> lists) {
        hiddenProgressDialog();
        addItems(lists);
        setLaoutChangeAdapter();
    }

    /**
     * 请求失败后的操作
     **/
    public void operateErrorCallback(String msg) {
        hiddenProgressDialog();
        toast(msg);
    }


}
