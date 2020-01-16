package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.widget.TextView;

import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay2.lite.R;


public class TranDetailAdapter extends RefundClickAdapter {

    public TranDetailAdapter(Context context, DailyDetailResp itemList) {
        super(context, itemList);
        stuName = context.getResources().getStringArray(R.array.tran_detail_adapter_item_title);
    }


    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        super.onBindViewHolder(holder, position);
        DailyDetailResp adpterParam = getT();
//        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
//        int dipLeft = (int) context.getResources().getDimension(R.dimen.dp_10);
//        tvTitle.setPadding(dipLeft, 0, 0, 0);

        TextView tvValue = holder.getView(R.id.tv_adapter_value);

        if (position == 3) {
            String value = adpterParam.getSn();
//            String value = "HS10010002000006700001";
            tvValue.setText(value);

        } else if (position == 4) {
            String value = adpterParam.getPayTime();
            tvValue.setText(value);
        }
    }
}
