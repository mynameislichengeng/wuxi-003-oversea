package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.adapter.RefundWarnAdapterParam;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.constants.TransRecordLogicConstants;

import org.w3c.dom.Text;

public class RefundClickAdapter extends BaseRecycleAdapter<DailyDetailResp> {
    protected String[] stuName;
    private String inputRefundAmount;//用户当前输入需要退款的金额

    public RefundClickAdapter(Context context, DailyDetailResp itemList) {
        this.context = context;
        stuName = context.getResources().getStringArray(R.array.refund_adapter_item_title);
        this.t = itemList;
    }

    @Override
    protected int getLayout() {
        return R.layout.adapter_left_tv_right_tv_2_ends_aligned;
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
        String sympol = null;
        switch (position) {
            case 0:
                title = stuName[0];
                sympol = TransRecordLogicConstants.TRANSCURRENCY.getSymbol(refundWarnAdapterParam.getTransCurrency());

                value = sympol + Calculater.divide100(refundWarnAdapterParam.getTransAmount());
                break;
            case 1:
                title = stuName[1];

                value = refundWarnAdapterParam.getTransName();
                break;
            case 2:
                title = stuName[2];
                String tranlog = refundWarnAdapterParam.getTranLogId();
                tranlog = Tools.deleteMidTranLog(tranlog, AppConfigHelper.getConfig(AppConfigDef.mid));
                value = tranlog;
                break;
            case 3:
                title = stuName[3];
                value = refundWarnAdapterParam.getPayTime();
                break;
            case 4:
                title = stuName[4];
                sympol = TransRecordLogicConstants.TRANSCURRENCY.getSymbol(refundWarnAdapterParam.getTransCurrency());
                value = sympol + this.inputRefundAmount;
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


    public void setInputRefundAmount(String inputRefundAmount) {
        this.inputRefundAmount = inputRefundAmount;
    }
}
