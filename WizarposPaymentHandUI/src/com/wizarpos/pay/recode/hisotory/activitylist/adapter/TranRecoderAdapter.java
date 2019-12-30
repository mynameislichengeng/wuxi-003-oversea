package com.wizarpos.pay.recode.hisotory.activitylist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.TransDetailResp;
import com.wizarpos.pay.recode.util.date.SimpleDateManager;
import com.wizarpos.pay.ui.newui.adapter.TranlogDetailAdapter;
import com.wizarpos.pay.ui.newui.entity.GroupEntity;
import com.wizarpos.pay.ui.newui.util.TodayTotalUtil;
import com.wizarpos.pay2.lite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TranRecoderAdapter extends BaseAdapter {

    private Context context;
    private List<DailyDetailResp> mList;


    private TranlogDetailAdapter.OnTranLogDetialListener onTranLogDetialListener;

    public TranRecoderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            //3.创建完ViewHolder后初始化ViewHolder
            viewHolder = new ViewHolder();
            //2.如果view是空的,直接填入内容,item完成后就可以创建ViewHolder了.
            convertView = LayoutInflater.from(context).inflate(R.layout.recode_item_transaction_records, null);

            //group
            viewHolder.groupDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.groupMonth = (TextView) convertView.findViewById(R.id.one_status_name);
            viewHolder.gropMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            viewHolder.lin_group = (LinearLayout) convertView.findViewById(R.id.lin_trans_recorde_month_header);

            //children
            viewHolder.rlItemContent = (RelativeLayout) convertView.findViewById(R.id.rlItemContent);
            viewHolder.llDetail = (LinearLayout) convertView.findViewById(R.id.llDetail);
            viewHolder.tvTranAmount = (TextView) convertView.findViewById(R.id.tvTranAmount);
            viewHolder.tvTranType = (TextView) convertView.findViewById(R.id.tvTranType);
            viewHolder.tvTranMode = (TextView) convertView.findViewById(R.id.tvTranMode);
            viewHolder.tvTranDate = (TextView) convertView.findViewById(R.id.tvTranDate);
            viewHolder.llTranLogId = ((LinearLayout) convertView.findViewById(R.id.llTranLogId));
            viewHolder.tvMasterTranLogId = ((TextView) convertView.findViewById(R.id.tvMasterTranLogId));
            viewHolder.tvTranLogId = (TextView) convertView.findViewById(R.id.tvTranLogId);
            viewHolder.tvOperatorId = (TextView) convertView.findViewById(R.id.tvOperatorId);
            viewHolder.btnPrint = (Button) convertView.findViewById(R.id.btnPrint);
            viewHolder.btnRevoke = (Button) convertView.findViewById(R.id.btnRevoke);
            viewHolder.ivArrowRight = (ImageView) convertView.findViewById(R.id.ivArrowRight);
            viewHolder.ivBottomLine = (ImageView) convertView.findViewById(R.id.ivBottomLine);
            viewHolder.ivTranlogIcon = (ImageView) convertView.findViewById(R.id.ivTranlogIcon);
            viewHolder.ivLastBottomLine = (ImageView) convertView.findViewById(R.id.ivLastBottomLine);
            viewHolder.tvBelowOperator = (TextView) convertView.findViewById(R.id.tvBeloweOptName);
            viewHolder.llBelowOpt = (LinearLayout) convertView.findViewById(R.id.llBeloweOptName);
            viewHolder.llTopOpt = (LinearLayout) convertView.findViewById(R.id.llTopOptName);
            viewHolder.llItemBtns = (LinearLayout) convertView.findViewById(R.id.llItemBtns);
            //TODO 卡券核销扣减部分暂不显示
            convertView.findViewById(R.id.llTranlogDetailReduce).setVisibility(View.GONE);
            convertView.findViewById(R.id.llTranlogDetailReduceAmount).setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        operateGroupView(position, viewHolder);
        operateChildView(position, viewHolder);


        return convertView;
    }


    private void operateChildView(int position, ViewHolder viewHolder) {
        final DailyDetailResp item = getmList().get(position);
        viewHolder.tvTranAmount.setText(Calculater.formotFen(item.getSingleAmount()));
        if (TextUtils.isEmpty(item.getTransKind())) {

        } else if (item.getTransKind().contains(context.getString(R.string.pay_tag))) {//消费判断字符比较需处理
//            viewHolder.ivTranlogIcon.setImageResource(R.drawable.ic_xiaofei);
            viewHolder.ivTranlogIcon.setImageResource(TodayTotalUtil.getPayItemImage(item.getTransName()));
            initBtnsView(viewHolder, true);
        } else if (item.getTransKind().contains(context.getString(R.string.refund_tag))) {//消费判断字符比较需处理
            viewHolder.ivTranlogIcon.setImageResource(R.drawable.ic_repeal);
            initBtnsView(viewHolder, false);
        }
        viewHolder.tvTranType.setText(item.getTransKind());
        if ("Union Pay QR".equals(item.getTransName())) {
            viewHolder.tvTranMode.setText(item.getTransName());
        } else {
            viewHolder.tvTranMode.setText(item.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.refund_tag), ""));
        }
        viewHolder.tvTranDate.setText(getPayTime(item.getPayTime()));
        String masterTranLogId = Tools.deleteMidTranLog(item.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
        String tranlogId = Tools.deleteMidTranLog(item.getMasterTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));

        if (context.getString(R.string.pay_tag).equals(item.getTransKind())) {
            viewHolder.llTranLogId.setVisibility(View.GONE);
        } else {
            viewHolder.llTranLogId.setVisibility(View.VISIBLE);
            viewHolder.tvTranLogId.setText(masterTranLogId);
        }
        viewHolder.tvMasterTranLogId.setText(tranlogId);
        if (item.getOptName() != null) {
            viewHolder.llTopOpt.setVisibility(View.GONE);
            viewHolder.llBelowOpt.setVisibility(View.VISIBLE);
            viewHolder.tvBelowOperator.setText(TextUtils.isEmpty(item.getOptName()) ? "" : item.getOptName());
        }
        if (item.isExpand) {
            viewHolder.llDetail.setVisibility(View.VISIBLE);
            viewHolder.ivBottomLine.setVisibility(View.VISIBLE);
            viewHolder.ivArrowRight.setImageResource(R.drawable.btn_back_up);
        } else {
            viewHolder.llDetail.setVisibility(View.GONE);
            viewHolder.ivBottomLine.setVisibility(View.GONE);
            viewHolder.ivArrowRight.setImageResource(R.drawable.icon_down);
        }
