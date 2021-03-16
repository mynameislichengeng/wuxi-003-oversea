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
import com.motionpay.pay2.lite.R;


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

        DailyDetailResp adpterParam = getT();

        holder.setVisibility(true);
        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
        TextView tvValue = holder.getView(R.id.tv_adapter_value);
        TextView tvBottom = holder.getView(R.id.tv_adapter_bottom);
        tvBottom.setVisibility(View.GONE);
//        settingTextViewMaxWidth(tvValue, R.dimen.dp_180);
//        settingTextViewMaxWidth(tvBottom, R.dimen.dp_180);

        switch (position) {
            case 0:
                setText(tvTitle, stuName[0]);
                setSaleAmountItem(tvValue, adpterParam);
                return;
            case 1:
                setText(tvTitle, stuName[1]);
                setPaymentTypeItem(tvValue, adpterParam);
                return;
            case 2:
                setText(tvTitle, stuName[2]);
//                settingTextViewMaxWidth(tvValue, R.dimen.dp_220);
                setReceiptItem(tvValue, tvBottom, adpterParam);
                return;
            case 3:
                setText(tvTitle, stuName[3]);
                String sn = adpterParam.getSn();
                setText(tvValue, sn);

                return;
            case 4:
                setText(tvTitle, stuName[4]);
                setInvoiceItem(holder, tvValue, adpterParam);
                return;
            case 5:
                setText(tvTitle, stuName[5]);
                setPaytimeItem(tvValue, adpterParam);
//                settingTextViewMaxWidth(tvValue, R.dimen.dp_220);
                return;
            case 6:
                setText(tvTitle, stuName[6]);
                setRemarkItem(holder, tvValue, adpterParam);
                return;

        }
    }


}
