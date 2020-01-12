package com.lc.librefreshlistview.linear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.lc.librefreshlistview.R;
import com.lc.librefreshlistview.base.BaseLinearRecycleView;


/**
 * Created by Administrator on 2017/4/13.
 */

public class SimpleLinearRecycleView<T> extends BaseLinearRecycleView<T> {
    public SimpleLinearRecycleView(Context context) {
        super(context);
    }

    public SimpleLinearRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleLinearRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private View view;

    @Override
    protected void initView() {
        view = LayoutInflater.from(context).inflate(getLayout(), null);
        //refreshView
        xRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        setRefreshView();
        //recycleView
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_base);
        setRecyclerView();
    }

    @Override
    protected View getView() {
        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.base_layout_xrefresh_recycleview;
    }

}
