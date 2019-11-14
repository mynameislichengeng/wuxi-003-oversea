package com.wizarpos.pay.ui.newui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.TransDetailResp;
import com.wizarpos.pay.ui.newui.entity.ChildEntity;
import com.wizarpos.pay.ui.newui.entity.GroupEntity;
import com.wizarpos.pay.ui.newui.entity.OrderMap;
import com.wizarpos.pay.ui.newui.util.TodayTotalUtil;
import com.wizarpos.pay2.lite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vienan on 2015/9/17.
 */
public class TranlogDetailAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int MAXLENGTH = 7;

    private OrderMap mapList;
    private List<GroupEntity> groupList = new ArrayList<>();

    private OnTranLogDetialListener onTranLogDetialListener;


    /**
     * 构造方法
     *
     * @param context
     */
    public TranlogDetailAdapter(Context context) {
        this.context = context;
    }


    /**
     * 返回一级Item总数
     */
    @Override
    public int getGroupCount() {
//        return groupList.size();
        if (mapList == null) {
            return 0;
        }
        return mapList.size();
    }

    /**
     * 返回二级Item总数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
//        if (groupList.get(groupPosition).getChildEntities() == null) {
//            return 0;
//        } else {
//            return groupList.get(groupPosition).getChildEntities().size();
//        }
        //崩溃fix yaosong
        if (mapList == null || mapList.getByPosition(groupPosition) == null) {
            return 0;
        } else {
            return mapList.getByPosition(groupPosition).size();
        }
    }

    /**
     * 获取一级Item内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    /**
     * 获取二级Item内容
     */
    @Override
    public ChildEntity getChild(int groupPosition, int childPosition) {
//        return groupList.get(groupPosition).getChildEntities().get(childPosition);
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = new GroupViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_status_item, null);
//            holder.groupName = (TextView) convertView.findViewById(R.id.one_status_name);
            holder.groupDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.groupMonth = (TextView) convertView.findViewById(R.id.one_status_name);
            holder.gropMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        String date = groupList.get(groupPosition).getGroupName().replace("-", "");
        if (date != null) {
            holder.groupDate.setText(date.substring(date.length() - 2, date.length()));
            holder.groupMonth.setText(turnMonth(date));
        }
//        holder.groupName.setText(groupList.get(groupPosition).getGroupName());
        holder.gropMoney.setText("$" + Calculater.formotFen(groupList.get(groupPosition).getAmount()));
        return convertView;
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

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final DailyDetailResp item = mapList.getByPosition(groupPosition).get(childPosition);
        ChildViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (ChildViewHolder) convertView.getTag();
        } else {
            viewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction_records, null);
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
        }
//        viewHolder.btnRevoke.setText(item.getCancelKind());
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
        if (isLastChild) {
            viewHolder.ivLastBottomLine.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivLastBottomLine.setVisibility(View.GONE);
        }
        final ChildViewHolder finalViewHolder = viewHolder;
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
        return convertView;
    }

    private void initBtnsView(final ChildViewHolder viewHolder, boolean isShowRevoke) {
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


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        TextView groupDate;
        TextView groupMonth;
        TextView groupName;
        TextView gropMoney;
    }

    static class ChildViewHolder {
        public TextView tvTranType;//类型
        public TextView tvTranMode;//交易
        public TextView tvTranDate;//日期
        public TextView tvTranAmount;//金额
        public TextView tvTranLogId;//交易流水
        public TextView tvOperatorId;//操作员账号
        public TextView tvBelowOperator;//操作员账号
        public Button btnPrint;
        public Button btnRevoke;
        public SwipeLayout swipeLayout;
        public ImageView iv_star, iv_trash, ivArrowRight, ivBottomLine, ivTranlogIcon, ivLastBottomLine;
        public TextView childTitle;
        public RelativeLayout rlItemContent;
        public LinearLayout llDetail, llItemBtns;
        public LinearLayout llBelowOpt;
        public LinearLayout llTopOpt;
        public LinearLayout llTranLogId;
        public TextView tvMasterTranLogId;
    }

    public void setDataChanged(List<TransDetailResp> detailRespList) {
        if (detailRespList == null || detailRespList.isEmpty()) {
            this.groupList.clear();
            mapList = null;
        } else {
            groupList = initList(detailRespList);
            mapList = getMap(detailRespList);
        }
        this.notifyDataSetChanged();
    }

    public void addDataChanged(List<GroupEntity> groupList) {
        if (groupList == null || groupList.isEmpty()) {
            return;
        } else {
            this.groupList.addAll(groupList);
        }
    }

    private List<GroupEntity> initList(List<TransDetailResp> list) {
        List<GroupEntity> groupList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GroupEntity entity = new GroupEntity(list.get(i).getTransTime());
            entity.setAmount(list.get(i).getTotalAmount());
            groupList.add(entity);
        }
        return groupList;
    }

    private OrderMap getMap(List<TransDetailResp> list) {
        OrderMap mapList = new OrderMap();
        for (int i = 0; i < list.size(); i++) {
            mapList.put("i" + i, initData(list.get(i).getTransDetail()));
        }
        return mapList;
    }

    private List<DailyDetailResp> initData(List<DailyDetailResp> list) {
        List<DailyDetailResp> removeList = new ArrayList<DailyDetailResp>();
        for (DailyDetailResp resp : list) {
            resp.setExpand(false);
            resp.setSingleAmount(resp.getTrasnAmount());
            resp.setTransName(resp.getTransKind());
            resp.setTransTime(getFormatedDateTime("HH:mm:ss", Long.parseLong(resp.getTran_time())));
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

    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    public void setOnTranLogDetialListener(OnTranLogDetialListener onTranLogDetialListener) {
        this.onTranLogDetialListener = onTranLogDetialListener;
    }

    public interface OnTranLogDetialListener {
        void onPrint(DailyDetailResp resp);

        void onRevoke(DailyDetailResp resp);
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

}
