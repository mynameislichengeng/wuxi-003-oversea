package com.lc.librefreshlistview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.lc.librefreshlistview.holder.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这个adapter主要需要实现其2个方法
 * getLayout()
 * onBindViewHolder()
 * <p>
 * Created by Administrator on 2017/4/13.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<SimpleRecycleViewHodler> {
    protected List<T> lists;
    protected OnItemClick onItemClick;//每一项布局响应事件
    protected Context context;


    /**
     * 获得布局
     **/
    protected abstract int getLayout();

    @Override
    public SimpleRecycleViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                getLayout(), parent, false);
        SimpleRecycleViewHodler vh = new SimpleRecycleViewHodler(v);
        return vh;
    }

    /**
     * 用于实现具体的加载逻辑
     **/
    @Override
    public abstract void onBindViewHolder(SimpleRecycleViewHodler holder, int position);

    @Override
    public int getItemCount() {
        return lists != null ? lists.size() : 0;
    }

    /**
     * 用于修改adapter
     **/
    public void changeAdapter() {
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    /**
     * 用于快速加载响应的布局Id
     *
     * @param holder hodler
     * @param resId  布局文件中的id
     **/
    protected View getWidget(SimpleRecycleViewHodler holder, int resId) {
        View view = ViewHolder.get(holder.getView(), resId);
        return view;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }

    public List<T> getLists() {
        return this.lists;
    }

    /**
     * 点击每一项的回掉
     **/
    public static interface OnItemClick<T> {
        public void onItemClick(int position, T t);
    }

    /**
     * toast提示
     **/
    protected void toast(int res) {
        if (context != null) {
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * toast提示
     **/
    protected void toast(String str) {
        if (context != null) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }


}
