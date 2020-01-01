package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
//import com.wizarpos.pay.ui.newui.adapter.TranlogDetailAdapter;
import com.wizarpos.pay.recode.hisotory.activitylist.callback.OnTranLogDetialListener;
import com.wizarpos.pay.ui.newui.util.TodayTotalUtil;
import com.wizarpos.pay2.lite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        operateChildView(position, holder.getView());
    }

    private void operateChildView(int position, final View viewHolder) {
        final DailyDetailResp item = getLists().get(position);

        TextView tvTranAmount = viewHolder.findViewById(R.id.tvTranAmount);
        tvTranAmount.setText(Calculater.formotFen(item.getSingleAmount()));

        ImageView ivTranlogIcon = viewHolder.findViewById(R.id.ivTranlogIcon);
        if (TextUtils.isEmpty(item.getTransKind())) {

        } else if (item.getTransKind().contains(context.getString(R.string.pay_tag))) {//消费判断字符比较需处理
//            viewHolder.ivTranlogIcon.setImageResource(R.drawable.ic_xiaofei);
            ivTranlogIcon.setImageResource(TodayTotalUtil.getPayItemImage(item.getTransName()));
            initBtnsView(viewHolder, true);
        } else if (item.getTransKind().contains(context.getString(R.string.refund_tag))) {//消费判断字符比较需处理
            ivTranlogIcon.setImageResource(R.drawable.ic_repeal);
            initBtnsView(viewHolder, false);
        }

        TextView tvTranType = (TextView) viewHolder.findViewById(R.id.tvTranType);
        tvTranType.setText(item.getTransKind());

        TextView tvTranMode = (TextView) viewHolder.findViewById(R.id.tvTranMode);
        if ("Union Pay QR".equals(item.getTransName())) {
            tvTranMode.setText(item.getTransName());
        } else {
            tvTranMode.setText(item.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.refund_tag), ""));
        }
        TextView tvTranDate = (TextView) viewHolder.findViewById(R.id.tvTranDate);
        tvTranDate.setText(item.getPayTime());
        String masterTranLogId = Tools.deleteMidTranLog(item.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
        String tranlogId = Tools.deleteMidTranLog(item.getMasterTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));

        LinearLayout llTranLogId = ((LinearLayout) viewHolder.findViewById(R.id.llTranLogId));
        TextView tvTranLogId = (TextView) viewHolder.findViewById(R.id.tvTranLogId);
        if (context.getString(R.string.pay_tag).equals(item.getTransKind())) {
            llTranLogId.setVisibility(View.GONE);
        } else {
            llTranLogId.setVisibility(View.VISIBLE);
            tvTranLogId.setText(masterTranLogId);
        }
        TextView tvMasterTranLogId = ((TextView) viewHolder.findViewById(R.id.tvMasterTranLogId));
        tvMasterTranLogId.setText(tranlogId);

        LinearLayout llTopOpt = (LinearLayout) viewHolder.findViewById(R.id.llTopOptName);
        LinearLayout llBelowOpt = (LinearLayout) viewHolder.findViewById(R.id.llBeloweOptName);
        TextView tvBelowOperator = (TextView) viewHolder.findViewById(R.id.tvBeloweOptName);
        if (item.getOptName() != null) {
            llTopOpt.setVisibility(View.GONE);
            llBelowOpt.setVisibility(View.VISIBLE);
            tvBelowOperator.setText(TextUtils.isEmpty(item.getOptName()) ? "" : item.getOptName());
        }

        final LinearLayout llDetail = (LinearLayout) viewHolder.findViewById(R.id.llDetail);
        final ImageView ivBottomLine = (ImageView) viewHolder.findViewById(R.id.ivBottomLine);
        final ImageView ivArrowRight = (ImageView) viewHolder.findViewById(R.id.ivArrowRight);
        if (item.isExpand) {
            llDetail.setVisibility(View.VISIBLE);
            ivBottomLine.setVisibility(View.VISIBLE);
            ivArrowRight.setImageResource(R.drawable.btn_back_up);
        } else {
            llDetail.setVisibility(View.GONE);
            ivBottomLine.setVisibility(View.GONE);
            ivArrowRight.setImageResource(R.drawable.icon_down);
        }
