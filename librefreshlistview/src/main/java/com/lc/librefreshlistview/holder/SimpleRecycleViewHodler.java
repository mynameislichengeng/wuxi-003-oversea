package com.lc.librefreshlistview.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * recycleViewHolder
 * Created by Administrator on 2017/4/13.
 */

public class SimpleRecycleViewHodler extends RecyclerView.ViewHolder {
    private View rootView;
    private SparseArray<View> sparseArrayViews = new SparseArray<>();

    public SimpleRecycleViewHodler(View itemView) {
        super(itemView);
        this.rootView = itemView;
    }

    public View getView() {
        return rootView;
    }

    public <T extends View> T getView(int viewId) {
        T view = (T) sparseArrayViews.get(viewId);
        if (view == null) {
            view = (T) rootView.findViewById(viewId);
            sparseArrayViews.put(viewId, view);
        }
        return view;
    }

}
