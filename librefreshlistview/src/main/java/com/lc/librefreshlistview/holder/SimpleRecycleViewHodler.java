package com.lc.librefreshlistview.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recycleViewHolder
 * Created by Administrator on 2017/4/13.
 */

public class SimpleRecycleViewHodler extends RecyclerView.ViewHolder {
    private View view;
    public SimpleRecycleViewHodler(View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public View getView() {
        return view;
    }
}
