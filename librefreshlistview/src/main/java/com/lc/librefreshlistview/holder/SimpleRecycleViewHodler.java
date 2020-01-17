package com.lc.librefreshlistview.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

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

    public void setVisibility(boolean isVisible) {
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (isVisible) {
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }
}
