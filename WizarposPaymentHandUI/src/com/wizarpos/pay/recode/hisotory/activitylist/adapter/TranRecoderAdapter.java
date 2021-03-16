package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;
import com.lc.librefreshlistview.holder.SimpleRecycleViewHodler;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
//import com.wizarpos.pay.ui.newui.adapter.TranlogDetailAdapter;
import com.wizarpos.pay.recode.hisotory.activitylist.callback.OnTranLogDetialListener;
import com.wizarpos.pay.recode.constants.TransRecordConstants;
import com.wizarpos.pay.recode.util.date.SimpleDateManager;
import com.wizarpos.pay.ui.newui.util.TodayTotalUtil;
import com.motionpay.pay2.lite.R;
import com.wizarpos.recode.data.TranLogIdDataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
交易界面list的adapter
 */
public class TranRecoderAdapter extends BaseRecycleAdapter<DailyDetailResp> {

    private Context context;


    private OnTranLogDetialListener onTranLogDetialListener;

    public TranRecoderAdapter(Context context) {
        this.context = context;
    }


    @Override
    protected int getLayout() {
        return R.layout.recode_item_transaction_records;
    }

    @Override
    public void onBindViewHolder(SimpleRecycleViewHodler holder, int position) {
        operateChildView(position, holder);
    }

