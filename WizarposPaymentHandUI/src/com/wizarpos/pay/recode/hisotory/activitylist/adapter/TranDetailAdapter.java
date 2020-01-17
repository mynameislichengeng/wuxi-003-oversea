package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.log.util.StringUtil;
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
    public int getItemCount() {
        int count = stuName.length;
        return count;

    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        super.onBindViewHolder(holder, position);
        DailyDetailResp adpterParam = getT();

        boolean isVisible = true;
        if (position == 4) {
            //如果不存在该值，那么就隐藏
            String merchantTradeCode = adpterParam.getMerchantTradeCode();
            if (TextUtils.isEmpty(merchantTradeCode)) {
                isVisible = false;
            }
        }
        holder.setVisibility(isVisible);

        TextView tvValue = holder.getView(R.id.tv_adapter_value);
        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
        if (position == 3) {
            //
            tvTitle.setText(stuName[3]);
            //
            String value = adpterParam.getSn();
            tvValue.setText(value);
        } else if (position == 4) {

            String merchantTradeCode = adpterParam.getMerchantTradeCode();
            if (!TextUtils.isEmpty(merchantTradeCode)) {
                tvTitle.setText(stuName[4]);
                tvValue.setText(merchantTradeCode);
            }
        } else if (position == 5) {
            tvTitle.setText(stuName[5]);
            String value = adpterParam.getPayTime();
            tvValue.setText(value);
        }
    }


}