//        if (isLastChild) {
//            viewHolder.ivLastBottomLine.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.ivLastBottomLine.setVisibility(View.GONE);
//        }


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.rlItemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isExpand) {
                    finalViewHolder.llDetail.setVisibility(View.GONE);
                    finalViewHolder.ivBottomLine.setVisibility(View.GONE);
                    finalViewHolder.ivArrowRight.setImageResource(R.drawable.icon_down);
                    item.isExpand = false;
                } else {
                    finalViewHolder.llDetail.setVisibility(View.VISIBLE);
                    finalViewHolder.ivBottomLine.setVisibility(View.VISIBLE);
                    finalViewHolder.ivArrowRight.setImageResource(R.drawable.btn_back_up);
                    item.isExpand = true;
                }
                //除详情显示bug @yaosong [20160429]
                if (TextUtils.isEmpty(item.getTransKind())) {

                } else if (item.getTransKind().contains(context.getString(R.string.pay_tag))) {
                    initBtnsView(finalViewHolder, true);
                } else if (item.getTransKind().contains(context.getString(R.string.refund_tag))) {
                    initBtnsView(finalViewHolder, false);
                }
            }
        });
        viewHolder.btnPrint.setOnClickListener(new View.OnClickListener() {//打印
            @Override
            public void onClick(View view) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onPrint(item);
                }
            }
        });
        viewHolder.btnRevoke.setOnClickListener(new View.OnClickListener() {//打印
            @Override
            public void onClick(View view) {
                if (onTranLogDetialListener != null) {
                    onTranLogDetialListener.onRevoke(item);
                }
            }
        });
    }


    private void operateGroupView(int position, ViewHolder viewHolder) {
        DailyDetailResp dailyDetailResp = getmList().get(position);
        //显示月
        if (isGroupView(position)) {
            viewHolder.lin_group.setVisibility(View.VISIBLE);
            Date m = SimpleDateManager.fromYYYYMMDDHHMMSS(dailyDetailResp.getTransTime());
            String date = SimpleDateManager.toStrYYYYMMDD(m.getTime());
            date = date.replace("-", "");
            if (date != null) {
                viewHolder.groupDate.setText(date.substring(date.length() - 2, date.length()));
                viewHolder.groupMonth.setText(turnMonth(date));
            }
//           viewHolder.gropMoney.setText("$" + Calculater.formotFen(groupList.get(groupPosition).getAmount()));
        } else {
            viewHolder.lin_group.setVisibility(View.GONE);
        }
    }


    public void setDataChanged(List<DailyDetailResp> groupList) {
        changeUpdate(groupList);
        setmList(groupList);
        this.notifyDataSetChanged();
    }

    public void addDataChanged(List<DailyDetailResp> groupList) {
        if (groupList != null && groupList.size() > 0) {
            changeUpdate(groupList);
            if (getmList() == null) {
                setmList(groupList);
            } else {
                getmList().addAll(groupList);
            }
            this.notifyDataSetChanged();
        }

    }

    public void clearList() {
        getmList().clear();
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

    private void initBtnsView(final ViewHolder viewHolder, boolean isShowRevoke) {
        if (isShowRevoke) {
            viewHolder.btnPrint.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            viewHolder.btnRevoke.setVisibility(View.VISIBLE);
            viewHolder.btnRevoke.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        } else {
            viewHolder.btnPrint.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2f));
            viewHolder.btnRevoke.setVisibility(View.GONE);
            viewHolder.btnRevoke.setLayoutParams(new LinearLayout.LayoutParams(
                    0, 0, 0f));
        }
    }


    private boolean isGroupView(int position) {
        if (position == 0) {
            return true;
        }
        if (position > 0) {
            DailyDetailResp last = getmList().get(position - 1);
            String lastPayTime = last.getTransTime();
            DailyDetailResp current = getmList().get(position);
            String currentPayTime = current.getTransTime();
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date lastDate = simpleDateFormat.parse(lastPayTime);
                Date currentDate = simpleDateFormat.parse(currentPayTime);
                if (lastDate.getTime() != currentDate.getTime()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    private String turnMonth(String date) {
        StringBuilder sb = new StringBuilder();
        String month = date.substring(date.length() - 4, date.length() - 2);
//        sb.append(month);
        sb.append(getConvertMonth(month) + "/");
        sb.append(date.substring(0, 4));
        return sb.toString();
    }

    private String getConvertMonth(String month) {
        switch (month) {
            case "01":
                return context.getString(R.string.jan_simple);
            case "02":
                return context.getString(R.string.feb_simple);
            case "03":
                return context.getString(R.string.mar_simple);
            case "04":
                return context.getString(R.string.apr_simple);
            case "05":
                return context.getString(R.string.may_simple);
            case "06":
                return context.getString(R.string.june_simple);
            case "07":
                return context.getString(R.string.july_simple);
            case "08":
                return context.getString(R.string.aug_simple);
            case "09":
                return context.getString(R.string.sept_simple);
            case "10":
                return context.getString(R.string.oct_simple);
            case "11":
                return context.getString(R.string.nov_simple);
            case "12":
                return context.getString(R.string.dec_simple);
            default:
                return "";
        }
    }

    static class ViewHolder {

        public LinearLayout lin_group;
        public TextView groupDate;
        public TextView groupMonth;
        public TextView groupName;
        public TextView gropMoney;


        public TextView tvTranType;//类型
        public TextView tvTranMode;//交易
        public TextView tvTranDate;//日期
        public TextView tvTranAmount;//金额
        public TextView tvTranLogId;//交易流水
        public TextView tvOperatorId;//操作员账号
        public TextView tvBelowOperator;//操作员账号
        public Button btnPrint;
        public Button btnRevoke;

        public ImageView iv_star, iv_trash, ivArrowRight, ivBottomLine, ivTranlogIcon, ivLastBottomLine;
        public TextView childTitle;
        public RelativeLayout rlItemContent;
        public LinearLayout llDetail, llItemBtns;
        public LinearLayout llBelowOpt;
        public LinearLayout llTopOpt;
        public LinearLayout llTranLogId;
        public TextView tvMasterTranLogId;
    }

    public void setmList(List<DailyDetailResp> mList) {
        this.mList = mList;
    }

    public void setOnTranLogDetialListener(TranlogDetailAdapter.OnTranLogDetialListener onTranLogDetialListener) {
        this.onTranLogDetialListener = onTranLogDetialListener;
    }

    public List<DailyDetailResp> getmList() {
        return mList;
    }

    public TranlogDetailAdapter.OnTranLogDetialListener getOnTranLogDetialListener() {
        return onTranLogDetialListener;
    }
}
