package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.data.TranLogIdDataUtil;

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
        return R.layout.adapter_acti_refund_detail_item;
    }

    @Override
    public int getItemCount() {
        return stuName.length;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        //进行一下初始化
        holder.setVisibility(true);
        TextView tvTitle = holder.getView(R.id.tv_adapter_title);
        TextView tvValue = holder.getView(R.id.tv_adapter_value);
        TextView tvBottom = holder.getView(R.id.tv_adapter_bottom);
        tvBottom.setVisibility(View.GONE);
        settingTextSize(tvTitle, tvValue, tvBottom);
        settingTextViewMaxWidth(tvValue, R.dimen.dp_150);
        settingTextViewMaxWidth(tvBottom, R.dimen.dp_150);

        DailyDetailResp refundWarnAdapterParam = getT();

        switch (position) {
            case 0://Sale Amount
                setText(tvTitle, stuName[0]);
                setSaleAmountItem(tvValue, refundWarnAdapterParam);
                return;
            case 2://Payment Type:
                setText(tvTitle, stuName[1]);
                setPaymentTypeItem(tvValue, refundWarnAdapterParam);
                return;
            case 3://Receipt#:
                setText(tvTitle, stuName[2]);
                setReceiptItem(tvValue, tvBottom, refundWarnAdapterParam);
                settingTextViewMaxWidth(tvValue, R.dimen.dp_160);
                settingTextViewMaxWidth(tvBottom, R.dimen.dp_160);
                return;
            case 1://time
                setText(tvTitle, stuName[3]);
                setPaytimeItem(tvValue, refundWarnAdapterParam);
                return;
            case 6://Refund Amount:
                setText(tvTitle, stuName[4]);
                setReFundAmountItem(tvValue, refundWarnAdapterParam);
                return;
            case 4://Invoice#:
                setText(tvTitle, stuName[5]);
                setInvoiceItem(holder, tvValue, refundWarnAdapterParam);
                return;
            case 5://Remark:
                setText(tvTitle, stuName[6]);
                setRemarkItem(holder, tvValue, refundWarnAdapterParam);
                return;
            default:
                break;
        }

    }


    public void setInputRefundAmount(String inputRefundAmount) {
        this.inputRefundAmount = inputRefundAmount;
    }

    protected void setSaleAmountItem(TextView tv, DailyDetailResp refundWarnAdapterParam) {
        String sympol = TransRecordLogicConstants.TRANSCURRENCY.getSymbol(refundWarnAdapterParam.getTransCurrency());
        String value = sympol + Calculater.divide100(refundWarnAdapterParam.getTransAmount());
        setText(tv, value);
    }

    protected void setPaymentTypeItem(TextView tv, DailyDetailResp refundWarnAdapterParam) {
        String value = refundWarnAdapterParam.getTransName();
        setText(tv, value);
    }

    protected void setReceiptItem(TextView tvTop, TextView tvBottom, DailyDetailResp refundWarnAdapterParam) {
        String tranlog = refundWarnAdapterParam.getTranLogId();
        String topValue = TranLogIdDataUtil.createTranlogFormatMid(tranlog);
        setText(tvTop, topValue);
        //补充一下，下面的显示
        String bottomValue = TranLogIdDataUtil.createTranlogFormatSuffixlog(tranlog);
        tvBottom.setVisibility(View.VISIBLE);
        setText(tvBottom, bottomValue);

    }

    protected void setReFundAmountItem(TextView tv, DailyDetailResp refundWarnAdapterParam) {
        String sympol = TransRecordLogicConstants.TRANSCURRENCY.getSymbol(refundWarnAdapterParam.getTransCurrency());
        String value = sympol + this.inputRefundAmount;
        setText(tv, value);
    }


    protected void setPaytimeItem(TextView tv, DailyDetailResp refundWarnAdapterParam) {
        String value = refundWarnAdapterParam.getPayTime();
        setText(tv, value);
    }

    protected void setInvoiceItem(SimpleRecycleViewHodler holder, TextView tv, DailyDetailResp refundWarnAdapterParam) {
        String merchantTradeCode = refundWarnAdapterParam.getMerchantTradeCode();
        if (!TextUtils.isEmpty(merchantTradeCode)) {
            String value = merchantTradeCode;
            setText(tv, value);
        } else {
            holder.setVisibility(false);
        }
    }


    protected void setRemarkItem(SimpleRecycleViewHodler holder, TextView tv, DailyDetailResp refundWarnAdapterParam) {
        String diffCode = refundWarnAdapterParam.getDiffCode();
        if (TextUtils.isEmpty(diffCode)) {
            holder.setVisibility(false);
        } else {
            setText(tv, diffCode);
        }
    }


    protected void setText(TextView tv, String value) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        tv.setText(value);
    }


    protected void settingTextSize(TextView tvTitle, TextView tvValue, TextView tvBottom) {
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvBottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    protected void settingTextViewMaxWidth(TextView tvValue, int res) {
        float maxwidth = context.getResources().getDimension(res);
        tvValue.setMaxWidth((int) maxwidth);
    }


}
