package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay2.lite.R;


public class TranDetailAdapter extends RefundClickAdapter {

    public TranDetailAdapter(Context context, DailyDetailResp itemList) {
        super(context, itemList);
    }

    @Override
    public int getItemCount() {
        return stuName.length - 1;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        super.onBindViewHolder(holder, position);
        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
        int dipLeft = (int) context.getResources().getDimension(R.dimen.dp_10);
        tvTitle.setPadding(dipLeft, 0, 0, 0);
    }
}
