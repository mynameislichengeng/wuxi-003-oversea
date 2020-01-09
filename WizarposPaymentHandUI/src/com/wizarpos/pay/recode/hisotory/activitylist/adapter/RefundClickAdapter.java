package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.adapter.RefundWarnAdapterParam;
import com.wizarpos.pay2.lite.R;

import org.w3c.dom.Text;

public class RefundClickAdapter extends BaseRecycleAdapter<DailyDetailResp> {
    private String[] stuName;

    public RefundClickAdapter(Context context, DailyDetailResp itemList) {
        this.context = context;
        stuName = context.getResources().getStringArray(R.array.refund_adapter_item_title);
        this.t = itemList;
    }

    @Override
    protected int getLayout() {
        return R.layout.adapter_left_tv_right_tv;
    }

    @Override
    public int getItemCount() {
        return stuName.length;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
        TextView tvValue = holder.getView(R.id.tv_adapter_value);


        String title = null;
        String value = null;
        DailyDetailResp refundWarnAdapterParam = getT();

        switch (position) {
            case 0:
                title = stuName[0];
                value = Calculater.divide100(refundWarnAdapterParam.getSingleAmount());
                break;
            case 1:
                title = stuName[1];
                value = refundWarnAdapterParam.getTransName();
                break;
            case 2:
                title = stuName[2];
                String tranlog = refundWarnAdapterParam.getTranLogId();
                if (TextUtils.isEmpty(tranlog)) {
                    tranlog = "";
                } else {
                    if (tranlog.length() > 12) {
                        String a = tranlog.substring(0, 6);
                        String b = tranlog.substring(tranlog.length() - 6);
                        tranlog = a + b;
                    }
                }
                value = tranlog;
                break;
            case 3:
                title = stuName[3];
                value = refundWarnAdapterParam.getPayTime();
                break;
            case 4:
                title = stuName[4];
                value = Calculater.divide100(refundWarnAdapterParam.getRefundAmount());
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