//        if (isLastChild) {
//            viewHolder.ivLastBottomLine.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.ivLastBottomLine.setVisibility(View.GONE);
//        }


        RelativeLayout rlItemContent = (RelativeLayout) viewHolder.findViewById(R.id.rlItemContent);
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
        Button btnPrint = (Button) viewHolder.findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {//打印
            @Override
            public void onClick(View view) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onPrint(item);
                }
            }
        });
        Button btnRevoke = (Button) viewHolder.findViewById(R.id.btnRevoke);
        btnRevoke.setOnClickListener(new View.OnClickListener() {//打印
            @Override
            public void onClick(View view) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onRevoke(item);
                }
            }
        });
    }


    public  void setDataChanged(List<DailyDetailResp> groupList) {
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
            resp.setExpand(false);
            resp.setSingleAmount(resp.getTrasnAmount());
            resp.setTransName(resp.getTransKind());
            resp.setTransTime(resp.getTran_time());
            resp.setPayTime(resp.getPayTime());
            resp.setTransName(Constants.TRAN_TYPE.get(resp.getTransType()));
            resp.setMasterTranLogId(resp.getMasterTranLogId());
            resp.setTranlogId(resp.getTranLogId());
            resp.setRefundAmount(resp.getRefundAmount());
            resp.setOptName(resp.getOptName());
            resp.setTipAmount(resp.getTipAmount());
            resp.setThirdTradeNo(resp.getThirdTradeNo());
            resp.setExchangeRate(resp.getExchangeRate());
            resp.setCnyAmount(resp.getCnyAmount());
            if (!TextUtils.isEmpty(resp.getThirdExtName())) {
                resp.setThirdExtName(resp.getThirdExtName());
            }
            if (!TextUtils.isEmpty(resp.getThirdExtId())) {
                resp.setThirdExtId(resp.getThirdExtId());
            }
            if (TextUtils.isEmpty(resp.getTransName())) {//bugfix 万一返回了本地没有的TranType则不显示此数据 Song
                removeList.add(resp);
            }
        }
        list.removeAll(removeList);
        return list;
    }

    /**
     * 只要小时，不要月份
     *
     * @return
     */
    private String getPayTime(String payTime) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(payTime);
            SimpleDateFormat hh = new SimpleDateFormat("HH:mm:ss");
            String str = hh.format(date);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return payTime;
    }

    private void initBtnsView(final View viewHolder, boolean isShowRevoke) {
        Button btnPrint = (Button) viewHolder.findViewById(R.id.btnPrint);
        Button btnRevoke = (Button) viewHolder.findViewById(R.id.btnRevoke);
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

    public OnTranLogDetialListener getOnTranLogDetialListener() {
        return onTranLogDetialListener;
    }

    public void setOnTranLogDetialListener(OnTranLogDetialListener onTranLogDetialListener) {
        this.onTranLogDetialListener = onTranLogDetialListener;
    }

//    public void setmList(List<DailyDetailResp> mList) {
//        this.mList = mList;
//    }



//    public List<DailyDetailResp> getmList() {
//        return mList;
//    }




//
//        static class ViewHolder {
//
//            public LinearLayout lin_group;
//            public TextView groupDate;
//            public TextView groupMonth;
//            public TextView groupName;
//            public TextView gropMoney;
//
//
//            public TextView tvTranType;//类型
//            public TextView tvTranMode;//交易
//            public TextView tvTranDate;//日期
//            public TextView tvTranAmount;//金额
//            public TextView tvTranLogId;//交易流水
//            public TextView tvOperatorId;//操作员账号
//            public TextView tvBelowOperator;//操作员账号
//            public Button btnPrint;
//            public Button btnRevoke;
//
//            public ImageView iv_star, iv_trash, ivArrowRight, ivBottomLine, ivTranlogIcon, ivLastBottomLine;
//            public TextView childTitle;
//            public RelativeLayout rlItemContent;
//            public LinearLayout llDetail, llItemBtns;
//            public LinearLayout llBelowOpt;
//            public LinearLayout llTopOpt;
//            public LinearLayout llTranLogId;
//            public TextView tvMasterTranLogId;
//        }


//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            //3.创建完ViewHolder后初始化ViewHolder
//            viewHolder = new ViewHolder();
//            //2.如果view是空的,直接填入内容,item完成后就可以创建ViewHolder了.
//            convertView = LayoutInflater.from(context).inflate(R.layout.recode_item_transaction_records, null);
//
//            //group
//            viewHolder.groupDate = (TextView) convertView.findViewById(R.id.tvDate);
//            viewHolder.groupMonth = (TextView) convertView.findViewById(R.id.one_status_name);
//            viewHolder.gropMoney = (TextView) convertView.findViewById(R.id.tvMoney);
//            viewHolder.lin_group = (LinearLayout) convertView.findViewById(R.id.lin_trans_recorde_month_header);
//
//            //children
//            viewHolder.rlItemContent = (RelativeLayout) convertView.findViewById(R.id.rlItemContent);
//            viewHolder.llDetail = (LinearLayout) convertView.findViewById(R.id.llDetail);
//            viewHolder.tvTranAmount = (TextView) convertView.findViewById(R.id.tvTranAmount);
//            viewHolder.tvTranType = (TextView) convertView.findViewById(R.id.tvTranType);
//            viewHolder.tvTranMode = (TextView) convertView.findViewById(R.id.tvTranMode);
//            viewHolder.tvTranDate = (TextView) convertView.findViewById(R.id.tvTranDate);
//            viewHolder.llTranLogId = ((LinearLayout) convertView.findViewById(R.id.llTranLogId));
//            viewHolder.tvMasterTranLogId = ((TextView) convertView.findViewById(R.id.tvMasterTranLogId));
//            viewHolder.tvTranLogId = (TextView) convertView.findViewById(R.id.tvTranLogId);
//            viewHolder.tvOperatorId = (TextView) convertView.findViewById(R.id.tvOperatorId);
//            viewHolder.btnPrint = (Button) convertView.findViewById(R.id.btnPrint);
//            viewHolder.btnRevoke = (Button) convertView.findViewById(R.id.btnRevoke);
//            viewHolder.ivArrowRight = (ImageView) convertView.findViewById(R.id.ivArrowRight);
//            viewHolder.ivBottomLine = (ImageView) convertView.findViewById(R.id.ivBottomLine);
//            viewHolder.ivTranlogIcon = (ImageView) convertView.findViewById(R.id.ivTranlogIcon);
//            viewHolder.ivLastBottomLine = (ImageView) convertView.findViewById(R.id.ivLastBottomLine);
//            viewHolder.tvBelowOperator = (TextView) convertView.findViewById(R.id.tvBeloweOptName);
//            viewHolder.llBelowOpt = (LinearLayout) convertView.findViewById(R.id.llBeloweOptName);
//            viewHolder.llTopOpt = (LinearLayout) convertView.findViewById(R.id.llTopOptName);
//            viewHolder.llItemBtns = (LinearLayout) convertView.findViewById(R.id.llItemBtns);
//            //TODO 卡券核销扣减部分暂不显示
//            convertView.findViewById(R.id.llTranlogDetailReduce).setVisibility(View.GONE);
//            convertView.findViewById(R.id.llTranlogDetailReduceAmount).setVisibility(View.GONE);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        operateGroupView(position, viewHolder);
//        operateChildView(position, viewHolder);
//
//
//        return convertView;
//    }
}
