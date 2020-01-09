package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.adapter.RefundWarnAdapterParam;
import com.wizarpos.pay2.lite.R;

public class RefundClickAdapter extends BaseRecycleAdapter<DailyDetailResp> {

    public RefundClickAdapter(Context context, DailyDetailResp itemList) {
        this.context = context;
        this.t = itemList;
    }

    @Override
    protected int getLayout() {
        return R.layout.adapter_left_tv_right_tv;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
        TextView tvValue = holder.getView(R.id.tv_adapter_value);

        String[] stuName = context.getResources().getStringArray(R.array.refund_adapter_item_title);
        String title = null;
        String value = null;
        DailyDetailResp refundWarnAdapterParam = getT();

        switch (position) {
            case 0:
                title = stuName[0];
                value = refundWarnAdapterParam.getSingleAmount();
                break;
            case 1:
                title = stuName[1];
                value = refundWarnAdapterParam.getTransType();
                break;
            case 2:
                title = stuName[2];
                value = refundWarnAdapterParam.getPayTime();
                break;
            case 3:
                title = stuName[3];
                value = refundWarnAdapterParam.getPayTime();
                break;
            case 4:
                title = stuName[4];
                value = refundWarnAdapterParam.getRefundAmount();
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        tvTitle.setText(title);
        tvValue.setText(value);
    }


}