    private void operateChildView(int position, final SimpleRecycleViewHodler viewHolder) {

        operateSettingLayoutTitleDay(position, viewHolder);
        final DailyDetailResp item = getLists().get(position);

        //显示多少钱
        TextView tvTranAmount = viewHolder.getView(R.id.tvTranAmount);
        tvTranAmount.setText(Calculater.formotFen(item.getTransAmount()));
        //货币类型
        TextView tvTranCurrency = viewHolder.getView(R.id.tvTranCurrency);
        tvTranCurrency.setText(TransRecordConstants.TRANSCURRENCY.getSymbol(item.getTransCurrency()));

        ImageView ivTranlogIcon = viewHolder.getView(R.id.ivTranlogIcon);
        if (TextUtils.isEmpty(item.getTransKind())) {

        } else if (item.getTransKind().contains(context.getString(R.string.pay_tag))) {//消费判断字符比较需处理
            ivTranlogIcon.setImageResource(TodayTotalUtil.getPayItemImage(item.getTransName()));
            initBtnsView(viewHolder, true);
        } else if (item.getTransKind().contains(context.getString(R.string.refund_tag))) {//消费判断字符比较需处理
            ivTranlogIcon.setImageResource(R.drawable.ic_repeal);
            initBtnsView(viewHolder, false);
        }

        TextView tvTranType = (TextView) viewHolder.getView(R.id.tvTranType);
        tvTranType.setText(item.getTransKind());

        TextView tvTranMode = (TextView) viewHolder.getView(R.id.tvTranMode);
        if ("Union Pay QC".equals(item.getTransName())) {
            tvTranMode.setText(item.getTransName());
        } else {
            tvTranMode.setText(item.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.refund_tag), ""));
        }
        TextView tvTranDate = (TextView) viewHolder.getView(R.id.tvTranDate);
        tvTranDate.setText(item.getPayTime());

        //receipt number
        String masterTranlogId = item.getMasterTranLogId();
        TextView tvMasterTranLogId = ((TextView) viewHolder.getView(R.id.tvMasterTranLogId));
        TextView tv_MasterTranLogId_bottom = ((TextView) viewHolder.getView(R.id.tv_MasterTranLogId_bottom));
        tvMasterTranLogId.setText(TranLogIdDataUtil.createTranlogFormatMid(masterTranlogId));
        tv_MasterTranLogId_bottom.setText(TranLogIdDataUtil.createTranlogFormatSuffixlog(masterTranlogId));

        //refund receipt number
        String tranLogId = item.getTranLogId();
        LinearLayout llTranLogId = ((LinearLayout) viewHolder.getView(R.id.llTranLogId));
        TextView tvTranLogId = (TextView) viewHolder.getView(R.id.tvTranLogId);
        TextView tv_tranlogid_bottom = (TextView) viewHolder.getView(R.id.tv_tranlogid_bottom);
        if (context.getString(R.string.pay_tag).equals(item.getTransKind())) {
            llTranLogId.setVisibility(View.GONE);
        } else {
            llTranLogId.setVisibility(View.VISIBLE);
            tvTranLogId.setText(TranLogIdDataUtil.createTranlogFormatMid(tranLogId));
            tv_tranlogid_bottom.setText(TranLogIdDataUtil.createTranlogFormatSuffixlog(masterTranlogId));
        }


        LinearLayout llBelowOpt = (LinearLayout) viewHolder.getView(R.id.llBeloweOptName);
        TextView tvBelowOperator = (TextView) viewHolder.getView(R.id.tvBeloweOptName);
        if (item.getOptName() != null) {
            llBelowOpt.setVisibility(View.VISIBLE);
            tvBelowOperator.setText(TextUtils.isEmpty(item.getOptName()) ? "" : item.getOptName());
        } else {
            llBelowOpt.setVisibility(View.GONE);
        }

        final LinearLayout llDetail = (LinearLayout) viewHolder.getView(R.id.llDetail);

        final ImageView ivBottomLine = (ImageView) viewHolder.getView(R.id.ivBottomLine);
        final ImageView ivArrowRight = viewHolder.getView(R.id.ivArrowRight);

        if (item.isExpand) {
            llDetail.setVisibility(View.VISIBLE);
            ivBottomLine.setVisibility(View.VISIBLE);
            ivArrowRight.setImageResource(R.drawable.btn_back_up);
        } else {
            llDetail.setVisibility(View.GONE);
            ivBottomLine.setVisibility(View.GONE);
            ivArrowRight.setImageResource(R.drawable.icon_down);
        }
        ImageView ivLastBottomLine = (ImageView) viewHolder.getView(R.id.ivLastBottomLine);
        if (true) {
            ivLastBottomLine.setVisibility(View.VISIBLE);
        } else {
            ivLastBottomLine.setVisibility(View.GONE);
        }


        RelativeLayout rlItemContent = (RelativeLayout) viewHolder.getView(R.id.rlItemContent);
        rlItemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isExpand) {
                    llDetail.setVisibility(View.GONE);
                    ivBottomLine.setVisibility(View.GONE);
                    ivArrowRight.setImageResource(R.drawable.icon_down);
                    item.isExpand = false;
                } else {
                    llDetail.setVisibility(View.VISIBLE);
                    ivBottomLine.setVisibility(View.VISIBLE);
                    ivArrowRight.setImageResource(R.drawable.btn_back_up);
                    item.isExpand = true;
                }
                //除详情显示bug @yaosong [20160429]
                if (TextUtils.isEmpty(item.getTransKind())) {

                } else if (item.getTransKind().contains(context.getString(R.string.pay_tag))) {
                    initBtnsView(viewHolder, true);
                } else if (item.getTransKind().contains(context.getString(R.string.refund_tag))) {
                    initBtnsView(viewHolder, false);
                }
            }
        });
        Button btnPrint = (Button) viewHolder.getView(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {//打印
            @Override
            public void onClick(View view) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onPrint(item);
                }
            }
        });
        Button btnRevoke = (Button) viewHolder.getView(R.id.btnRevoke);
        btnRevoke.setOnClickListener(new View.OnClickListener() {//打印
            @Override
            public void onClick(View view) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onRevoke(item);
                }
            }
        });
        Button btn_detail = viewHolder.getView(R.id.btn_detail);
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onDetail(item);
                }
            }
        });

    }


    public void setDataChanged(List<DailyDetailResp> groupList) {
        changeUpdate(groupList);
        setLists(groupList);
        this.notifyDataSetChanged();
    }

    public void addDataChanged(List<DailyDetailResp> groupList) {
        if (groupList != null && groupList.size() > 0) {
            changeUpdate(groupList);
            if (getLists() == null) {
                setLists(groupList);
            } else {
                getLists().addAll(groupList);
            }
            this.notifyDataSetChanged();
        }

    }

    public void clearList() {
        getLists().clear();
        this.notifyDataSetChanged();
    }

    private List<DailyDetailResp> changeUpdate(List<DailyDetailResp> list) {
        List<DailyDetailResp> removeList = new ArrayList<DailyDetailResp>();
        for (DailyDetailResp resp : list) {
//            resp.setExpand(false);
//            resp.setSingleAmount(resp.getTransAmount());
//            resp.setTransName(resp.getTransKind());
//            resp.setTranTime(resp.getTran_time());
//            resp.setPayTime(resp.getPayTime());
//            resp.setTransName(Constants.TRAN_TYPE.get(resp.getTransType()));
//            resp.setMasterTranLogId(resp.getMasterTranLogId());
//            resp.setTranlogId(resp.getTranLogId());
//            resp.setRefundAmount(resp.getRefundAmount());
//            resp.setOptName(resp.getOptName());
//            resp.setTipAmount(resp.getTipAmount());
//            resp.setThirdTradeNo(resp.getThirdTradeNo());
//            resp.setExchangeRate(resp.getExchangeRate());
//            resp.setCnyAmount(resp.getCnyAmount());
//            if (!TextUtils.isEmpty(resp.getThirdExtName())) {
//                resp.setThirdExtName(resp.getThirdExtName());
//            }
//            if (!TextUtils.isEmpty(resp.getThirdExtId())) {
//                resp.setThirdExtId(resp.getThirdExtId());
//            }
            if (TextUtils.isEmpty(resp.getTransName())) {//bugfix 万一返回了本地没有的TranType则不显示此数据 Song
                removeList.add(resp);
            }
        }
        list.removeAll(removeList);
        return list;
    }


    private void initBtnsView(final SimpleRecycleViewHodler viewHolder, boolean isShowRevoke) {
        Button btnPrint = (Button) viewHolder.getView(R.id.btnPrint);
        Button btnRevoke = (Button) viewHolder.getView(R.id.btnRevoke);
        if (isShowRevoke) {

            btnPrint.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

            btnRevoke.setVisibility(View.VISIBLE);
            btnRevoke.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        } else {
            btnPrint.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
            btnRevoke.setVisibility(View.GONE);
            btnRevoke.setLayoutParams(new LinearLayout.LayoutParams(
                    0, 0, 0f));
        }
    }

    public void setOnTranLogDetialListener(OnTranLogDetialListener onTranLogDetialListener) {
        this.onTranLogDetialListener = onTranLogDetialListener;
    }

    /**
     * 设置每天的title
     */
    private void operateSettingLayoutTitleDay(int position, final SimpleRecycleViewHodler viewHolder) {
        final DailyDetailResp item = getLists().get(position);
        RelativeLayout rel_day_header = viewHolder.getView(R.id.rel_day_header);


        boolean isShowTitle = false;

        if (position == 0) {
            isShowTitle = true;
        } else {
            Calendar calendar = Calendar.getInstance();

            //当前
            String tranTime = item.getTranTime();
            Date date = SimpleDateManager.fromYYYYMMDDHHMMSS(tranTime);
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //前一条
            DailyDetailResp beforeItem = getLists().get(position - 1);
            String beforeTranTime = beforeItem.getTranTime();
            Date beforedate = SimpleDateManager.fromYYYYMMDDHHMMSS(beforeTranTime);
            calendar.setTime(beforedate);
            int beforeday = calendar.get(Calendar.DAY_OF_MONTH);

            if (day != beforeday) {
                isShowTitle = true;
            }
        }


        //只有第一项才由这个值
        if (!isShowTitle) {
            rel_day_header.setVisibility(View.GONE);
        } else {
            String timeStr = item.getPayTime();
            rel_day_header.setVisibility(View.VISIBLE);

            TextView tvmonthyear = viewHolder.getView(R.id.tv_month_year);
            Date date = SimpleDateManager.fromYYYYMMDDHHMMSS(timeStr);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                //按照 may12, 2020这样来显示
                StringBuilder sb = new StringBuilder();
                sb.append(SimpleDateManager.getMonthEnglish(month));
                sb.append(day);
                sb.append(",");
                sb.append(" ");
                sb.append(year);

                tvmonthyear.setText(sb.toString());

            } else {
                Log.e("error", "时间有问题");
            }
        }


    }

}
